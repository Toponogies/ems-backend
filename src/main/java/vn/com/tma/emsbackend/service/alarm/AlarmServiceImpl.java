package vn.com.tma.emsbackend.service.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.dto.AlarmDTO;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.entity.Alarm;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.mapper.AlarmMapper;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.service.common.AlarmCommonExternalService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmCommonExternalService alarmCommonExternalService;
    private final NetworkDeviceService networkDeviceService;
    private final NetworkDeviceMapper networkDeviceMapper;
    private final AlarmMapper alarmMapper;


    public List<AlarmDTO> getAllAlarmByDeviceId(Long deviceId) {
        NetworkDevice networkDevice = networkDeviceMapper.dtoToEntity(networkDeviceService.get(deviceId));
        List<AlarmDTO> alarmDTOS = alarmMapper.entitiesToDTOs(alarmCommonExternalService.getAllAlarmByDevice(networkDevice));
        for(AlarmDTO alarmDTO:alarmDTOS){
            alarmDTO.setNetworkDevice(networkDevice.getLabel());
        }
        return alarmDTOS;
    }

    @Override
    public List<AlarmDTO> getAllAlarm() {
        List<NetworkDeviceDTO> networkDeviceDTOS = networkDeviceService.getAll();
        List<AlarmDTO> alarmDTOS = new ArrayList<>();
        for (NetworkDeviceDTO networkDeviceDTO : networkDeviceDTOS) {
            List<Alarm> alarmsByDevice = null;
            try {
                alarmsByDevice = alarmCommonExternalService.getAllAlarmByDevice(networkDeviceMapper.dtoToEntity(networkDeviceDTO));
            } catch (Exception e) {
                e.printStackTrace();
                alarmsByDevice = new ArrayList<>();
            }
            alarmDTOS.addAll(alarmsByDevice.stream().map(alarm -> {
                AlarmDTO alarmDTO = alarmMapper.entityToDTO(alarm);
                alarmDTO.setNetworkDevice(networkDeviceDTO.getLabel());
                return alarmDTO;
            }).collect(Collectors.toList()));

        }
        return alarmDTOS;
    }
}
