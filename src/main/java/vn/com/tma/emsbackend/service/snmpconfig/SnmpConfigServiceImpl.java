package vn.com.tma.emsbackend.service.snmpconfig;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.model.mapper.NetworkDeviceMapper;
import vn.com.tma.emsbackend.service.common.SnmpConfigBaseCommonExternalService;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;

@Service
@RequiredArgsConstructor
public class SnmpConfigServiceImpl implements SnmpConfigService {

    private final SnmpConfigBaseCommonExternalService snmpConfigBaseCommonExternalService;

    private final NetworkDeviceService networkDeviceService;

    private final NetworkDeviceMapper networkDeviceMapper;
    @Override
    public void setSnmpTrapConfig(Long deviceId){
        NetworkDeviceDTO networkDeviceDTO = networkDeviceService.get(deviceId);
        snmpConfigBaseCommonExternalService.setSnmpTrapConfig(networkDeviceMapper.dtoToEntity(networkDeviceDTO));
    }
}
