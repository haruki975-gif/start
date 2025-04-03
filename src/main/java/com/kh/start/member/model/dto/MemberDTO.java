package com.kh.start.member.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {
	
	private Long memberNo;
	
	@Size(min=5, max=15, message="아이디는 5글자 이상 15글자 이하만 사용할 수 있습니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message="아이디는 영어/숫자만 사용할 수 있습니다.")
	@NotBlank(message="아이디는 비어있을 수 없습니다.")
	private String memberId;
	
	@Size(min=4, max=15, message="비밀번호는 5글자 이상 15글자 이하만 사용할 수 있습니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message="비밀번호는 영어/숫자만 사용할 수 있습니다.")
	@NotBlank(message="비밀번호는 비어있을 수 없습니다.")
	private String memberPw;
	
	private String memberName;
	private String role;
}
