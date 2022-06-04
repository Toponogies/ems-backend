package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.service.ssh.ResyncService;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static vn.com.tma.emsbackend.common.constant.Constant.INTERVAL_CHECK_RESYNC_QUEUE;
import static vn.com.tma.emsbackend.common.constant.Constant.MAX_RESYNC_CONCURRENCY_DEVICE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResyncExecutor {
    private final ResyncService resyncService;
    private final ResyncQueueManager resyncQueueManager;


    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(MAX_RESYNC_CONCURRENCY_DEVICE, MAX_RESYNC_CONCURRENCY_DEVICE,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());


    @Scheduled(fixedRate = INTERVAL_CHECK_RESYNC_QUEUE)
    @Transactional
    public void resyncAllDevicesInWaitingQueue() {
        while (resyncQueueManager.isWaitingQueueHasNext() && !isThreadPoolIsFull()) {
            Long id = resyncQueueManager.getNextInWaitingQueue();
            threadPoolExecutor.execute(() -> {
                try {
                    resyncQueueManager.pushToResynchronizingQueue(id);
                    resyncService.resyncDeviceById(id);
                } catch (Exception e) {
                    log.error("Resync fail: device id:" + id + " " + e.getMessage());
                } finally {
                    resyncQueueManager.popResynchronizingQueue(id);
                }
            });
            resyncQueueManager.popWaitingQueue();
        }
    }

    private boolean isThreadPoolIsFull() {
        return threadPoolExecutor.getActiveCount() >= threadPoolExecutor.getCorePoolSize();
    }

}