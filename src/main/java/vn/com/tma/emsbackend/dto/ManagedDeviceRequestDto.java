package vn.com.tma.emsbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagedDeviceRequestDto {
    private String label;

    private String ipAddress;

    private long port;

    private long credentialId;
}
