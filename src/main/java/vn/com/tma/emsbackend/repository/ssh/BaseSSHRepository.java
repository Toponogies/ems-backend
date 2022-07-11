package vn.com.tma.emsbackend.repository.ssh;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.tma.emsbackend.common.constant.Constant;
import vn.com.tma.emsbackend.service.ssh.utils.DeviceConnectionManager;

public abstract class BaseSSHRepository {
    @Autowired
    DeviceConnectionManager deviceConnectionManager;

    protected String getErrorMessage(String command, String result) {
        result = result.replaceAll(command, "");
        String[] lines = result.split("\n");
        for (String line : lines) {
            if (Constant.ERROR_WORD.stream().anyMatch(errorWord -> line.toLowerCase().contains(errorWord))) {
                return line.trim();
            }
        }
        return "";
    }

    protected String getMainResult(String command, String result) {
        String[] lines = result.split("\n");
        String mainResult = "";
        int startResultIndex = 0;
        for (int index = 0; index < lines.length; index++) {
            if (lines[index].contains(command)) {
                startResultIndex = index + 1;
                break;
            }
        }
        for (int index = startResultIndex; index < lines.length - 1; index++) {
            if (!lines[index].isBlank()) {
                mainResult = mainResult.concat(lines[index] + "\n");
            }
        }
        return mainResult;
    }
}
