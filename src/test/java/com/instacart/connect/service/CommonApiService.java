package com.instacart.connect.service;

import io.restassured.response.Response;

import java.util.Map;

public class CommonApiService {
    private CommonApiService (){}

    public static Response post(String uri, Object body) {
        return RestService.post(uri, TokenManagerService.getToken(), body);
    }

    public static Response get(String uri, Map<String, String> queryParams) {
        return RestService.get(uri, TokenManagerService.getToken(), queryParams);
    }
}
