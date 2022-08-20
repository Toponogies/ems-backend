package vn.com.tma.emsbackend.common.commandgenerator;

public class AlarmCommandGenerator {
    private static final String COMMAND_PREFIX = "alarm";


    public static String getAllAlarmWithDetail(){
        return String.join(" ", COMMAND_PREFIX, "show", "status", "more");
    }
}
