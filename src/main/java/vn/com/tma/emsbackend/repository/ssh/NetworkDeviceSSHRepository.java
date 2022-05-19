package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.common.commandgenerator.NetworkDeviceCommandGenerator;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.parser.NetworkDeviceCommandParser;

@Repository
@AllArgsConstructor
public class NetworkDeviceSSHRepository extends  BaseSSHRepository{
    public NetworkDevice getDetail(long id){
        SSHExecutor connection = deviceConnectionManager.getConnection(id);
        String result = connection.execute(NetworkDeviceCommandGenerator.getDetail());
        return NetworkDeviceCommandParser.boardShowInfoCommandParse(result);
    }
}
