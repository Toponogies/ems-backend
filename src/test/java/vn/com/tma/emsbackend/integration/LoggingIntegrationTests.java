package vn.com.tma.emsbackend.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import vn.com.tma.emsbackend.model.dto.CredentialDTO;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggingIntegrationTests {
    @Autowired
    private TestRestTemplate testRestTemplate;

    private ListAppender<ILoggingEvent> appender;
    private final Logger appLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    @BeforeEach
    public void setUp() {
        appender = new ListAppender<>();
        appender.start();
        appLogger.addAppender(appender);
    }

    @AfterEach
    public void afterEach() {
        appLogger.detachAppender(appender);
    }

    @Test
    void shouldLogGetCredentialWithId() {
        testRestTemplate.exchange("/api/v1/credentials/1", HttpMethod.GET, null, CredentialDTO.class);

        assertThat(appender.list).extracting(ILoggingEvent::getFormattedMessage)
                .contains("Get credential with id: 1 ");
    }

    @Test
    void shouldLogCouldNotDelete() {
        testRestTemplate.exchange("/api/v1/credentials/1", HttpMethod.DELETE, null, CredentialDTO.class);

        assertThat(appender.list).extracting(ILoggingEvent::getFormattedMessage)
                .contains("Delete credential with id: 1");
    }

    @Test
    void shouldLogAddNewCredential(){
        CredentialDTO credentialDto = new CredentialDTO();
        credentialDto.setName("This_is_a_name");
        credentialDto.setUsername("username");
        credentialDto.setPassword("ThisIsARandomPassword");
        HttpEntity<CredentialDTO> httpEntity = new HttpEntity<>(credentialDto, null);
        
        testRestTemplate.exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, CredentialDTO.class);

        assertThat(appender.list).extracting(ILoggingEvent::getFormattedMessage).contains("Add new credential");
    }
}
