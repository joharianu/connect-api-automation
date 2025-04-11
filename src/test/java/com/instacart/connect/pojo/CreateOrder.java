package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateOrder {

    public String order_id;
    public Object service_option_hold_id;
    public long initial_tip_cents;
    public String loyalty_number;
    public String special_instructions;
    public boolean leave_unattended;
    public Address address;
    public List<OrderItem> items;
    public OrderUser user;
    public String location_code;

    @Builder
    @Data
    public static class OrderItem {
        public String line_num;
        public int count;
        public double weight;
        public String special_instructions;
        public Object replacement_policy;
        public Object replacement_items;
        public InnerOrderItem item;
    }

    @Builder
    @Data
    public static class InnerOrderItem {
        public String rrc;
    }

    @Builder
    @Data
    public static class OrderUser {
        public String phone_number;
        public String birthday;
        public boolean sms_opt_in;
    }
}
