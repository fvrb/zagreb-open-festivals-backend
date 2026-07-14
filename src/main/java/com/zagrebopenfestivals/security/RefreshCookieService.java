package com.zagrebopenfestivals.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Component
public class RefreshCookieService {

    private static final String REFRESH_COOKIE = "refreshToken";

    private final long refreshExpirationMs;
    private final boolean refreshCookieSecure;

    public RefreshCookieService(
            @Value("${jwt.refresh-expiration-ms:604800000}") long refreshExpirationMs,
            @Value("${jwt.refresh-cookie.secure:false}") boolean refreshCookieSecure) {
        this.refreshExpirationMs = refreshExpirationMs;
        this.refreshCookieSecure = refreshCookieSecure;
    }

    public String read(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nedostaje refresh token");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> REFRESH_COOKIE.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nedostaje refresh token"));
    }

    public void set(HttpServletResponse response, String rawToken) {
        int maxAgeSeconds = (int) (refreshExpirationMs / 1000);
        response.addHeader("Set-Cookie", buildHeader(rawToken, maxAgeSeconds));
    }

    public void clear(HttpServletResponse response) {
        response.addHeader("Set-Cookie", buildHeader("", 0));
    }

    private String buildHeader(String value, int maxAgeSeconds) {
        String secureAttribute = refreshCookieSecure ? "; Secure" : "";
        return String.format(
                "%s=%s; Max-Age=%d; Path=/auth; HttpOnly; SameSite=Lax%s",
                REFRESH_COOKIE, value, maxAgeSeconds, secureAttribute
        );
    }
}
