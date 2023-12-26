package com.alibou.security.plan;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlanRequest {

    private Integer id;
    private String name;
    private String description;
    private String imageURL;
    private Integer price;
    private Boolean promo;
}
