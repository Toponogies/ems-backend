package vn.com.tma.emsbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Enum {
    @Getter
    @AllArgsConstructor
    public enum State {
        DISABLED("disabled"),
        ENABLED("enabled");

        private final String value;

        public static Enum.State getState(String value){
            if(Constant.NEGATIVE_WORK.contains(value)){
                return DISABLED;
            }
            return ENABLED;
        }
    }

    @Getter
    @AllArgsConstructor
    public enum InterfaceDHCP {
        DISABLED("disabled"),
        ENABLED("enabled");
        private final String value;
        public static Enum.InterfaceDHCP parse(String value){
            if(Constant.NEGATIVE_WORK.contains(value)){
                return DISABLED;
            }
            return ENABLED;
        }
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
