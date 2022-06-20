package vn.com.tma.emsbackend.service.ssh.utils;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.model.dto.ResyncNotification;
import vn.com.tma.emsbackend.service.ssh.ResyncService;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static vn.com.tma.emsbackend.common.constant.Constant.*;

@Slf4j
@Component
public class ResyncExecutor {
    private final ResyncService resyncService;
    private final ResyncQueueManager resyncQueueManager;

    private final SocketIOServer socketIOServer;
    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(MAX_RESYNC_CONCURRENCY_DEVICE, MAX_RESYNC_CONCURRENCY_DEVICE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public ResyncExecutor(ResyncService resyncService, ResyncQueueManager resyncQueueManager, SocketIOServer socketIOServer) {
        this.resyncService = resyncService;
        this.resyncQueueManager = resyncQueueManager;
        this.resyncQueueManager.setResyncExecutorListener(this);
        this.socketIOServer = socketIOServer;
    }


    @Transactional
    public void resyncAllDevicesInWaitingQueue() {
        while (resyncQueueManager.isWaitingQueueHasNext() && !isThreadPoolIsFull()) {
            Long id = resyncQueueManager.getNextInWaitingQueue();
            threadPoolExecutor.execute(() -> {
                for (int retryTime = 1; retryTime <= LIMIT_RETRIES_NUMBER; retryTime++) {
                    try {
                        log.info("Try to resync device id: " + id + " Retry: " + retryTime + "/" + LIMIT_RETRIES_NUMBER);
                        resyncQueueManager.pushToResynchronizingQueue(id);
                        resyncService.resyncDeviceById(id);
                        retryTime = LIMIT_RETRIES_NUMBER;
                    } catch (Exception e) {
                        log.error("Resync fail: device id:" + id + " " + e.getMessage());
                        calmDown();
                    }
                }
                resyncQueueManager.popResynchronizingQueue(id);
                resyncAllDevicesInWaitingQueue();
                ResyncNotification resyncNotification = new ResyncNotification();
                resyncNotification.setDeviceId(id);
                socketIOServer.getBroadcastOperations().sendEvent("resync", resyncNotification);
            });
            resyncQueueManager.popWaitingQueue();
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