package com.example.springbootboilerplate.rocket;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SpareSeatResponse {
    String seat;
    Boolean status;
}
