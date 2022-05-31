package vn.com.tma.emsbackend.common.constant;

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

    public static final List<String> NEGATIVE_WORD = List.of("NO", "DISABLE", "DISABLED");

    public static final List<String> ERROR_WORD = List.of("error", "invalid");

    //resync time
    public static final long INTERVAL_ADD_ALL_DEVICES_TO_RESYNC_QUEUE = 15 * 60 * 1000L;
    public static final long INTERVAL_CHECK_RESYNC_QUEUE= 100;
}
