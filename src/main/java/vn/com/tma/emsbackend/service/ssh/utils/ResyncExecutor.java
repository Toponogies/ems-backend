package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.com.tma.emsbackend.controller.WebSocketController;
import vn.com.tma.emsbackend.model.dto.ResyncNotificationDTO;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.model.exception.DeviceNotFoundException;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;
import vn.com.tma.emsbackend.service.ssh.ResyncService;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static vn.com.tma.emsbackend.common.constant.Constant.*;

@Slf4j
@Component
public class ResyncExecutor {
    private final NetworkDeviceRepository networkDeviceRepository;

    private final WebSocketController webSocketTextController;
    private final ResyncService resyncService;
    private final ResyncQueueManager resyncQueueManager;
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(MAX_RESYNC_CONCURRENCY_DEVICE, MAX_RESYNC_CONCURRENCY_DEVICE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public ResyncExecutor(ResyncService resyncService, ResyncQueueManager resyncQueueManager, NetworkDeviceRepository networkDeviceRepository, WebSocketController webSocketTextController) {
        this.resyncService = resyncService;
        this.resyncQueueManager = resyncQueueManager;
        this.resyncQueueManager.setResyncExecutorListener(this);
        this.networkDeviceRepository = networkDeviceRepository;
        this.webSocketTextController = webSocketTextController;
    }


    public void resyncAllDevicesInWaitingQueue() {
        while (resyncQueueManager.isWaitingQueueHasNext() && !isThreadPoolIsFull()) {
            Long id = resyncQueueManager.getNextInWaitingQueue();
            threadPoolExecutor.execute(() -> {
                for (int retryTime = 1; retryTime <= LIMIT_RETRIES_NUMBER; retryTime++) {
                    try {
                        log.info("Try to resync device id: " + id + " Retry: " + retryTime + "/" + LIMIT_RETRIES_NUMBER);

                        resyncService.resyncDeviceById(id);

                        retryTime = LIMIT_RETRIES_NUMBER;
                    } catch (Exception e) {
                        log.error("Resync fail: device id:" + id + " " + e.getMessage());
                        calmDown();
                    }
                }
                resyncQueueManager.popResynchronizingQueue(id);
                resyncAllDevicesInWaitingQueue();
                Optional<NetworkDevice> networkDevice = networkDeviceRepository.findById(id);
                if (networkDevice.isEmpty())
                    throw new DeviceNotFoundException(String.valueOf(id));
                ResyncNotificationDTO resyncNotificationDTO = new ResyncNotificationDTO();
                resyncNotificationDTO.setDevice(networkDevice.get().getLabel());
                webSocketTextController.sendResyncDoneMessage(resyncNotificationDTO);
            });
            resyncQueueManager.pushToResynchronizingQueue(id);
            resyncQueueManager.popWaitingQueue(id);
        }
    }

    private boolean isThreadPoolIsFull() {
        return threadPoolExecutor.getActiveCount() >= threadPoolExecutor.getCorePoolSize();
    }

    private void calmDown() {
        try {
            Thread.sleep(SLOW_DOWN_TIME);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Fail to make thread sleep");
        }
    }

}