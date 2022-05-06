package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.DeviceConnectionManager;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.entity.NetworkDevice;
import vn.com.tma.emsbackend.parser.NetworkDeviceCommandParser;

@Repository
@AllArgsConstructor
public class NetworkDeviceSSHRepository extends  BaseSSHRepository{
    public NetworkDevice getDetail(long id){
        SSHExecutor connection = deviceConnectionManager.getConnection(id);
        String result = connection.execute("board show info");
        return NetworkDeviceCommandParser.boardShowInfoCommandParse(result);
    }
}
