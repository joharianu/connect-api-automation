package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Address {
    private String address_line_1;
    private String postal_code;
}
