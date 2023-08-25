package com.benson.bensonservice.controller;

import com.benson.bensonservice.model.vo.SendLineNotifyRespVo;
import com.benson.bensonservice.response.Body;
import com.benson.bensonservice.service.LineApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/line")
public class LineApiController {
    private static final Logger logger = LogManager.getLogger(LineApiController.class);
    @Autowired
    private LineApiService lineApiService;

    @GetMapping("/auth_notify")
    public String authNotify() {
        logger.info("auth notify");
        return lineApiService.authNotify();
    }

    @GetMapping("/notify/callback")
    public ResponseEntity<String> lineNotifyCallBack(@RequestParam(value = "code", required = false) String code,
                                                     @RequestParam("state") String state,
                                                     @RequestParam(value = "error", required = false) String error) {
        logger.info("=========notify callback========");

        Optional.ofNullable(code).ifPresent(c -> {
            logger.info("code: {}", c);
        });
        Optional.ofNullable(state).ifPresent(s -> logger.info("state: {}", s));
        if (error != null) {
            logger.error("An error occurred: {}", error);
            return ResponseEntity.badRequest().body("An error occurred: " + error);
        }
        return ResponseEntity.ok(lineApiService.tokenNotify(code).getBody());
    }

    @PostMapping("/notify")
    public ResponseEntity<Body> sendNotify(@RequestBody HashMap<String, String> request) throws JsonProcessingException {
        String message = request.get("message");
        logger.info("message: {}", message);
        var resp = lineApiService.sendNotify(message).getBody();
//        if (resp.equals("400")) {
//
//        }
        ObjectMapper mapper = new ObjectMapper();
        SendLineNotifyRespVo sendLineNotifyRespVo = mapper.readValue(resp, SendLineNotifyRespVo.class);
        if (sendLineNotifyRespVo.getStatus() != HttpStatus.OK.value()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Body.build().fail("Not Found."));
        }
        return ResponseEntity.ok(Body.build().ok("ok"));
    }
}
