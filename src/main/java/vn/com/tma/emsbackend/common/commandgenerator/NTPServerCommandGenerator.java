package vn.com.tma.emsbackend.common.commandgenerator;

public class NTPServerCommandGenerator {
    private static final String COMMAND_PREFIX = "ntp";

    public static String getAll(){
        return COMMAND_PREFIX + " show";
    }

}
