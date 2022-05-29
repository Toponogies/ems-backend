package vn.com.tma.emsbackend.service.ntpserver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.tma.emsbackend.common.comparator.NTPServiceComparator;
import vn.com.tma.emsbackend.model.dto.NTPServerDTO;
import vn.com.tma.emsbackend.model.entity.Interface;
import vn.com.tma.emsbackend.model.entity.NTPServer;
import vn.com.tma.emsbackend.model.entity.NetworkDevice;
import vn.com.tma.emsbackend.repository.NTPServerRepository;
import vn.com.tma.emsbackend.service.ssh.NTPServerSSHService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NTPServerServiceImpl implements NTPServerService {
    private final NTPServerRepository ntpServerRepository;

    private final NTPServerSSHService ntpServerSSHService;

    @Override
    public List<NTPServerDTO> getAll() {
        return null;
    }

    @Override
    public NTPServerDTO get(long id) {
        return null;
    }

    @Override
    public NTPServerDTO add(NTPServerDTO request) {
        return null;
    }

    @Override
    public NTPServerDTO update(long id, NTPServerDTO request) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Transactional
    @Override
    public void resyncNTPServer(long deviceId) {
        List<NTPServer> oldNtpServers = ntpServerRepository.findByNetworkDevice_Id(deviceId);
        List<NTPServer> newNtpServers = ntpServerSSHService.getAllNtpServer(deviceId);
        NetworkDevice networkDevice = new NetworkDevice();
        networkDevice.setId(deviceId);
        for (NTPServer ntpServer : newNtpServers) {
            ntpServer.setNetworkDevice(networkDevice);
        }
        syncWithDB(newNtpServers, oldNtpServers, new NTPServiceComparator());
    }

    private void syncWithDB(List<NTPServer> newNtpServers, List<NTPServer> oldNtpServers, Comparator<NTPServer> ntpServerComparator) {
        newNtpServers.sort(ntpServerComparator);
        oldNtpServers.sort(ntpServerComparator);
        if (newNtpServers.equals(oldNtpServers)) return;

        HashMap<Integer, NTPServer> integerNTPServerHashMap = new HashMap<>();
        for (NTPServer newNtpServer : newNtpServers) {
            integerNTPServerHashMap.put(newNtpServer.hashCode(), newNtpServer);
        }

        for (NTPServer oldNtpServer : oldNtpServers) {
            NTPServer newNtpServer = integerNTPServerHashMap.get(oldNtpServer.hashCode());
            if (newNtpServer == null) {
                ntpServerRepository.delete(oldNtpServer);
            } else {
                integerNTPServerHashMap.remove(oldNtpServer.hashCode());
            }
        }

        for (Map.Entry<Integer, NTPServer> keyValuePair : integerNTPServerHashMap.entrySet()) {
            ntpServerRepository.save(keyValuePair.getValue());
        }
    }
}
