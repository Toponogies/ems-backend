package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.ChannelShellCloseException;
import vn.com.tma.emsbackend.model.exception.ChannelShellOpenException;
import vn.com.tma.emsbackend.model.exception.DeviceConnectionException;
import vn.com.tma.emsbackend.model.exception.SSHExecuteException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Slf4j
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
        ClientSession newClientSession;
        try {
            newClientSession = sshClient.connect(currentManagedDevice.getCredential().getUsername(), currentManagedDevice.getIpAddress(), currentManagedDevice.getSshPort()).verify(5000L).getSession();
            newClientSession.addPasswordIdentity(currentManagedDevice.getCredential().getPassword());
            newClientSession.auth().verify(5000L);
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
        clientSession.close(true);
        channelShell.close(true);
        sshClient.close(true);
        sshClient.stop();
    }

    private boolean isCompletelyWaitUntilEnd(ByteArrayOutputStream responseStream) {
        long startTime = System.currentTimeMillis();
        while (!isEndOfMessage(responseStream.toString())) {
            channelShell.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 100);
            if (System.currentTimeMillis() - startTime > 2000) {
                close();
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
        return sshClient.isOpen();
    }

    public boolean isClosed() {
        return clientSession.isClosed() || sshClient.isClosed();
    }
}
