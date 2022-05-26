package vn.com.tma.emsbackend.repository.ssh;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.tma.emsbackend.common.DeviceConnectionManager;
import vn.com.tma.emsbackend.common.constant.Constant;

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
}
