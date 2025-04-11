package com.instacart.connect.pojo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetStore {
    private Address find_by;
}
