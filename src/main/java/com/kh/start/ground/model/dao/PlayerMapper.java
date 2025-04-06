package com.kh.start.ground.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.start.ground.model.dto.PlayerDTO;

@Mapper
public interface PlayerMapper {

	@Select("SELECT PLAYER_IDX playerIdx, PLAYER_NO playerNo, PLAYER_NAME playerName, PLAYER_POSITION_NO playerPositionNo, PLAYER_POSITION playerPosition FROM TB_BOOT_PLAYER ORDER BY PLAYER_IDX ASC")
	List<PlayerDTO> getAllPlayers();
}
