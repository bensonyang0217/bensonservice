package com.benson.bensonservice.config.security.oauth2;

import com.benson.bensonservice.config.security.oauth2.user.GithubOAuth2UserInfo;
import com.benson.bensonservice.config.security.oauth2.user.GoogleOAuth2UserInfo;
import com.benson.bensonservice.config.security.oauth2.user.LineOAuth2UserInfo;
import com.benson.bensonservice.config.security.oauth2.user.OAuth2UserInfo;
import com.benson.bensonservice.constants.AuthProvider;
import com.benson.bensonservice.exception.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
//            return null;
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.facebook.toString())) {
            return null;
//            return new FacebookOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
//            return null;
            return new GithubOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.line.toString())) {
            return new LineOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
