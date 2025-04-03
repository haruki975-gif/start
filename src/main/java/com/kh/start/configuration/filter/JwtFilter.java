package com.kh.start.configuration.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kh.start.auth.model.vo.CustomUserDetails;
import com.kh.start.auth.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil util;
	private final UserDetailsService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//log.info("도니?");
		// 토큰 검증
		
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		//log.info("진짜로 넘어옴? {}", authorization);
		
		if(authorization == null || !authorization.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = authorization.split(" ")[1];
		//log.info("토큰 : {}", token);
		
		// 1. 서버에서 관리하는 비밀키로 만들어진 것이 맞는가?
		// 2. 유효기간이 안 지났는가?
		try {
			// 비밀번호 바꾸기 기능 구현하다가
			// 인가 절차 구현해야지
			Claims claims = util.parseJwt(token);
			String username = claims.getSubject();
			
			//log.info(username);
			// 인가받는데 성공한 사용자의 정보가 필요함
			// 사용자의 정보를 SecurityContextHolder -> Context -> Authentication 담을 것
			CustomUserDetails user = (CustomUserDetails)userService.loadUserByUsername(username);
			UsernamePasswordAuthenticationToken authentication
				= new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			// 세부설정 사용자의 IP주소, MAX주소, SessionID 등등이 포함될 수 있음
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// session.setAttribute("loginMember", 사용자정보);
			
		} catch(ExpiredJwtException e) {
			log.info("만료됨");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("만료된 토큰입니다.");
			return;
		} catch(JwtException e) {
			log.info("유효하지 않은 토큰값");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("유효하지 않은 토큰입니다.");
			return;
		}
	
		filterChain.doFilter(request, response);
	}
	
	

}
