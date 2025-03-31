package com.instacart.connect.order;

import com.instacart.connect.pojo.User;
import com.instacart.connect.service.CommonApiService;
import com.instacart.connect.util.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class CreateOrderTest {

    private static String externalUserId = null;

    @Test
    public void createUser() {
        User user = RandomDataUtil.createUserData();

        Response response = CommonApiService.post("/v2/fulfillment/users", user);
        response.then().assertThat().statusCode(200);
        externalUserId = response.getBody().jsonPath().getString("user_id");
    }

}
