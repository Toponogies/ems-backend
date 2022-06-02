package vn.com.tma.emsbackend.common.commandgenerator;

public class PortCommandGenerator {
    private PortCommandGenerator() {
    }

    private static final String COMMAND_PREFIX = "port";

    public static String getAll() {
        return COMMAND_PREFIX + " show configuration";
    }
}
