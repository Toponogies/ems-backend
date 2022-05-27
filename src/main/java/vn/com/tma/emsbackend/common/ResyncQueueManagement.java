package vn.com.tma.emsbackend.common;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public class ResyncQueueManagement {
    private final Set<Long> networkDevicesWaitingForResync = ConcurrentHashMap.newKeySet();
    private Iterator<Long> currentIterator;

    private static final int MAX_RESYNC_CONCURRENCY_DEVICE = 3;

    private int currentInResyncDevice = 0;


    public final Executor executor = Executors.newFixedThreadPool(MAX_RESYNC_CONCURRENCY_DEVICE);


    public boolean isDeviceResyncing(Long deviceId) {
        return networkDevicesWaitingForResync.contains(deviceId);
    }

    public void pushToQueue(Long... deviceIds) {
        networkDevicesWaitingForResync.addAll(List.of(deviceIds));
    }
    public void pushAll(Collection<Long> networkDevicesId) {
        networkDevicesWaitingForResync.addAll(networkDevicesId);
    }

    public boolean hasNext() {
        return networkDevicesWaitingForResync.iterator().hasNext() && currentInResyncDevice < MAX_RESYNC_CONCURRENCY_DEVICE;
    }

    public Long next() {
        currentIterator = networkDevicesWaitingForResync.iterator();
        currentInResyncDevice++;
        log.info(String.valueOf(currentInResyncDevice));
        return currentIterator.next();
    }

    public void pop() {
        currentIterator.remove();
        currentIterator = null;
    }

    public void release() {
        currentInResyncDevice--;
        log.info(String.valueOf(currentInResyncDevice));
    }
}
