package com.benson.bensonservice.config.security.oauth2;

import com.benson.bensonservice.config.AppProperties;
import com.benson.bensonservice.exception.BadRequestException;
import com.benson.bensonservice.model.vo.TokenResponseVo;
import com.benson.bensonservice.response.Body;
import com.benson.bensonservice.utils.CookieUtils;
import com.benson.bensonservice.utils.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final Logger logger = LogManager.getLogger(OAuth2AuthenticationSuccessHandler.class);
    private AppProperties appProperties;
    private JwtTokenProvider jwtTokenProvider;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, AppProperties appProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        UserPrincipal oAuth2User = (UserPrincipal) authentication.getPrincipal();
        String targetUrl = determineTargetUrl(request, response, authentication);
        String token = this.jwtTokenProvider.createToken(authentication);

        // setting response header
//        response.setHeader("Authorization", "Bearer " + token);

        clearAuthenticationAttributes(request, response);
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        TokenResponseVo tokenResponseVo = TokenResponseVo.builder().token(token).build();
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(Body.build().ok("success", tokenResponseVo));
        logger.info(jsonString.toString());
        response.getWriter().write(jsonString);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
//        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, "redirect_uri")
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String token = jwtTokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if (authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}

