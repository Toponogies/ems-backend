package vn.com.tma.emsbackend.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static vn.com.tma.emsbackend.common.constant.Constant.NEGATIVE_WORD;

public class Enum {
    @Getter
    @AllArgsConstructor
    public enum State {
        DISABLED("disabled","disable"),
        ENABLED("enabled", "enable");
        private final String value;
        private final String action;

        public static Enum.State parse(String value) {
            if (NEGATIVE_WORD.contains(value.toUpperCase())) {
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

    public enum SocketAction {
        RESYNC_DONE
    }

    @Getter
    public enum AlarmSeverity {
        MINOR("1"),
        MAJOR("2"),
        CRITICAL("3");
        public final String snmpId;
        public static AlarmSeverity parse(String value) {
            switch (value){
                case "MJ":
                    return AlarmSeverity.MAJOR;
                case "CR":
                    return AlarmSeverity.CRITICAL;
                default:
                    return AlarmSeverity.MINOR;
            }
        }

        AlarmSeverity(String snmpId){
            this.snmpId = snmpId;
        }
        public static AlarmSeverity fromSnmpId(String snmpId){
            if (CRITICAL.snmpId.equals(snmpId)) {
                return AlarmSeverity.CRITICAL;
            }else if(MAJOR.snmpId.equals(snmpId)){
                return AlarmSeverity.MAJOR;
            }
            return AlarmSeverity.MINOR;
        }
    }

}
