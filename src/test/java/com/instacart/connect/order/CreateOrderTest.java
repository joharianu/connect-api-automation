package com.instacart.connect.order;

import com.instacart.connect.config.PropertyLoader;
import com.instacart.connect.pojo.*;
import com.instacart.connect.service.CommonApiService;
import com.instacart.connect.util.RandomDataUtil;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateOrderTest {

    private static List<String> states  =
            List.of("picking", "verifying_replacements", "checkout", "receipt_sanity_check", "delivering", "completed");
    private static Logger logger = LoggerFactory.getLogger(CreateOrderTest.class);

    private static String externalUserId = null;
    private static String phoneNumber = null;
    private static String locationCode = null;
    private static Long serviceOptionId = null;
    private static Long serviceOptionHoldId = null;
    private static String orderId = null;
    private static String orderStatus = null;
    private static String shopperId = null;
    private static String batchId = null;

    @Test(priority = 1)
    public void createUser() {
        User user = RandomDataUtil.createUserData();

        Response response = CommonApiService.post("/v2/fulfillment/users", user);
        response.then().assertThat().statusCode(200);
        externalUserId = response.getBody().jsonPath().getString("user_id");
        phoneNumber = response.getBody().jsonPath().getString("phone_number");
        logger.info("Created user: {}", externalUserId);
    }

    @Test(priority = 2)
    public void getStores() {
        GetStore getStore = GetStore.builder().find_by(getAddress()).build();
        Response response = CommonApiService.post("/v2/fulfillment/stores/delivery", getStore);
        response.then().assertThat().statusCode(200);
        locationCode = response.getBody().jsonPath().getString("stores[0].location_code");
        logger.info("Location Code of first store: {}", locationCode);
    }

    @Test(priority = 3)
    public void getCartServiceOptions() {
        GetCartServiceOptions getCartServiceOptions = GetCartServiceOptions.builder()
                .location_code(locationCode)
                .with_eta_options(true)
                .with_priority_eta_options(true)
                .address(getAddress())
                .items(List.of(GetCartServiceOptions.Item.builder()
                        .line_num("1")
                        .count(4)
                        .item(GetCartServiceOptions.Item.InnerItem.builder()
                                .upc(PropertyLoader.getInstance().getProperty("upc"))
                                .build())
                        .build()))
                .build();

        Response response = CommonApiService.post("/v2/fulfillment/users/" + externalUserId + "/service_options/cart/delivery",
                getCartServiceOptions);
        response.then().assertThat().statusCode(200);
        serviceOptionId = response.getBody().jsonPath().getLong("service_options[0].id");
        logger.info("Service Option Id : {}", serviceOptionId);
    }

    @Test(priority = 4)
    public void reserveServiceOption() {
        Response response = CommonApiService
                .post("/v2/fulfillment/users/" + externalUserId + "/service_options/" + serviceOptionId + "/reserve", "");
        response.then().assertThat().statusCode(200);
        serviceOptionHoldId = response.getBody().jsonPath().getLong("service_option_hold.id");
        logger.info("Service Option Hold Id : {}", serviceOptionHoldId);

    }

    @Test(priority = 5)
    public void createOrder() {

        CreateOrder createOrder = CreateOrder.builder()
                .order_id("TEST-" + RandomDataUtil.getRandomId())
                .service_option_hold_id(serviceOptionHoldId)
                .initial_tip_cents(RandomDataUtil.getRandomCents())
                .loyalty_number(String.valueOf(RandomDataUtil.getShortRandomNumber()))
                .address(getAddress())
                .items(List.of(CreateOrder.OrderItem.builder()
                        .line_num(String.valueOf(RandomDataUtil.getShortRandomNumber()))
                        .count(Long.valueOf(RandomDataUtil.getShortRandomNumber()).intValue())
                        .weight((double) RandomDataUtil.getShortRandomNumber())
                        .special_instructions("")
                        .item(CreateOrder.InnerOrderItem.builder()
                                .rrc("594827")
                                .build())
                        .build()))
                .user(CreateOrder.OrderUser.builder()
                        .phone_number(phoneNumber)
                        .birthday(RandomDataUtil.getRandomDate())
                        .sms_opt_in(true)
                        .build())
                .location_code(locationCode)
                .build();

        Response response = CommonApiService
                .post("/v2/fulfillment/users/" + externalUserId + "/orders/delivery", createOrder);
        response.then().assertThat().statusCode(200);
        orderId = response.getBody().jsonPath().getString("id");
        orderStatus = response.getBody().jsonPath().getString("status");
        logger.info("Order Created with Id : {} and Status : {}", orderId, orderStatus);

    }

    @Test(priority = 6)
    public void createShopper() {
        CreateShopper createOrder = CreateShopper.builder()
                .email(RandomDataUtil.getRandomEmail())
                .phone(RandomDataUtil.getPhoneNumber())
                .password("123456")
                .build();
        Response response = CommonApiService
                .post("/v2/fulfillment_sandbox/shoppers", createOrder);
        response.then().assertThat().statusCode(200);
        shopperId = response.getBody().jsonPath().getString("id");
        logger.info("Shopper Created with Id : {}", shopperId);
    }

    @Test(priority = 7)
    public void generateBatch() {
        GenerateBatch generateBatch = GenerateBatch.builder()
                .shopper_id(shopperId)
                .batch_type("delivery")
                .orders(List.of(GenerateBatch.Order.builder()
                        .id(orderId)
                        .user_id(externalUserId)
                        .build())).build();
        Response response = CommonApiService
                .post("/v2/fulfillment_sandbox/batches", generateBatch);
        response.then().assertThat().statusCode(200);
        batchId = response.getBody().jsonPath().getString("id");
        logger.info("Batch Created with Id : {}", batchId);
    }

    @Test(priority = 8)
    public void advanceBatchToCompleted() {
        states.forEach(state -> {
            Response response = CommonApiService
                    .post("/v2/fulfillment_sandbox/batches/"+batchId+"/advance", "{\"workflow_state\": \""+state+"\"}");
            response.then().assertThat().statusCode(200);
            String workflowState = response.getBody().jsonPath().getString("workflow_state");
            assert workflowState.equals(state);
        });

    }

    private Address getAddress() {
        return Address.builder()
                .address_line_1(PropertyLoader.getInstance().getProperty("address_line_1"))
                .postal_code(PropertyLoader.getInstance().getProperty("postal_code"))
                .build();
    }

}
