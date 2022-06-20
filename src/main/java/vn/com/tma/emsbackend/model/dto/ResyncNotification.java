package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResyncNotification {
    private final String action = "RESYNC_DONE";
    private  Long deviceId;
}
