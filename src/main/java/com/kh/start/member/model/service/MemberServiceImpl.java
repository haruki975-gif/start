package com.kh.start.member.model.service;


import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.start.auth.model.vo.CustomUserDetails;
import com.kh.start.exception.MemberIdDuplicateException;
import com.kh.start.member.model.dao.MemberMapper;
import com.kh.start.member.model.dto.ChangePasswordDTO;
import com.kh.start.member.model.dto.MemberDTO;
import com.kh.start.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
	
	private final MemberMapper mapper;
	private final PasswordEncoder passwordEncoder;
	
	// 회원 등록
	public void signUp(MemberDTO member) {
		MemberDTO searchedMember = mapper.getMemberByMemberId(member.getMemberId());
		
		if(searchedMember != null) {
			throw new MemberIdDuplicateException("이미 존재하는 아이디입니다.");
		}
		// 할일 두개
		// 첫번째: 비밀번호 암호화
		// 두번째: 권한 롤 주기
		Member memberValue = Member.builder()
								.memberId(member.getMemberId())
								.memberPw(passwordEncoder.encode(member.getMemberPw()))
								.memberName(member.getMemberName())
								.role("ROLE_USER")
								.build();
		
		mapper.signUp(memberValue);
		log.info("사용자 등록 성공 : {}", member);
	}

	
	// 비밀번호 변경
	@Override
	public void changePassword(ChangePasswordDTO passwordEntity) {
		// 현재 비밀번호를 맞게 쳤는지 검증
		// 맞으면 새로운 비밀번호를 암호화
		// SecurityContextHolder에서 사용자 정보 받아오기
		
		// -> PasswordEncoder => matches()
		// 첫번째 인자 : 평문, 두번째 인자 : 암호문
		
		String encodePassword = passwordEncoder.encode(passwordEntity.getNewPassword());
		Long memberNo = passwordMatches(passwordEntity.getCurrentPassword());
		
		Map<String, Object> changeRequest = new HashMap();
		changeRequest.put("memberNo", memberNo);
		changeRequest.put("encodePassword", encodePassword);
		
		// mapper에 가서 update
		// UPDATE TB_BOOT_MEMBER SET MEMBER_PW = ? WHERE MEMBER_ID/NO = 현재요청한사용자의식별값
		mapper.changePassword(changeRequest);
	}

	
	// 비밀번호 받아서 회원 삭제
	@Override
	public void deleteByPassword(String password) {
		
		// 사용자가 입력한 비밀번호와 DB에 저장된 비밀번호가 일치하는지 확인
		
		// Mapper가서 -> DELETE
		// DELETE FROM TB_BOOT_MEMBER WHERE MEMBER_NO = ?
		Long memberNo = passwordMatches(password);
		mapper.deleteByPassword(memberNo);
	}
	
	private Long passwordMatches(String password) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails)auth.getPrincipal();
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}
		return user.getMemberNo();
	}
	
	

}
