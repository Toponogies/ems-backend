package vn.com.tma.emsbackend.common.commandgenerator;

import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.Port;

public class InterfaceCommandGenerator {
    private static final String COMMAND_PREFIX = "interface";
    private static final String ADD_ACTION = "add";

    private static final String EDIT_ACTION ="edit";
    private static final String STATE_PARAMS = "state";

    private static final String NAME_PARAMS = "name";
    private static final String DHCP_PARAMS = "dhcp";
    private static final String ADDRESS_PARAMS = "address";
    private static final String NETMASK_PARAMS = "netmask";
    private static final String GATEWAY_PARAMS = "gateway";
    private static final String PORT_PARAMS = "port";

    public static String add(Interface anInterface) {
        return COMMAND_PREFIX + " " +
                ADD_ACTION + " " +
                anInterface.getName() + " " +
                STATE_PARAMS + " " + anInterface.getState().getValue() + " " +
                DHCP_PARAMS + " " + anInterface.getDhcp().getValue() + " " +
                (anInterface.getIpAddress() == null || anInterface.getIpAddress().length() == 0 ? "" : ADDRESS_PARAMS + " " + anInterface.getIpAddress() + " ") +
                (anInterface.getNetmask() == null || anInterface.getNetmask().length() == 0 ? "" : NETMASK_PARAMS + " " + anInterface.getNetmask() + " ") +
                (anInterface.getGateway() == null || anInterface.getGateway().length() == 0 ? "" : GATEWAY_PARAMS + " " + anInterface.getGateway() + " ") +
                PORT_PARAMS + " " + anInterface.getPort().getName();
    }

    public static String edit(String oldInterfaceName, Interface anInterface, Port port){
        return COMMAND_PREFIX + " " +
                EDIT_ACTION + " " +
                oldInterfaceName + " " +
                NAME_PARAMS + " " +
                anInterface.getName() + " " +
                STATE_PARAMS + " " + anInterface.getState().getValue() + " " +
                DHCP_PARAMS + " " + anInterface.getDhcp().getValue() + " " +
                (anInterface.getIpAddress() == null || anInterface.getIpAddress().length() == 0 ? "" : ADDRESS_PARAMS + " " + anInterface.getIpAddress() + " ") +
                (anInterface.getNetmask() == null || anInterface.getNetmask().length() == 0 ? "" : NETMASK_PARAMS + " " + anInterface.getNetmask() + " ") +
                (anInterface.getGateway() == null || anInterface.getGateway().length() == 0 ? "" : GATEWAY_PARAMS + " " + anInterface.getGateway() + " ") +
                PORT_PARAMS + " " + port.getName();
    }

    public static String showAll(){
        return COMMAND_PREFIX + " show";
    }
    public static String showDetail(String interfaceName){
        return COMMAND_PREFIX + " show " + interfaceName;
    }

}
