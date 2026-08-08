package com.example.SmartHome.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class OtpVerificationFilter extends OncePerRequestFilter {

    private static final String[] ALLOWED_PATHS = {
            "/verify-otp", "/login", "/logout", "/register",
            "/forgot-password", "/reset-password", "/css",
            "/js", "/images", "/favicon.ico", "/favicon-16x16.png",
            "/favicon-32x32.png", "/apple-touch-icon.png"
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String uri = request.getRequestURI();

        boolean isAllowedPath = false;
        for (String path : ALLOWED_PATHS) {
            if (uri.startsWith(path)) { isAllowedPath = true; break; }
        }

        boolean isRealAuth = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);

        if (isRealAuth && !isAllowedPath) {
            Boolean otpVerified = (Boolean) request.getSession().getAttribute("otpVerified");
            if (otpVerified == null || !otpVerified) {
                response.sendRedirect("/verify-otp");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
