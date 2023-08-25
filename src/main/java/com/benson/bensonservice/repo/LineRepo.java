package com.benson.bensonservice.repo;

import com.benson.bensonservice.model.entity.LineNotify;
import com.benson.bensonservice.model.vo.LineNotifyRespVo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepo extends JpaRepository<LineNotify, Integer> {
    LineNotifyRespVo findByUserId(Integer userId);
}
