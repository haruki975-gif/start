package com.kh.start.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.start.member.model.dto.ChangePasswordDTO;
import com.kh.start.member.model.dto.MemberDTO;
import com.kh.start.member.model.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
	
    /* Bean 설정을 통한 의존성을 낮추기 위한 의존해야하는 타입 작성 */
	private final MemberService memberService;

	// 회원가입 자원요청을 받으면서
	// 아이디/비밀번호/이름 받아서 서비스로 넘김
	// JSON 형태로 상태 코드와 함께 넘김
	
	// /members = 약속
	// GET                  --> 조회 요청(SELECT)
	// GET(/members/멤버번호) --> 단일 조회 요청(SELECT) 멤버 번호로 조건을 검
	// POST                 --> 데이터 생성 요청 (INSERT)
	// PUT                  --> 데이터 갱신 요청 (UPDATE)
	// DELETE               --> 데이터 삭제 요청 (DELETE)
	
	// 계층구조로 식별할 때 /자원/PK
	// 요청 시 전달값이 많을 때 /자원?키=값&키=값&키=값
	
	@PostMapping
	public ResponseEntity<?> signUp(@RequestBody @Valid MemberDTO member){
		//log.info("내가 받음? {}", member);
		memberService.signUp(member);
		return ResponseEntity.status(201).build();
	}
	
	/*
	 * 원래 비밀번호 :
	 * 바꿀 비밀번호 :
	 * 바꿀 비밀번호 확인 :
	 */
	// 비밀번호 변경 기능 구현
	@PutMapping
	public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDTO passwordEntity){
		//log.info("비밀번호 잘 넘어옴? {}", passwordEntity);
		memberService.changePassword(passwordEntity);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	//회원 탈퇴
	@DeleteMapping
	public ResponseEntity<?> deleteByPassword(@RequestBody Map<String, String> request){
		log.info("이게오나? {}", request);
		memberService.deleteByPassword(request.get("password"));
		return ResponseEntity.ok("삭제~");
	}
	
	
	
	
	
	
	
	
	
	
}
