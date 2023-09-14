package com.example.rockpaperscissorbackend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.rockpaperscissorbackend.enums.*;
import com.example.rockpaperscissorbackend.exception.GameNotFoundException;
import com.example.rockpaperscissorbackend.exception.GameOverException;
import com.example.rockpaperscissorbackend.model.Game;
import com.example.rockpaperscissorbackend.model.Round;
import com.example.rockpaperscissorbackend.service.GameService;

@SpringBootTest
@ContextConfiguration(classes = RockPaperScissorBackendApplication.class)
@TestInstance(Lifecycle.PER_CLASS)
class RockPaperScissorBackendApplicationTests {

	@Autowired
	private GameService gameService;

	@Test
	void contextLoads() {
	}

	@BeforeAll
	public void test_startGame(@Autowired GameService gameService) {
		Game createdGame = gameService.startGame("Mauro");

		assertEquals(createdGame.getId(), Long.valueOf(1));
		assertEquals(createdGame.getPlayerName(), "Mauro");
		assertEquals(createdGame.getPlayerScore(), Integer.valueOf(0));
		assertEquals(createdGame.getStatus(), GameStatus.STARTED);
		assertTrue(createdGame.getRounds().isEmpty());
	}


	@Test
	public void test_playRound_not_existing_game() throws GameNotFoundException, GameOverException {
		GameNotFoundException exception = assertThrows(GameNotFoundException.class, () -> gameService.playRound(Long.valueOf(10), Choice.PAPER, Choice.ROCK));
		assertEquals(exception.getMessage(), "Game not found");
	}

	@Test
	public void test_playRound_win_game() throws GameNotFoundException, GameOverException {
		Game resultGame = gameService.playRound(Long.valueOf(1), Choice.PAPER, Choice.ROCK);
		Round lastRound = resultGame.getRounds().get(resultGame.getRounds().size() - 1);
		assertEquals(lastRound.getPlayerResult(), Result.WIN);
	}


	@Test
	public void test_playRound_lose_game() throws GameNotFoundException, GameOverException {
		Game resultGame = gameService.playRound(Long.valueOf(1), Choice.PAPER, Choice.SCISSOR);
		Round lastRound = resultGame.getRounds().get(resultGame.getRounds().size() - 1);
		assertEquals(lastRound.getPlayerResult(), Result.LOOSE);
	}


	@Test
	public void test_playRound_draw_game() throws GameNotFoundException, GameOverException {
		Game resultGame = gameService.playRound(Long.valueOf(1), Choice.PAPER, Choice.PAPER);
		Round lastRound = resultGame.getRounds().get(resultGame.getRounds().size() - 1);
		assertEquals(lastRound.getPlayerResult(), Result.DRAW);
	}

	@AfterAll
	public void test_endGame(@Autowired GameService gameService) throws GameNotFoundException {
		Game resultGame = gameService.endGame(Long.valueOf(1));
		assertEquals(resultGame.getStatus(), GameStatus.FINISHED);
	}
}
