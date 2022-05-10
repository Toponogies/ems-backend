package vn.com.tma.emsbackend.repository.ssh;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.common.commandgenerator.InterfaceCommandGenerator;
import vn.com.tma.emsbackend.entity.NDInterface;
import vn.com.tma.emsbackend.entity.Port;
import vn.com.tma.emsbackend.parser.InterfaceCommandParser;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class InterfaceSSHRepository extends BaseSSHRepository {

    public List<NDInterface> getAll(long deviceId, List<Port> devicePorts) {
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(deviceId);
        String result = sshExecutor.execute(InterfaceCommandGenerator.showAll());

        List<String> interfacesName = InterfaceCommandParser.interfaceShowParse(result);
        List<NDInterface> ndInterfaces = new ArrayList<>();

        for (String interfaceName : interfacesName) {
            String detailResult = sshExecutor.execute(InterfaceCommandGenerator.showDetail(interfaceName));
            NDInterface ndInterface = InterfaceCommandParser.interfaceShowDetailParse(detailResult);
            for (Port port : devicePorts) {
                if (port.getName().equals(ndInterface.getPort().getName())) {
                    ndInterface.setPort(port);
                    break;
                }
            }
            ndInterfaces.add(ndInterface);
        }
        return ndInterfaces;
    }

    public void add(long deviceId, NDInterface ndInterface, Port port) {
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(deviceId);
        String command = InterfaceCommandGenerator.add(ndInterface, port);
        String result = sshExecutor.execute(command);
    }
}
