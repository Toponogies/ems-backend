package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.common.SSHColumn;
import vn.com.tma.emsbackend.entity.Port;

import java.util.ArrayList;
import java.util.List;

public class PortCommandParser {
    public static List<Port>  portShowConfigCommand(String executeResult){
        List<Port> ports = new ArrayList<>();
        TableReader tableReader = new TableReader(executeResult).split();
        while(tableReader.next()){
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
