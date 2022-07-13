package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static vn.com.tma.emsbackend.common.constant.Constant.INTERVAL_ADD_ALL_DEVICES_TO_RESYNC_QUEUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResyncQueueManager {
    private final NetworkDeviceRepository networkDeviceRepository;

    public final Set<Long> waitingNetworkDevices = ConcurrentHashMap.newKeySet();
    public final Set<Long> resynchronizingNetworkDevices = ConcurrentHashMap.newKeySet();
    private ResyncExecutor resyncExecutorListener;

    private volatile boolean isAddedAllToQueue = true;

    @Scheduled(fixedRate = INTERVAL_ADD_ALL_DEVICES_TO_RESYNC_QUEUE)
    @Transactional
    public void addAllDeviceToWaitingQueue() {
        pushToWaitingQueue(networkDeviceRepository.findAll().stream().map(NetworkDevice::getId).toArray(Long[]::new));
    }

    public Enum.ResyncStatus getResyncStatus(Long deviceId) {
        while (!isAddedAllToQueue) {
            Thread.onSpinWait();
        }
        if (resynchronizingNetworkDevices.contains(deviceId) || waitingNetworkDevices.contains(deviceId))
            return Enum.ResyncStatus.ONGOING;
        return Enum.ResyncStatus.DONE;
    }

    public void pushToWaitingQueue(Long... deviceIds) {
        isAddedAllToQueue = false;
        waitingNetworkDevices.addAll(List.of(deviceIds));
        isAddedAllToQueue = true;
        if (resyncExecutorListener != null) {
            resyncExecutorListener.resyncAllDevicesInWaitingQueue();
        }
    }

    public boolean isWaitingQueueHasNext() {
        return waitingNetworkDevices.iterator().hasNext();
    }

    public Long getNextInWaitingQueue() {
        return waitingNetworkDevices.stream().iterator().next();
    }

    public void popWaitingQueue(Long id) {
        waitingNetworkDevices.remove(id);
    }

    public void pushToResynchronizingQueue(Long id) {
        resynchronizingNetworkDevices.add(id);
    }

    public void popResynchronizingQueue(Long id) {
        resynchronizingNetworkDevices.remove(id);
    }


    public void setResyncExecutorListener(ResyncExecutor resyncExecutorListener) {
        this.resyncExecutorListener = resyncExecutorListener;
    }
}
