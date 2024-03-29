package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.repository.ssh.AlarmSSHRepository;
import vn.com.tma.emsbackend.service.external.AlarmBaseExternalService;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AlarmSSHService implements AlarmBaseExternalService {
    private final AlarmSSHRepository alarmSSHRepository;

    @Override
    public List<Alarm> getAllAlarmByDevice(Long deviceId) {
        return alarmSSHRepository.getAllAlarmByDevice(deviceId);
    }
}
