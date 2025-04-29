package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateShopper {

    private String email;
    private String phone;
    private String password;
}
