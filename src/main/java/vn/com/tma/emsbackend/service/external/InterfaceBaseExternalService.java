package vn.com.tma.emsbackend.service.external;

import vn.com.tma.emsbackend.model.dto.PortDTO;
import vn.com.tma.emsbackend.model.entity.Interface;

import java.util.List;

public interface InterfaceBaseExternalService {
    List<Interface> getAllInterface(long networkDeviceId, List<PortDTO> ports);

    void add(Interface anInterface);

    void edit(String oldInterfaceName, Interface anInterface);

    void delete(Interface anInterface);
}
