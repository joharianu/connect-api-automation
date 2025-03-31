package com.instacart.connect.service;

import com.instacart.connect.config.PropertyLoader;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class TokenManagerService {
    private TokenManagerService() {
    }

    private static String token;
    private static LocalDateTime expiryTime;

    public static synchronized String getToken() {
        if (token == null || LocalDateTime.now().isAfter(expiryTime)) {
            String grantType = PropertyLoader.getInstance().getProperty("grant_type");
            String clientId = PropertyLoader.getInstance().getProperty("client_id");
            String clientSecret = PropertyLoader.getInstance().getProperty("client_secret");

            Response response = RestService.postToken(new Token(grantType, clientId, clientSecret));
            if (response.statusCode() == 200) {
                token = response.getBody().jsonPath().getString("access_token");
                expiryTime = LocalDateTime.now().plusSeconds(Long.parseLong(response.getBody().jsonPath().getString("expires_in")));
            }
        }
        return token;
    }

    @Getter
    @AllArgsConstructor
    private static class Token {
        private final String grant_type;
        private final String client_id;
        private final String client_secret;
    }
}
