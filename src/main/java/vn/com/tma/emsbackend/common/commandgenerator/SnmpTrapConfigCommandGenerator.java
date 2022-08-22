package vn.com.tma.emsbackend.common.commandgenerator;

import org.apache.commons.lang3.StringUtils;
import vn.com.tma.emsbackend.common.enums.Enum;

public class SnmpTrapConfigCommandGenerator {
    private static final String COMMAND_PREFIX = "snmp-trap";
    private static final String SNMP_V2C = "v2c";

    private static final int DEFAULT_HOST_ID = 1;
    private static final Enum.State DEFAULT_SNMP_TRAP_STATE = Enum.State.ENABLED;
    private static final String DEFAULT_HOST_COMMUNICATION_STRING = "emsazerty!";
    private static final int DEFAULT_HOST_PORT = 3001;
    private static final String DEFAULT_EMS_IP = "192.168.1.2";

    public static String editSnmpTrapV2c() {
        return String.format(String.join(" ", COMMAND_PREFIX, "edit", SNMP_V2C,
                        String.valueOf(DEFAULT_HOST_ID),
                        "host-state", "%s",
                        "host-community", "%s",
                        "host-name", "%s",
                        "host-port", " %s"),
                DEFAULT_SNMP_TRAP_STATE.getAction(),
                DEFAULT_HOST_COMMUNICATION_STRING,
                DEFAULT_EMS_IP,
                DEFAULT_HOST_PORT);
    }

}
