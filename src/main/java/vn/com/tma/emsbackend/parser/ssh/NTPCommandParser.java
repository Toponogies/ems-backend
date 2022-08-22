package vn.com.tma.emsbackend.parser.ssh;

import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.NTPServer;

import java.util.ArrayList;
import java.util.List;

public class NTPCommandParser {
    private static final String ENABLED_SERVER_LIST_STRING = "Enabled server list:";
    private static final String DISABLED_SERVER_LIST_STRING = "Disabled server list:";

    private static final String SPLIT_REGEX = "[:(]";

    private NTPCommandParser() {
    }

    public static List<NTPServer> ntpShowCommand(String executeResult) {
        List<NTPServer> ntpServers = new ArrayList<>();
        String[] lines = executeResult.split("\n");
        int index = 0;
        while (true) {
            if (lines[index].contains(ENABLED_SERVER_LIST_STRING)) {
                ntpServers.addAll(parseListNTPServers(lines, index, Enum.State.ENABLED));
                index += ntpServers.size();
            } else if (lines[index].contains(DISABLED_SERVER_LIST_STRING)) {
                ntpServers.addAll(parseListNTPServers(lines, index, Enum.State.DISABLED));
                break;
            }
            index++;
        }
        return ntpServers;
    }

    private static NTPServer parseNTPServer(String line, Enum.State state) {
        NTPServer ntpServer = new NTPServer();
        String[] tuple = line.split(SPLIT_REGEX);
        String address;
        if (state == Enum.State.ENABLED) {
            address = tuple[tuple.length - 2].trim();
        } else {
            address = tuple[tuple.length - 1].trim();
        }
        ntpServer.setServerAddress(address);
        ntpServer.setState(state);
        return ntpServer;
    }

    private static List<NTPServer> parseListNTPServers(String[] lines, int startIndex, Enum.State state) {
        int index = startIndex;
        if (lines[startIndex].trim().split(":").length == 1) return new ArrayList<>();
        List<NTPServer> ntpServers = new ArrayList<>();
        while (lines[index].trim().length() != 0) {
            ntpServers.add(parseNTPServer(lines[index], state));
            index++;
        }
        return ntpServers;
    }
}
