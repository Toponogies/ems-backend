package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.SSHColumn;
import vn.com.tma.emsbackend.entity.NetworkDevice;
import vn.com.tma.emsbackend.parser.splitter.ListSplitter;

public class NetworkDeviceCommandParser {
    private NetworkDeviceCommandParser() {
    }

    public static NetworkDevice boardShowInfoCommandParse(String executeResult) {
        ListSplitter listReader = new ListSplitter(executeResult).split();
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setFirmware(listReader.get(SSHColumn.NetworkDevice.FIRMWARE));
        networkDevice.setMacAddress(listReader.get(SSHColumn.NetworkDevice.MAC_ADDRESS));
        networkDevice.setModel(listReader.get(SSHColumn.NetworkDevice.MODEL));
        networkDevice.setSerial(listReader.get(SSHColumn.NetworkDevice.SERIAL));
        return networkDevice;
    }
}
