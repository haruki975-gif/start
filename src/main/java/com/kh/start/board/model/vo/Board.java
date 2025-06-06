package com.kh.start.board.model.vo;

import java.util.Date;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Board {
	
	private Long boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriter;
	private String boardFileUrl;
	private String status;
	private Date createDate;
	private Date modifyDate;

}
