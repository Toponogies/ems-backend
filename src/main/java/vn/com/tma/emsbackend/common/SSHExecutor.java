package vn.com.tma.emsbackend.common;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelShell;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.ApplicationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
            throw new ApplicationException("ManagedDevice is null, please set managed device before execute");
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
            throw new ApplicationException(e);
        }
    }

    private void closeChannelShell() {
        try {
            channelShell.close();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    private ClientSession createClientSession() {
        ClientSession newClientSession = null;
        try {
            var a = currentManagedDevice.getCredential().getUsername();
            newClientSession = sshClient.connect(currentManagedDevice.getCredential().getUsername(), currentManagedDevice.getIpAddress(), currentManagedDevice.getSshPort()).verify().getSession();
            newClientSession.addPasswordIdentity(currentManagedDevice.getCredential().getPassword());
            newClientSession.auth().verify();
            return newClientSession;
        } catch (IOException e) {
            throw new ApplicationException(e);
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
            throw new ApplicationException(errorStream.toString());
        }
        waitUntilEnd(responseStream);
        return responseStream.toString();
    }

    private void setCommand(String command) {
        try {
            channelShell.open().verify(10000);
            OutputStream pipedIn = channelShell.getInvertedIn();
            pipedIn.write((command + "\n").getBytes());
            pipedIn.flush();
        } catch (IOException e) {
            throw new ApplicationException(e);
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

    private void waitUntilEnd(ByteArrayOutputStream responseStream) {
        while (!isEndOfMessage(responseStream.toString())) {
            channelShell.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 100);
        }
    }

    private boolean isEndOfMessage(String result) {
        if (result.length() == 0) return false;
        String trimmedResult = result.trim();
        if(END_DELIM_CHAR.contains(String.valueOf(trimmedResult.charAt(trimmedResult.length() - 1)))){
            return trimmedResult.chars().filter(ch -> ch == ':').count() >= 2;
        }
        return false;
    }
}
