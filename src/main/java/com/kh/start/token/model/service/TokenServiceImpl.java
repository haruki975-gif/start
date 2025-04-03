package com.kh.start.token.model.service;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kh.start.auth.util.JwtUtil;
import com.kh.start.token.model.dao.TokenMapper;
import com.kh.start.token.model.vo.RefreshToken;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	
	private final JwtUtil tokenUtil;
	private final TokenMapper tokenMapper;
	
	@Override
	public Map<String, String> generateToken(String username, Long memberNo) {
		
		// 1, 2번
		Map<String, String> tokens = createToken(username);
		
		// 3번(memberNo, refreshToken, expiration)
		saveToken(tokens.get("refreshToken"), memberNo);
		
		// 4번(만료된 토큰 날리기)
		tokenMapper.deleteExpiredRefreshToken(System.currentTimeMillis());
	
		// 5번
		
		return tokens;
	}
	
	@Override
	public Map<String, String> refreshToken(String refreshToken) {
		
		RefreshToken token = RefreshToken.builder()
										 .token(refreshToken)
										 .build();
		// ㄴ여기에 유저넘버도 같이 달아서 보내기(1번숙제)
		
		RefreshToken responseToken = tokenMapper.findByToken(token);
		// 오늘 숙제 1.JwtUtil을 이용하여 refreshToken을 Parsing한 뒤
		// MEMBER_NO 및 token값을 이용하여 SELECT하는 문으로 수정하기 => JwtFilter참고
		
		// 오늘 숙제 2. 예외 발생 시 예외처리 핸들러 싸그리 몽땅 커스텀 익셉션 클래스를 구현하여
		// 			  예외처리기가 처리하게 하기
		if(responseToken == null || token.getExpiration() < System.currentTimeMillis()) {
			throw new RuntimeException("유효하지 않은 토큰입니다.");
		}
		
		String username = getUsernameByToken(refreshToken);
		Long memberNo = responseToken.getMemberNo();
		return generateToken(username, memberNo);
	}
	
	private String getUsernameByToken(String refreshToken) {
		Claims claims = tokenUtil.parseJwt(refreshToken);
		return claims.getSubject();
	}
	
	private void saveToken(String refreshToken, Long memberNo) {
		RefreshToken token = RefreshToken.builder()
										 .token(refreshToken)
										 .memberNo(memberNo)
										 .expiration(System.currentTimeMillis() + 36000000L * 72)
										 .build();
		// 인서트
		tokenMapper.saveToken(token);
	}
	
	private Map<String, String> createToken(String username) {
		String accessToken = tokenUtil.getAccessToken(username);
		String refreshToken = tokenUtil.getRefreshToken(username);
		
		Map<String, String> loginResponse = new HashMap();
		loginResponse.put("accessToken", accessToken);
		loginResponse.put("refreshToken", refreshToken);
		
		return loginResponse;
	}
}