package vn.com.tma.emsbackend.common.constant;

public class SSHColumn {
    private SSHColumn(){}
    public static final class NetworkDevice {
        private NetworkDevice(){}
        public static final String MODEL = "Product name";
        public static final String MAC_ADDRESS = "MAC base address";
        public static final String SERIAL = "Serial number";
        public static final String FIRMWARE = "Firmware version";
    }

    public static final class Interface {
        private Interface(){}
        public static final String DHCP = "DHCP";
        public static final String GATEWAY = "Gateway";
        public static final String IPADDRESS = "IP address";
        public static final String NAME = "Interface name";
        public static final String NETMASK = "Netmask";
        public static final String STATE = "State";
    }

    public static final class Port {
        private Port(){}
        public static final String CONNECTOR = "Connector";
        public static final String NAME = "Port name";
        public static final String STATE = "State";
        public static final String MAC_ADDRESS = "MAC address";
    }


}