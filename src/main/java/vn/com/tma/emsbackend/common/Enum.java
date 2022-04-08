package vn.com.tma.emsbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Enum {
    @Getter
    @AllArgsConstructor
    public enum State {
        DISABLE("disabled"),
        ENABLE("enabled");

        private final String value;
    }

    @Getter
    @AllArgsConstructor
    public enum InterfaceDHCP {
        DISABLE("disabled"),
        ENABLE("enabled");
        private final String value;
    }

    public enum ManagedDeviceState {
        IN_SERVICE,
        OUT_OF_SERVICE,
        IN_PROCESS
    }
}
