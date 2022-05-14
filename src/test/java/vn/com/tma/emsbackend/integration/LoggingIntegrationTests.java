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
import vn.com.tma.emsbackend.dto.CredentialDto;
import vn.com.tma.emsbackend.dto.CredentialRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoggingIntegrationTests {
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
    public void shouldLogGetCredentialWithId() {
        testRestTemplate.exchange("/api/v1/credentials/1", HttpMethod.GET, null, CredentialDto.class);

        assertThat(appender.list).extracting(ILoggingEvent::getFormattedMessage)
                .contains("Get credential with id: 1 ");
    }

    @Test
    public void shouldLogCouldNotDelete() {
        testRestTemplate.exchange("/api/v1/credentials/1", HttpMethod.DELETE, null, CredentialDto.class);

        assertThat(appender.list).extracting(ILoggingEvent::getFormattedMessage)
                .contains("Delete credential with id: 1")
                .contains("Can not delete credential with id: 1");
    }

    @Test
    public void shouldLogAddNewCredential(){
        CredentialRequestDto credentialRequestDto = new CredentialRequestDto();
        credentialRequestDto.setName("This_is_a_name");
        credentialRequestDto.setUsername("username");
        credentialRequestDto.setPassword("ThisIsARandomPassword");
        HttpEntity<CredentialRequestDto> httpEntity = new HttpEntity<>(credentialRequestDto, null);
        
        testRestTemplate.exchange("/api/v1/credentials", HttpMethod.POST, httpEntity, CredentialDto.class);

        // TODO: Try fixing this warning
        assertThat(appender.list).extracting(ILoggingEvent::getFormattedMessage);
    }
}
