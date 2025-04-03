package com.kh.start.board.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.multipart.MultipartFile;

import com.kh.start.board.model.dto.BoardDTO;
import com.kh.start.board.model.vo.Board;

@Mapper
public interface BoardMapper {
	
	void save(Board board);
	
	List<BoardDTO> findAll(RowBounds rb);
	
	BoardDTO findById(Long boardNo);
	
	@Update("UPDATE TB_BOOT_BOARD SET BOARD_TITLE = #{boardTitle}, BOARD_CONTENT = #{boardContent}, BOARD_FILE_URL = #{boardFileUrl} WHERE BOARD_NO = #{boardNo}")
	BoardDTO update(BoardDTO board, MultipartFile file);
	
	void update(BoardDTO board);
	
	@Update("UPDATE TB_BOOT_BOARD SET STATUS = 'N' WHERE BOARD_NO = #{boardNo}")
	void deleteById(Long boardNo);
	
}
