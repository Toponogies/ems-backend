package vn.com.tma.emsbackend.repository.ssh;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.commandgenerator.AlarmCommandGenerator;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.parser.ssh.AlarmCommandParser;
import vn.com.tma.emsbackend.service.ssh.utils.SSHExecutor;

import java.util.List;

@Repository
@AllArgsConstructor
public class AlarmSSHRepository extends BaseSSHRepository {

    public List<Alarm> getAllAlarmByDevice(long deviceId){
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(deviceId);
        String result = sshExecutor.execute(AlarmCommandGenerator.getAllAlarmWithDetail());
        return AlarmCommandParser.alarmShowStatusMore(result);
    }
}
