package vn.com.tma.emsbackend.common.commandgenerator;

import vn.com.tma.emsbackend.entity.NDInterface;
import vn.com.tma.emsbackend.entity.Port;

public class InterfaceCommandGenerator {
    private static final String COMMAND_PREFIX = "interface";
    private static final String ADD_ACTION = "add";
    private static final String NAME_PARAMS = "name";
    private static final String DHCP_PARAMS = "dhcp";
    private static final String ADDRESS_PARAMS = "address";
    private static final String NETMASK_PARAMS = "netmask";
    private static final String GATEWAY_PARAMS = "gateway";
    private static final String PORT_PARAMS = "port";

    public static String add(NDInterface ndInterface, Port port) {
        return COMMAND_PREFIX + " " +
                ADD_ACTION + " " +
                ndInterface.getName() + " " +
                DHCP_PARAMS + " " + ndInterface.getDhcp().getValue() + " " +
                (ndInterface.getIpAddress() == null || ndInterface.getIpAddress().length() == 0 ? "" : ADDRESS_PARAMS + " " + ndInterface.getIpAddress() + " ") +
                (ndInterface.getNetmask() == null || ndInterface.getNetmask().length() == 0 ? "" : NETMASK_PARAMS + " " + ndInterface.getNetmask() + " ") +
                (ndInterface.getGateway() == null || ndInterface.getGateway().length() == 0 ? "" : GATEWAY_PARAMS + " " + ndInterface.getGateway() + " ") +
                PORT_PARAMS + " " + port.getName();
    }

}
