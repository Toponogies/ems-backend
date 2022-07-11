package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.constant.SSHColumn;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.Port;
import vn.com.tma.emsbackend.parser.splitter.ListSplitter;
import vn.com.tma.emsbackend.parser.splitter.TableSplitter;

import java.util.ArrayList;
import java.util.List;

public class InterfaceCommandParser {
    private InterfaceCommandParser() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Interface> interfaceShowParse(String executeResult) {
        List<Interface> anInterfaces = new ArrayList<>();
        TableSplitter tableReader = new TableSplitter(executeResult);
        while (tableReader.next()) {
            Interface anInterface = new Interface();
            anInterface.setDhcp(Enum.State.valueOf(tableReader.getValue(SSHColumn.Interface.DHCP).toUpperCase()));
            anInterface.setGateway(tableReader.getValue(SSHColumn.Interface.GATEWAY));
            anInterface.setIpAddress(tableReader.getValue(SSHColumn.Interface.IPADDRESS));
            anInterface.setName(tableReader.getValue(SSHColumn.Interface.NAME));
            anInterface.setNetmask(tableReader.getValue(SSHColumn.Interface.NETMASK));
            anInterface.setState(Enum.State.valueOf(tableReader.getValue(SSHColumn.Interface.STATE).toUpperCase()));
            anInterfaces.add(anInterface);
        }
        return anInterfaces;
    }

    public static Interface interfaceShowDetailParse(String executeResult) {
        Interface anInterface = new Interface();
        ListSplitter listSplitter = new ListSplitter(executeResult);
        anInterface.setName(listSplitter.get(SSHColumn.Interface.NAME));
        anInterface.setState(Enum.State.valueOf(listSplitter.get(SSHColumn.Interface.STATE_DETAIL).toUpperCase()));
        anInterface.setDhcp(Enum.State.parse(SSHColumn.Interface.DHCP));
        anInterface.setIpAddress(listSplitter.get(SSHColumn.Interface.IPADDRESS));
        anInterface.setNetmask(listSplitter.get(SSHColumn.Interface.NETMASK));
        anInterface.setGateway(listSplitter.get(SSHColumn.Interface.GATEWAY));

        Port port = new Port();
        port.setName(listSplitter.get(SSHColumn.Interface.PORT));
        anInterface.setPort(port);
        return anInterface;
    }
}
