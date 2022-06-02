package vn.com.tma.emsbackend.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static vn.com.tma.emsbackend.common.constant.Constant.NEGATIVE_WORD;

public class Enum {
    @Getter
    @AllArgsConstructor
    public enum State {
        DISABLED("disabled"),
        ENABLED("enabled");
        private final String value;

        public static Enum.State parse(String value) {
            if (NEGATIVE_WORD.contains(value)) {
                return DISABLED;
            }
            return ENABLED;
        }
    }

    public enum NetworkDeviceState {
        IN_SERVICE,
        OUT_OF_SERVICE,
    }

    public enum NetworkDeviceType {
        VCX,
        GT,
        GX,
        LT,
        LX,
    }

    public enum ResyncStatus {
        ONGOING,
        DONE,
    }

}
