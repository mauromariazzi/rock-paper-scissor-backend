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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/games")
@Tag(name = "Rock Paper Scissor Game API")
public class GameController {

  Logger logger = LoggerFactory.getLogger(GameController.class);

  @Autowired
  private GameService gameService;

  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  @Operation(summary = "Start Game", 
    description = "Returns a newly created game or the existing one if the Player had already started one")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Game started"), 
    @ApiResponse(responseCode = "404", description = "Need a playerName to start the Game")
  })
  public Game startGame(@RequestParam("playerName") 
    @Parameter(name = "playerName", description = "Name of the Player") String playerName) {
    if(playerName !=  null) {
      return gameService.startGame(playerName);
    } else {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing player Name");
    }
  }
  
  @GetMapping("/{gameId}")
  @ResponseStatus(value = HttpStatus.OK)
  @Operation(summary = "Find Game by ID", 
    description = "Returns a Game by its ID")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Game found"), 
    @ApiResponse(responseCode = "404", description = "Game not found")
  })
  public Game getGame(@PathVariable("gameId") @Parameter(name = "gameId", description = "ID of the Game") Long id) {
    try {
      return gameService.getGame(id);
    } catch (GameNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", e);
    }
  }

  @PutMapping("/{gameId}")
  @Operation(summary = "End Game", 
    description = "Changes the status of the current Game to FINISHED")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Game updated"), 
    @ApiResponse(responseCode = "404", description = "Game not found")
  })
  @ResponseStatus(value = HttpStatus.OK)
  public Game endGame(@PathVariable("gameId") @Parameter(name = "gameId", description = "ID of the Game") Long id) {
    try {
      return gameService.endGame(id);
    } catch (GameNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not found", e);
    }
  }

  @PostMapping("/{gameId}")
  @ResponseStatus(value = HttpStatus.CREATED)
  @Operation(summary = "Play Round", 
    description = "Create new Round of the current Game")
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Round created"), 
    @ApiResponse(responseCode = "404", description = "Game not found or Game already finished")
  })
  public Game playRound(@PathVariable("gameId") @Parameter(name = "gameId", description = "ID of the Game") Long id,
    @RequestParam("playerChoice") 
      @Parameter(name = "playerChoice", description = "Choice (Rock, Paper or Scissor) of the Player") Choice playerChoice) {
    Choice computerChoice = Choice.getComputerChoice();
    try {
      return gameService.playRound(id, playerChoice, computerChoice);
    } catch (GameNotFoundException | GameOverException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error playing Round", e);
    }
  }

}
