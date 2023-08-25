package com.benson.bensonservice.config.security.oauth2.user;

import java.util.Map;

public class LineOAuth2UserInfo extends OAuth2UserInfo {
    private String statusMessage;

    public LineOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("userId");
    }

    @Override
    public String getName() {
        return (String) attributes.get("displayName");
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("pictureUrl");
    }

    public String getStatusMessage() {
        return (String) attributes.get("statusMessage");
    }

}
