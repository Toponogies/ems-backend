package vn.com.tma.emsbackend.parser;

import vn.com.tma.emsbackend.common.constant.SSHColumn;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.parser.splitter.TableSplitter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlarmCommandParser {

    public static List<Alarm> alarmShowStatusMore(String executeResult) {
        List<Alarm> alarms = new ArrayList<>();
        TableSplitter tableReader = new TableSplitter(executeResult);
        while (tableReader.next()) {
            try {
                Alarm alarm = new Alarm();
                alarm.setDate(tableReader.getValue(SSHColumn.AlarmDetail.TIME));
                alarm.setCondition(tableReader.getValue(SSHColumn.AlarmDetail.CONDITION));
                alarm.setAlarmNumber(tableReader.getValue(SSHColumn.AlarmDetail.ALARM_NUMBER));
                alarm.setSeverity(Enum.Severity.parse(tableReader.getValue(SSHColumn.AlarmDetail.SEVERITY)));
                alarm.setDescription(tableReader.getValue(SSHColumn.AlarmDetail.DESCRIPTION));
                alarms.add(alarm);
            }catch(Exception e){
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
        return alarms;
    }
}
