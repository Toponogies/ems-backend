package vn.com.tma.emsbackend.service.external;

import vn.com.tma.emsbackend.model.entity.Port;

import java.util.List;

public interface PortBaseExternalService {
    List<Port> getAllPort(long deviceId);
}
