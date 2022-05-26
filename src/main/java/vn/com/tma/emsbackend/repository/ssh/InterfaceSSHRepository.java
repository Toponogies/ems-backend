package vn.com.tma.emsbackend.repository.ssh;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.SSHExecutor;
import vn.com.tma.emsbackend.common.commandgenerator.InterfaceCommandGenerator;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.model.exception.SSHExecuteFailException;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.parser.InterfaceCommandParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class InterfaceSSHRepository extends BaseSSHRepository {

    public List<Interface> getAll(long deviceId, List<Port> devicePorts) {
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(deviceId);
        String result = sshExecutor.execute(InterfaceCommandGenerator.showAll());

        List<String> interfacesName = InterfaceCommandParser.interfaceShowParse(result).stream().map(Interface::getName).collect(Collectors.toList());
        List<Interface> interfaces = new ArrayList<>();

        for (String interfaceName : interfacesName) {
            String detailResult = sshExecutor.execute(InterfaceCommandGenerator.showDetail(interfaceName));
            Interface anInterface = InterfaceCommandParser.interfaceShowDetailParse(detailResult);
            for (Port port : devicePorts) {
                if (port.getName().equals(anInterface.getPort().getName())) {
                    anInterface.setPort(port);
                    break;
                }
            }
            if (anInterface.getPort().getId() == null) anInterface.setPort(null);
            interfaces.add(anInterface);
        }
        return interfaces;
    }

    public void add(Interface anInterface) {
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(anInterface.getNetworkDevice().getId());
        String command = InterfaceCommandGenerator.add(anInterface);
        String result = sshExecutor.execute(command);
        String errorMessage = getErrorMessage(command, result);
        if(errorMessage.length() > 0) throw new SSHExecuteFailException(errorMessage);
    }


}
