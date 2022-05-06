package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.entity.Port;
import vn.com.tma.emsbackend.parser.PortCommandParser;

import java.util.List;

@Repository
@AllArgsConstructor
public class PortSSHRepository extends  BaseSSHRepository {
    public List<Port> getAll(long deviceId){
        SSHExecutor connection = deviceConnectionManager.getConnection(deviceId);
        String result = connection.execute("port show configuration");
        return PortCommandParser.portShowConfigCommand(result);
    }
}
