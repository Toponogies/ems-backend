package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.common.commandgenerator.NTPServerCommandGenerator;
import vn.com.tma.emsbackend.model.entity.NTPServer;
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
}
