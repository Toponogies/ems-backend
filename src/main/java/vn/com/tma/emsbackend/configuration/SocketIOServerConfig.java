package vn.com.tma.emsbackend.configuration;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
@Slf4j
public class SocketIOServerConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(8082);
        SocketIOServer server =  new SocketIOServer(config);
        server.start();
        server.addConnectListener(socketIOClient -> server.getBroadcastOperations().sendEvent("msg", "hello"));
        return server;
    }
}
