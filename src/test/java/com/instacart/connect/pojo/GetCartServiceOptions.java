package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GetCartServiceOptions {
    private Address address;
    private List<Item> items;
    private String location_code;
    private boolean with_eta_options;
    private boolean with_priority_eta_options;

    @Builder
    @Data
    public static class Item {
        private String line_num;
        private int count;
        private InnerItem item;

        @Builder
        @Data
        public static class InnerItem {
            private String upc;
        }
    }

}
