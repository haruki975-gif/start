package com.kh.start.ground.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.start.ground.model.dto.PlayerDTO;
import com.kh.start.ground.model.service.PlayerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {
	
	private final PlayerService playerService;
	
	@GetMapping
	public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
	}
}
