package vn.com.tma.emsbackend.repository.ssh;

import org.springframework.stereotype.Repository;
import vn.com.tma.emsbackend.common.commandgenerator.ConfigurationCommandGenerator;
import vn.com.tma.emsbackend.service.ssh.utils.SSHExecutor;

@Repository
public class ConfigurationSSHRepository extends BaseSSHRepository{

    public String exportDeviceConfig(long deviceId){
        SSHExecutor sshExecutor = deviceConnectionManager.getConnection(deviceId);
        String result = sshExecutor.execute(ConfigurationCommandGenerator.export());
        return getMainResult(ConfigurationCommandGenerator.export(), result);
    }    
}
