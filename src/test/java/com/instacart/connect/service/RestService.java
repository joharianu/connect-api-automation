package com.instacart.connect.service;

import com.instacart.connect.config.PropertyLoader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.Map;

public class RestService {
    private RestService() {}

    private static final RequestSpecification requestSpecification =
            new RequestSpecBuilder().setBaseUri(PropertyLoader.getInstance().getBaseUrl()).log(LogDetail.ALL)
                    .setContentType("application/json").build();
    private static final ResponseSpecification responseSpecification =
            new ResponseSpecBuilder().log(LogDetail.ALL).build();

    public static Response postToken(Object requestBody) {
        return RestAssured.given(requestSpecification)
                .body(requestBody)
                .when().post(PropertyLoader.getInstance().getTokenApiUri())
                .then().spec(responseSpecification)
                .extract().response();
    }

    public static Response post(String uri, String token, Object requestBody) {
        return RestAssured.given(requestSpecification)
                .body(requestBody)
                .auth().oauth2(token)
                .when().post(uri)
                .then().spec(responseSpecification)
                .extract().response();
    }

    public static Response get(String uri, String token, Map<String, String> queryParams) {
        return RestAssured.given(requestSpecification)
                .params(queryParams)
                .auth().oauth2(token)
                .when().post(uri)
                .then().spec(responseSpecification)
                .extract().response();
    }

}
