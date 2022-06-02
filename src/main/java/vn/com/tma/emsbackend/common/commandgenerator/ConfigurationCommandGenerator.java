package vn.com.tma.emsbackend.common.commandgenerator;

public class ConfigurationCommandGenerator {
    private static final String COMMAND_PREFIX = "interface";
    private static final String EXPORT_ACTION = "export";

    private ConfigurationCommandGenerator() {
    }

    public static String export() {
        return COMMAND_PREFIX + " " + EXPORT_ACTION;
    }
}
