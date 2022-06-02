package vn.com.tma.emsbackend.common.commandgenerator;

public class NetworkDeviceCommandGenerator {
    private NetworkDeviceCommandGenerator() {
    }

    private static final String COMMAND_PREFIX = "board";

    public static String getDetail() {
        return COMMAND_PREFIX + " show info";
    }
}
