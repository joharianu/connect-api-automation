package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GenerateBatch {

    private String shopper_id;
    private String batch_type;
    private List<Order> orders;

    @Builder
    @Data
    public static class Order {
        private String id;
        private String user_id;
    }

}
