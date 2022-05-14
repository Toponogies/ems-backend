package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.common.SSHColumn;
import vn.com.tma.emsbackend.entity.Interface;
import vn.com.tma.emsbackend.parser.splitter.TableSplitter;

import java.util.ArrayList;
import java.util.List;

public class InterfaceCommandParser {
    public static List<Interface> interfaceShowParse(String executeResult){
        List<Interface> anInterfaces = new ArrayList<>();
        TableSplitter tableReader = new TableSplitter(executeResult).split();
        while(tableReader.next()){
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
}
