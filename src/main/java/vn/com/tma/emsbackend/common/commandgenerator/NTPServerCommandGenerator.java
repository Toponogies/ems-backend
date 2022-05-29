package vn.com.tma.emsbackend.common.commandgenerator;

public class NTPServerCommandGenerator {
    private static final String COMMAND_PREFIX = "ntp";
    private static final String ADD_ACTION = "add";
    private static final String ENABLE_ACTION = "enable";
    private static final String DISABLE_ACTION = "disable";
    private static final String DELETE_ACTION = "delete";


    public static String getAll() {
        return COMMAND_PREFIX + " show";
    }

    public static String add(String serverAddress) {
        return COMMAND_PREFIX + " " + ADD_ACTION + " " + serverAddress;
    }

    public static String enable(String serverAddress){
        return COMMAND_PREFIX +  " " + ENABLE_ACTION + " " + serverAddress;
    }

    public static String disable(String serverAddress){
        return COMMAND_PREFIX +  " " + DISABLE_ACTION + " " + serverAddress;
    }

    public static String delete(String serverAddress){
        return COMMAND_PREFIX + " " + DELETE_ACTION + " " + serverAddress;
    }
}
