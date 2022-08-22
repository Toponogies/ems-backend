package vn.com.tma.emsbackend.repository.ssh;

import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.commandgenerator.PortCommandGenerator;
import vn.com.tma.emsbackend.common.commandgenerator.SessionCommandGenerator;
import vn.com.tma.emsbackend.common.commandgenerator.SnmpTrapConfigCommandGenerator;
import vn.com.tma.emsbackend.model.exception.SSHExecuteException;
import vn.com.tma.emsbackend.service.ssh.utils.SSHExecutor;

@Repository
public class SnmpConfigSSHRepository extends BaseSSHRepository {
    public void setSnmpTrapConfig(Long deviceId) {
        SSHExecutor connection = deviceConnectionManager.getConnection(deviceId);
        connection.execute(SessionCommandGenerator.writelock());
        String command = SnmpTrapConfigCommandGenerator.editSnmpTrapV2c();
        String result = connection.execute(command);
        String errorMessage = getErrorMessage(command, result);
        if (errorMessage.length() > 0) throw new SSHExecuteException(errorMessage);
    }
}
