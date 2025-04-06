package com.kh.start.ground.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kh.start.ground.model.dao.PlayerMapper;
import com.kh.start.ground.model.dto.PlayerDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

	private final PlayerMapper playerMapper;
	
	@Override
	public List<PlayerDTO> getAllPlayers() {
		return playerMapper.getAllPlayers();
	}

}
