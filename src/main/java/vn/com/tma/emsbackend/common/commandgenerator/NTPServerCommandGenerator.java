package vn.com.tma.emsbackend.common.commandgenerator;

public class NTPServerCommandGenerator {
    private static final String COMMAND_PREFIX = "interface";

    public static String getAll(){
        return COMMAND_PREFIX + " show";
    }

}
