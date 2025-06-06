//package com.meossamos.smore.global.jwt;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//@Slf4j
//public class JwtFilter extends OncePerRequestFilter {
//
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    public static final String BEARER_PREFIX = "Bearer ";
//
//    private final TokenProvider tokenProvider;
//
//    // 실제 필터링 로직은 doFilterInternal 에 들어감
//    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//
//        // 채팅 API와 WebSocket, SSE 엔드포인트는 JWT 검증 건너뛰기
//        String requestURI = request.getRequestURI();
//        if (requestURI.startsWith("/api/chatrooms/") || requestURI.startsWith("/ws/") || requestURI.startsWith("/sse/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 1. Request Header 에서 토큰을 꺼냄
//        String bearerToken =  request.getHeader(AUTHORIZATION_HEADER);
//
//        String jwt = resolveToken(bearerToken);
//        // 2. validateToken 으로 토큰 유효성 검사
//        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
//        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
//            Authentication authentication = tokenProvider.getAuthentication(jwt);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            logger.debug("JWT 토큰이 유효합니다.");
//        } else {
//            logger.warn("JWT 토큰이 유효하지 않습니다." + jwt);
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    // Request Header 에서 토큰 정보를 꺼내오기
//    private String resolveToken(String bearerToken) {
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//
//}


package com.meossamos.smore.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        // JWT 검증을 건너뛰어야 하는 엔드포인트 (채팅, WebSocket, SSE)
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/chatrooms/") ||
                requestURI.startsWith("/ws/") ||
                requestURI.startsWith("/sse/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 토큰 가져오기
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        String jwt = resolveToken(bearerToken);

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("JWT 토큰이 유효합니다.");
        } else {
            log.warn("JWT 토큰이 유효하지 않습니다. " + jwt);
        }
        filterChain.doFilter(request, response);
    }

    // Authorization 헤더에서 "Bearer " 접두어를 제거하고 순수 토큰 반환
    private String resolveToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken)) {
            bearerToken = bearerToken.trim();
            if (bearerToken.toLowerCase().startsWith(BEARER_PREFIX.toLowerCase())) {
                return bearerToken.substring(BEARER_PREFIX.length()).trim();
            }
        }
        return null;
    }
}
