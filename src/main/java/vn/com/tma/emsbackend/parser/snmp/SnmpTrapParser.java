package vn.com.tma.emsbackend.parser.snmp;

import org.snmp4j.PDU;
import org.snmp4j.smi.Variable;
import vn.com.tma.emsbackend.common.constant.MibDetails;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Alarm;

import java.sql.Timestamp;

public class SnmpTrapParser {
    private static final String COLON = ":";
    private static final String DASH = "-";
    private static final String PAD = "%02d";
    private static final String SPACE = " ";

    public static Alarm parseToAlarm(PDU pdu) {
        Alarm alarm = new Alarm();
        alarm.setDescription(parseDescription(pdu));
        alarm.setAlarmNumber(parseAlarmNumber(pdu));
        alarm.setCondition(parseConditionType(pdu));
        alarm.setAlarmSeverity(parseSeverity(pdu));
        alarm.setDate(parseLastStatusChange(pdu));
        return alarm;
    }

    public static String parseIp(PDU pdu) {
        Variable variable = pdu.getVariable(MibDetails.IP);
        return parsePossibleEmptyValue(variable);
    }

    private static Enum.AlarmSeverity parseSeverity(PDU pdu) {
        Variable variable = pdu.getVariable(MibDetails.SEVERITY);
        return Enum.AlarmSeverity.fromSnmpId(parsePossibleEmptyValue(pdu.getVariable(MibDetails.SEVERITY)));
    }

    private static String parseConditionType(PDU pdu) {
        Variable variable = pdu.getVariable(MibDetails.CONDITION_TYPE);
        return parsePossibleEmptyValue(variable);
    }

    private static String parseAlarmNumber(PDU pdu) {
        Variable variable = pdu.getVariable(MibDetails.ALARM_NUMBER);
        return parsePossibleEmptyValue(variable);
    }

    private static String parseDescription(PDU pdu) {
        Variable variable = pdu.getVariable(MibDetails.DESCRIPTION);
        return parsePossibleEmptyValue(variable);
    }

    private static String parseLastStatusChange(PDU pdu) {
        Variable value = pdu.getVariable(MibDetails.LAST_TIME_CHANGE);
        String[] data = value.toString().split(COLON);
        return Integer.parseInt(data[0] + data[1], 16) + DASH + String.format(PAD, Integer.parseInt(data[2], 16)) + DASH + String.format(PAD, Integer.parseInt(data[3], 16)) + SPACE + String.format(PAD, Integer.parseInt(data[4], 16)) + COLON + String.format(PAD, Integer.parseInt(data[5], 16)) + COLON + String.format(PAD, Integer.parseInt(data[6], 16)) + (char) Integer.parseInt(data[8], 16) + String.format(PAD, Math.abs((byte) Integer.parseInt(data[9], 16))) + COLON + String.format(PAD, (byte) Integer.parseInt(data[10], 16));
    }

    private static String parsePossibleEmptyValue(Variable value) {
        if (value == null) {
            return "";
        }
        return value.toString();
    }
}
