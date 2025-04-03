package com.kh.start.auth.service;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kh.start.auth.model.vo.CustomUserDetails;
import com.kh.start.exception.CustomAuthenticationException;
import com.kh.start.member.model.dto.MemberDTO;
import com.kh.start.token.model.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    // private final JwtUtil jwtUtil;
    
    @Override
    public Map<String, String> login(MemberDTO member) {
        // SpringSecurity
        //
        // 1. 유효성 검증(아이디/ 비밀번호값이 입력되었는가, 영어숫자인가, 글자수가 괜찮은가)
        // 2. 아이디가 MEMBER_ID컬럼에 존재하는 아이디인가
        // 3. 비밀번호는 컬럼에 존재하는 암호문이 사용자가 입력한 평문으로 만들어진것이 맞는가
        // 사용자 인증
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(member.getMemberId(),
                                                             member.getMemberPw()));
        } catch(AuthenticationException e) {
            throw new CustomAuthenticationException("아이디 또는 비밀번호를 잘못 입력하셨습니다.");
        }
        
        CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
        log.info("로그인 성공!");
        log.info("인증에 성공한 사용자의 정보 : {}", user);
        // -------------------------------------------------------------------
        // 토큰 발급
        // JWT 라이브러리를 이용해서
        // AccessToken 및 RefreshToken 발급
        //String accessToken = jwtUtil.getAccessToken(user.getUsername());
        //String refreshToken = jwtUtil.getRefreshToken(user.getUsername());
        // log.info("\n accessToken값 : {} \n refreshToken값 : {} ", accessToken, refreshToken);
        // 같은 값을 가지고 해싱하면 항상 같은 결과가 나옴
        // 해시코드가 다르면 다른 객체다 O ==> 같은 값으로 해시돌리면 항상 결과가 같음
        // 해시코드가 같으면 같은 객체다 X
        Map<String, String> loginResponse = tokenService.generateToken(user.getUsername(), 
        															   user.getMemberNo());
        loginResponse.put("memberId", user.getUsername());
        loginResponse.put("memberName", user.getMemberName());
        loginResponse.put("memberNo", String.valueOf(user.getMemberNo()));
        
        return loginResponse;
    }

	@Override
	public CustomUserDetails getUserDetails() {
		
		Authentication auth = 
				SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		
		return user;
	}
	
	
	
}