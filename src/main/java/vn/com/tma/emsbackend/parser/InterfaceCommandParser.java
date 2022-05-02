package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.Enum;
import vn.com.tma.emsbackend.common.SSHColumn;
import vn.com.tma.emsbackend.entity.NDInterface;
import vn.com.tma.emsbackend.parser.splitter.TableSplitter;

import java.util.ArrayList;
import java.util.List;

public class InterfaceCommandParser {
    public static List<NDInterface> interfaceShowParse(String executeResult){
        List<NDInterface> ndInterfaces = new ArrayList<>();
        TableSplitter tableReader = new TableSplitter(executeResult).split();
        while(tableReader.next()){
            NDInterface ndInterface = new NDInterface();
            ndInterface.setDhcp(Enum.InterfaceDHCP.valueOf(tableReader.getValue(SSHColumn.NDInterface.DHCP).toUpperCase()));
            ndInterface.setGateway(tableReader.getValue(SSHColumn.NDInterface.GATEWAY));
            ndInterface.setIpAddress(tableReader.getValue(SSHColumn.NDInterface.IPADDRESS));
            ndInterface.setName(tableReader.getValue(SSHColumn.NDInterface.NAME));
            ndInterface.setNetmask(tableReader.getValue(SSHColumn.NDInterface.NETMASK));
            ndInterface.setState(Enum.State.valueOf(tableReader.getValue(SSHColumn.NDInterface.STATE).toUpperCase()));
            ndInterfaces.add(ndInterface);
        }
        return ndInterfaces;
    }
}
