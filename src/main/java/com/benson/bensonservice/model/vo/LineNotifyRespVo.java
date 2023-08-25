package com.benson.bensonservice.model.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LineNotifyRespVo {
    private Integer id;
    private String notifyToken;
    private Integer userId;
}
