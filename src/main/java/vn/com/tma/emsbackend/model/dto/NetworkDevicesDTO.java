package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class NetworkDevicesDTO {
    private List<NetworkDeviceDTO> devices;
}
