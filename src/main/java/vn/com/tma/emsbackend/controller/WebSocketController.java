package vn.com.tma.emsbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import vn.com.tma.emsbackend.model.dto.ResyncNotificationDTO;

@RestController
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessagingTemplate template;

    public void sendResyncDoneMessage(ResyncNotificationDTO resyncNotificationDTO) {
        template.convertAndSend("/topic/resync-done", resyncNotificationDTO);
    }


    @SendTo("/topic/resync-done")
    public ResyncNotificationDTO broadcastMessage(@Payload ResyncNotificationDTO resyncNotification) {
        return resyncNotification;
    }

}