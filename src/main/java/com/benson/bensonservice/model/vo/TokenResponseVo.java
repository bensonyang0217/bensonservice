package com.benson.bensonservice.model.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenResponseVo {
    //    private String username;
    private String token;
}
