package vn.com.tma.emsbackend.service.snmpconfig;

import vn.com.tma.emsbackend.model.entity.NetworkDevice;

public interface SnmpConfigService {
    public void setSnmpTrapConfig(Long deviceId);
}
