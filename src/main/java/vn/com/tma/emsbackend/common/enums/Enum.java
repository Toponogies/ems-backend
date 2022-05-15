package vn.com.tma.emsbackend.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Enum {
    @Getter
    @AllArgsConstructor
    public enum State {
        DISABLED("disabled"),
        ENABLED("enabled");
        private final String value;
    }

    public enum NetworkDeviceState {
        IN_SERVICE,
        OUT_OF_SERVICE,
        IN_PROCESS
    }

    public enum NetworkDeviceType {
        VCX,
        GT,
        GX,
        LT,
        LX,
    }
}
