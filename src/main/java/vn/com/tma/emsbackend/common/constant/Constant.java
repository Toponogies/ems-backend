package vn.com.tma.emsbackend.common.constant;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    private Constant() {
        throw new IllegalStateException("Utility class");
    }

    public static final int MAX_PORT_NUMBER = 65535;
    public static final String CREDENTIAL_TABLE = "credentials";
    public static final String NETWORK_DEVICE_TABLE = "network_devices";
    public static final String INTERFACE_TABLE = "interfaces";
    public static final String PORT_TABLE = "ports";
    public static final String NTP_SERVER_TABLE = "ntp_server";

    public static final List<String> NEGATIVE_WORK =  new ArrayList<>(List.of(new String[]{"NO", "DISABLE", "DISABLED"}));
}
