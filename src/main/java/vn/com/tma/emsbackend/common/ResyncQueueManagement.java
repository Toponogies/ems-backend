package vn.com.tma.emsbackend.common;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResyncQueueManagement {
    private final Set<Long> networkDevicesWaitingForResync = ConcurrentHashMap.newKeySet();
    private Iterator<Long> currentIterator;

    public boolean isDeviceResyncing(Long deviceId) {
        return networkDevicesWaitingForResync.contains(deviceId);
    }

    public void pushToQueue(Long... networkDevicesId) {
        networkDevicesWaitingForResync.addAll(List.of(networkDevicesId));
    }

    public void pushAll(Collection<Long> networkDevicesId) {
        networkDevicesWaitingForResync.addAll(networkDevicesId);
    }

    public boolean hasNext() {
        return networkDevicesWaitingForResync.iterator().hasNext();
    }

    public Long next() {
        currentIterator = networkDevicesWaitingForResync.iterator();
        return currentIterator.next();
    }
    public void pop(){
        currentIterator.remove();
        currentIterator = null;
    }
}
