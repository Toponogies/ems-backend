package vn.com.tma.emsbackend.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.NetworkDeviceDTO;
import vn.com.tma.emsbackend.service.device.NetworkDeviceService;
import vn.com.tma.emsbackend.service.resync.ResyncService;

import java.util.stream.Collectors;

import static vn.com.tma.emsbackend.common.constant.Constant.SCHEDULE_CHECK_RESYNC_QUEUE_IN_MILLISECONDS;
import static vn.com.tma.emsbackend.common.constant.Constant.SCHEDULE_RESYNC_ALL_TIME_IN_MILLISECONDS;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResyncScheduler {
    private final NetworkDeviceService networkDeviceService;
    private final ResyncService resyncService;
    private final ResyncQueueManagement resyncQueueManagement;


    @Scheduled(fixedRate = SCHEDULE_RESYNC_ALL_TIME_IN_MILLISECONDS)
    @Transactional
    public void addAllDeviceToQueue() {
        resyncQueueManagement.pushAll(networkDeviceService.getAll().stream().map(NetworkDeviceDTO::getId).collect(Collectors.toList()));
    }

    @Scheduled(fixedRate = SCHEDULE_CHECK_RESYNC_QUEUE_IN_MILLISECONDS)
    @Transactional
    public void executeResync() {
        if (resyncQueueManagement.hasNext()) {
            Long id = resyncQueueManagement.next();
            resyncQueueManagement.executor.execute(() -> {
                resyncService.resync(id);
                resyncQueueManagement.release();
            });
            resyncQueueManagement.pop();
        }
    }
}