package vn.com.tma.emsbackend.model.dto;

import lombok.Getter;
import lombok.Setter;
import vn.com.tma.emsbackend.common.enums.Enum;

@Getter
@Setter
public class ResyncNotificationDTO extends SocketDTO {
    private String device;

    public ResyncNotificationDTO() {
        this.socketAction = Enum.SocketAction.RESYNC_DONE;
    }
}
