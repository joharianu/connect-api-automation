package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private String user_id;
    private String first_name;
    private String last_name;
    private String phone_number;
}
