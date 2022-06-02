package vn.com.tma.emsbackend.service.ssh.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.enums.Enum;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.NetworkDeviceRepository;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static vn.com.tma.emsbackend.common.constant.Constant.INTERVAL_ADD_ALL_DEVICES_TO_RESYNC_QUEUE;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResyncQueueManager {
    private final NetworkDeviceRepository networkDeviceRepository;

    private final Set<Long> waitingNetworkDevices = ConcurrentHashMap.newKeySet();
    private final Set<Long> resynchronizingNetworkDevices = ConcurrentHashMap.newKeySet();
    private Iterator<Long> currentIterator;


    @Scheduled(fixedRate = INTERVAL_ADD_ALL_DEVICES_TO_RESYNC_QUEUE)
    @Transactional
    public void addAllDeviceToWaitingQueue() {
        pushToWaitingQueue(networkDeviceRepository.findAll().stream().map(NetworkDevice::getId).toArray(Long[]::new));
    }

    public Enum.ResyncStatus getResyncStatus(Long deviceId) {
        if (resynchronizingNetworkDevices.contains(deviceId) || waitingNetworkDevices.contains(deviceId))
            return Enum.ResyncStatus.ONGOING;
        return Enum.ResyncStatus.DONE;
    }

    public void pushToWaitingQueue(Long... deviceIds) {
        waitingNetworkDevices.addAll(List.of(deviceIds));
    }

    public boolean isWaitingQueueHasNext() {
        return waitingNetworkDevices.iterator().hasNext();
    }

    public Long getNextInWaitingQueue() {
        currentIterator = waitingNetworkDevices.iterator();
        return currentIterator.next();
    }

    public void popWaitingQueue() {
        currentIterator.remove();
        currentIterator = null;
    }

    public void pushToResynchronizingQueue(Long id) {
        resynchronizingNetworkDevices.add(id);
    }

    public void popResynchronizingQueue(Long id) {
        resynchronizingNetworkDevices.remove(id);
    }


}
