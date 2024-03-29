package vn.com.tma.emsbackend.parser.ssh;

import vn.com.tma.emsbackend.common.constant.SSHColumn;
import vn.com.tma.emsbackend.common.enums.Enum.NetworkDeviceType;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.parser.ssh.splitter.ListSplitter;

public class NetworkDeviceCommandParser {
    private NetworkDeviceCommandParser() {
    }

    public static NetworkDevice boardShowInfoCommandParse(String executeResult) {
        ListSplitter listReader = new ListSplitter(executeResult);
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setFirmware(listReader.get(SSHColumn.NetworkDevice.FIRMWARE));
        networkDevice.setMacAddress(listReader.get(SSHColumn.NetworkDevice.MAC_ADDRESS));
        networkDevice.setModel(listReader.get(SSHColumn.NetworkDevice.MODEL));
        networkDevice.setSerial(listReader.get(SSHColumn.NetworkDevice.SERIAL));

        networkDevice.setDeviceType(NetworkDeviceType.VCX);
        for (NetworkDeviceType networkDeviceType : NetworkDeviceType.values()) {
            if (networkDevice.getModel().contains(networkDeviceType.name())) {
                networkDevice.setDeviceType(networkDeviceType);
            }
        }
        return networkDevice;
    }
}
