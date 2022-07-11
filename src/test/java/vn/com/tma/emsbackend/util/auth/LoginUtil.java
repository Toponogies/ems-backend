package vn.com.tma.emsbackend.util.auth;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import vn.com.tma.emsbackend.util.entity.DTO.LoginDTO;

public class LoginUtil {

    private final String KEYCLOAK_SERVER = "http://localhost:5000";

    private final String REALM = "ems";

    private final String KEYCLOAK_CLIENT = "login-app";

    private final String LOGIN_URL = KEYCLOAK_SERVER + "/realms/" + REALM + "/protocol/openid-connect/token";

    public LoginDTO loginAsAdmin(TestRestTemplate testRestTemplate) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
        form.add("username", "admin");
        form.add("password", "admin");
        form.add("client_id", KEYCLOAK_CLIENT);
        form.add("grant_type", "password");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<LoginDTO> responseEntity = testRestTemplate.exchange(LOGIN_URL, HttpMethod.POST, request, LoginDTO.class);
        return responseEntity.getBody();
    }
}
