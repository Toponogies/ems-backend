package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.common.SSHColumn;
import vn.com.tma.emsbackend.entity.NDInterface;
import vn.com.tma.emsbackend.entity.Port;
import vn.com.tma.emsbackend.parser.splitter.ListSplitter;
import vn.com.tma.emsbackend.parser.splitter.TableSplitter;

import java.util.ArrayList;
import java.util.List;

public class InterfaceCommandParser {
    public static List<String> interfaceShowParse(String executeResult){
        List<String> interfacesName = new ArrayList<>();
        TableSplitter tableReader = new TableSplitter(executeResult).split();
        while(tableReader.next()){
            interfacesName.add(tableReader.getValue(SSHColumn.NDInterface.NAME));
        }
        return interfacesName;
    }

    public static NDInterface interfaceShowDetailParse(String executeResult){
        NDInterface ndInterface = new NDInterface();
        ListSplitter listSplitter = new ListSplitter(executeResult).split();
        ndInterface.setName(listSplitter.get(SSHColumn.NDInterface.NAME));
        ndInterface.setState(Enum.State.valueOf(listSplitter.get(SSHColumn.NDInterface.STATE).toUpperCase()));
        ndInterface.setDhcp(Enum.InterfaceDHCP.parse(SSHColumn.NDInterface.DHCP));
        ndInterface.setIpAddress(listSplitter.get(SSHColumn.NDInterface.IPADDRESS));
        ndInterface.setNetmask(listSplitter.get(SSHColumn.NDInterface.NETMASK));
        ndInterface.setGateway(listSplitter.get(SSHColumn.NDInterface.GATEWAY));

        Port port = new Port();
        port.setName(listSplitter.get(SSHColumn.NDInterface.PORT));
        ndInterface.setPort(port);
        return ndInterface;
    }
}
