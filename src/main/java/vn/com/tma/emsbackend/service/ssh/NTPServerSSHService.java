package vn.com.tma.emsbackend.service.ssh;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.com.tma.emsbackend.model.entity.NTPServer;
import vn.com.tma.emsbackend.repository.ssh.NTPServiceSSHRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NTPServerSSHService {
    private final NTPServiceSSHRepository ntpServiceSSHRepository;

    public List<NTPServer> getAllNtpServer(long deviceId){
        return ntpServiceSSHRepository.getAll(deviceId);
    }
}
