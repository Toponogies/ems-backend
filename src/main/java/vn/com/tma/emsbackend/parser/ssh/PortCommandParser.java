package vn.com.tma.emsbackend.parser.ssh;

import vn.com.tma.emsbackend.common.constant.SSHColumn;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.parser.ssh.splitter.TableSplitter;

import java.util.ArrayList;
import java.util.List;

public class PortCommandParser {
    private PortCommandParser() {
    }

    public static List<Port> portShowConfigCommand(String executeResult) {
        List<Port> ports = new ArrayList<>();
        TableSplitter tableReader = new TableSplitter(executeResult);
        while (tableReader.next()) {
            Port port = new Port();
            port.setConnector(tableReader.getValue(SSHColumn.Port.CONNECTOR));
            port.setState(Enum.State.valueOf(tableReader.getValue(SSHColumn.Port.STATE).toUpperCase()));
            port.setName(tableReader.getValue(SSHColumn.Port.NAME));
            port.setMacAddress(tableReader.getValue(SSHColumn.Port.MAC_ADDRESS));
            ports.add(port);
        }
        return ports;
    }
}
