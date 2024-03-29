package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.commandgenerator.NetworkDeviceCommandGenerator;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.parser.ssh.NetworkDeviceCommandParser;
import vn.com.tma.emsbackend.service.ssh.utils.SSHExecutor;
@Repository
@AllArgsConstructor
public class NetworkDeviceSSHRepository extends BaseSSHRepository {
    public NetworkDevice getDetail(long id) {
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(id);
        String result = sshExecutor.execute(NetworkDeviceCommandGenerator.getDetail());
        return NetworkDeviceCommandParser.boardShowInfoCommandParse(result);
    }

    public String sendCommand(Long id, String command) {
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(id);
        String result = sshExecutor.execute(command);
        return getMainResult(command, result);
    }
}
