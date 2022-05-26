package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.common.commandgenerator.NTPServerCommandGenerator;
import vn.com.tma.emsbackend.model.entity.NTPServer;
import vn.com.tma.emsbackend.model.exception.SSHExecuteFailException;
import vn.com.tma.emsbackend.parser.NTPCommandParser;

import java.util.List;

@Repository
@AllArgsConstructor
public class NTPServiceSSHRepository extends BaseSSHRepository {
    public List<NTPServer> getAll(long deviceId) {
        SSHExecutor connection = deviceConnectionManager.getConnection(deviceId);
        String result = connection.execute(NTPServerCommandGenerator.getAll());
        return NTPCommandParser.ntpShowCommand(result);
    }

    public void add(NTPServer ntpServer) {
        SSHExecutor connection = deviceConnectionManager.getConnection(ntpServer.getNetworkDevice().getId());
        String command = NTPServerCommandGenerator.add(ntpServer.getServerAddress());
        String result = connection.execute(command);
        String errorMessage = getErrorMessage(command, result);
        if (errorMessage.length() > 0) throw new SSHExecuteFailException(errorMessage);
    }

    public void enable(NTPServer ntpServer){
        SSHExecutor connection = deviceConnectionManager.getConnection(ntpServer.getNetworkDevice().getId());
        String command = NTPServerCommandGenerator.enable(ntpServer.getServerAddress());
        String result = connection.execute(command);
        String errorMessage = getErrorMessage(command, result);
        if (errorMessage.length() > 0) throw new SSHExecuteFailException(errorMessage);
    }


    public void disable(NTPServer ntpServer){
        SSHExecutor connection = deviceConnectionManager.getConnection(ntpServer.getNetworkDevice().getId());
        String command = NTPServerCommandGenerator.disable(ntpServer.getServerAddress());
        String result = connection.execute(command);
        String errorMessage = getErrorMessage(command, result);
        if (errorMessage.length() > 0) throw new SSHExecuteFailException(errorMessage);
    }


    public void delete(NTPServer ntpServer){
        SSHExecutor connection = deviceConnectionManager.getConnection(ntpServer.getNetworkDevice().getId());
        String command = NTPServerCommandGenerator.delete(ntpServer.getServerAddress());
        String result = connection.execute(command);
        String errorMessage = getErrorMessage(command, result);
        if (errorMessage.length() > 0) throw new SSHExecuteFailException(errorMessage);
    }
}
