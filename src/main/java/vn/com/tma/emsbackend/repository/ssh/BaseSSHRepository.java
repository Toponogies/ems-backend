package vn.com.tma.emsbackend.repository.ssh;

import org.springframework.beans.factory.annotation.Autowired;
import vn.com.tma.emsbackend.common.DeviceConnectionManager;

public abstract class BaseSSHRepository {
    @Autowired
    DeviceConnectionManager deviceConnectionManager;
}
