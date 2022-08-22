package vn.com.tma.emsbackend.common.commandgenerator;

public class SessionCommandGenerator {
    private static final String COMMAND_PREFIX = "session";

    public static String writelock(){
        return COMMAND_PREFIX + " writelock";
    }

}
