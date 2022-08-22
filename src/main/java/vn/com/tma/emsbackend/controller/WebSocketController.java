package vn.com.tma.emsbackend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import vn.com.tma.emsbackend.model.dto.AlarmDTO;
import vn.com.tma.emsbackend.model.dto.ResyncNotificationDTO;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final SimpMessagingTemplate template;

    public void sendResyncDoneMessage(ResyncNotificationDTO resyncNotificationDTO) {
        template.convertAndSend("/topic/resync-done", resyncNotificationDTO);
    }

    public void sendNewAlarmMessage(AlarmDTO alarmDTO) {
        template.convertAndSend("/topic/alarm", alarmDTO);
    }


    @SendTo("/topic/resync-done")
    public ResyncNotificationDTO broadcastMessage(@Payload ResyncNotificationDTO resyncNotification) {
        return resyncNotification;
    }

    @SendTo("/topic/alarm")
    public AlarmDTO broadcastMessage(@Payload AlarmDTO alarmDTO) {
        log.info("Send alarm");
        return alarmDTO;
    }

}