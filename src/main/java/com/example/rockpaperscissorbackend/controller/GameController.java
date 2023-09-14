package com.example.rockpaperscissorbackend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.rockpaperscissorbackend.enums.Choice;
import com.example.rockpaperscissorbackend.exception.GameNotFoundException;
import com.example.rockpaperscissorbackend.exception.GameOverException;
import com.example.rockpaperscissorbackend.model.Game;
import com.example.rockpaperscissorbackend.service.GameService;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

  Logger logger = LoggerFactory.getLogger(GameController.class);

  @Autowired
  private GameService gameService;

  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public Game startGame(@RequestParam("playerName") String playerName) {
    if(playerName !=  null) {
      return gameService.startGame(playerName);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing player Name");
    }
  }
  
  @GetMapping("/{gameId}")
  @ResponseStatus(value = HttpStatus.FOUND)
  public Game getGame(@PathVariable("gameId") Long id) {
    try {
      return gameService.getGame(id);
    } catch (GameNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", e);
    }
  }

  @PutMapping("/{gameId}")
  @ResponseStatus(value = HttpStatus.OK)
  public Game endGame(@PathVariable("gameId") Long id) {
    try {
      return gameService.endGame(id);
    } catch (GameNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", e);
    }
  }

  @PostMapping("/{gameId}")
  @ResponseStatus(value = HttpStatus.CREATED)
  public Game playRound(@PathVariable("gameId") Long id,
    @RequestParam("playerChoice") Choice playerChoice) {
    Choice computerChoice = Choice.getComputerChoice();
    try {
      return gameService.playRound(id, playerChoice, computerChoice);
    } catch (GameNotFoundException | GameOverException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error playing Round", e);
    }
  }

}
