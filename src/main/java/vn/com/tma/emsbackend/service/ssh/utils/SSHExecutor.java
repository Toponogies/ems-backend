package vn.com.tma.emsbackend.service.ssh.utils;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.PropertyResolverUtils;
import org.apache.sshd.common.channel.ChannelOutputStream;
import org.apache.sshd.core.CoreModuleProperties;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

public class SSHExecutor {
    private ChannelShell channelShell;

    private NetworkDevice currentManagedDevice;

    private ClientSession clientSession;

    private SshClient sshClient;

    private static final List<String> END_DELIM_CHAR = Arrays.asList(":", "#", ">");


    public String execute(String command) {
        if (currentManagedDevice == null) {
            throw new DeviceConnectionException((Long) null);
        }

        openChannelShell();

        //get result
        String result = run(command);


        closeChannelShell();
        return result;
    }


    private void openChannelShell() {
        try {
            channelShell = clientSession.createShellChannel();
        } catch (IOException e) {
            throw new ChannelShellOpenException(currentManagedDevice.getId());
        }
    }

    private void closeChannelShell() {
        try {
            channelShell.close();
        } catch (IOException e) {
            throw new ChannelShellCloseException(currentManagedDevice.getId());
        }
    }

    private ClientSession createClientSession() {
        ClientSession newClientSession = null;
        try {
            newClientSession = sshClient.connect(currentManagedDevice.getCredential().getUsername(), currentManagedDevice.getIpAddress(), currentManagedDevice.getSshPort()).verify().getSession();
            newClientSession.addPasswordIdentity(currentManagedDevice.getCredential().getPassword());
            newClientSession.auth().verify();
            CoreModuleProperties.IDLE_TIMEOUT.set(newClientSession, Duration.ZERO);
            return newClientSession;
        } catch (IOException e) {
            throw new DeviceConnectionException(currentManagedDevice.getId());
        }
    }


    private String run(String command) {
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

        //set OutputStream & ErrorStream
        channelShell.setOut(responseStream);
        channelShell.setErr(errorStream);


        setCommand(command);

        //if execution have error
        if (errorStream.toByteArray().length != 0) {
            throw new SSHExecuteException(errorStream.toString());
        }
        if (isCompletelyWaitUntilEnd(responseStream)) {
            return responseStream.toString();
        }
        throw new DeviceConnectionException(currentManagedDevice.getId());
    }

    private void setCommand(String command) {
        try {
            channelShell.open().verify();
            OutputStream pipedIn = channelShell.getInvertedIn();
            pipedIn.write((command + "\n").getBytes());
            pipedIn.flush();
        } catch (IOException e) {
            throw new DeviceConnectionException(currentManagedDevice.getId());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void open(NetworkDevice managedDevice) {
        currentManagedDevice = managedDevice;

        sshClient = SshClient.setUpDefaultClient();
        sshClient.start();

        clientSession = createClientSession();
    }

    public void close() {
        try {
            currentManagedDevice = null;
            channelShell.close();
            clientSession.close();
            sshClient.close();

            clientSession = null;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private boolean isCompletelyWaitUntilEnd(ByteArrayOutputStream responseStream) {
        long startTime = System.currentTimeMillis();
        while (!isEndOfMessage(responseStream.toString())) {
            channelShell.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 100);
            if (System.currentTimeMillis() - startTime > 10000) {
                return false;
            }
        }
        return true;
    }

    private boolean isEndOfMessage(String result) {
        if (result.length() == 0) return false;
        String trimmedResult = result.trim();
        if (END_DELIM_CHAR.contains(String.valueOf(trimmedResult.charAt(trimmedResult.length() - 1)))) {
            return trimmedResult.chars().filter(ch -> ch == ':').count() >= 2;
        }
        return false;
    }

    public NetworkDevice getCurrentManagedDevice() {
        return this.currentManagedDevice;
    }

    public boolean isOpen() {
        return !sshClient.isOpen();
    }
}
