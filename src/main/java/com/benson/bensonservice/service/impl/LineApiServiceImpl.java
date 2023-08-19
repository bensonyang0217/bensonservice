package com.benson.bensonservice.service.impl;

import com.benson.bensonservice.constants.ExternalServiceUrl;
import com.benson.bensonservice.service.LineApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * line api
 *
 * @author benson
 * @link https://notify-bot.line.me/doc/en
 * @date 2023/08/18
 */
@Service
public class LineApiServiceImpl implements LineApiService {
    private static final Logger logger = LogManager.getLogger(LineApiServiceImpl.class);
    @Value("${line.notify.clientId}")
    private String clientId;
    @Value("${line.notify.clientSecret}")
    private String clientSecret;
    @Value("${line.notify.redirectUri}")
    private String notifyRedirectUri;

    /**
     * auth notify
     *
     * @return url
     */
    @Override
    public String authNotify() {
        String authUrl = ExternalServiceUrl.AUTH_NOTIFY.getUrl();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", notifyRedirectUri)
                .queryParam("scope", "notify")
                .queryParam("state", "state");

        return builder.toUriString();
    }

    /**
     * tokenNotify
     *
     * @param code
     * @return {@link ResponseEntity}<{@link String}>
     */
    @Override
    public ResponseEntity<String> tokenNotify(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", notifyRedirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        String tokenUrl = ExternalServiceUrl.TOKEN_NOTIFY.getUrl();
        try {
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("Error occurred while calling the remote service.", e);
            return ResponseEntity.status(e.getStatusCode()).body("An error occurred: " + e.getLocalizedMessage());
        } catch (HttpServerErrorException e) {
            logger.error("Server error occurred while calling the remote service.", e);
            return ResponseEntity.status(e.getStatusCode()).body("Server error occurred. Please try again later.");
        }
    }
}