package com.example.rockpaperscissorbackend.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.rockpaperscissorbackend.enums.Choice;
import com.example.rockpaperscissorbackend.enums.GameStatus;
import com.example.rockpaperscissorbackend.enums.Result;
import com.example.rockpaperscissorbackend.exception.GameNotFoundException;
import com.example.rockpaperscissorbackend.exception.GameOverException;
import com.example.rockpaperscissorbackend.model.Game;
import com.example.rockpaperscissorbackend.model.Round;
import com.example.rockpaperscissorbackend.repository.GameRepository;
import com.example.rockpaperscissorbackend.repository.RoundRepository;
import com.example.rockpaperscissorbackend.service.GameService;

@Service
public class GameServiceImpl implements GameService {

  private final GameRepository gameDAO;
  private final RoundRepository roundDAO;

  public GameServiceImpl(GameRepository gameDAO, RoundRepository roundDAO) {
    this.gameDAO = gameDAO;
    this.roundDAO = roundDAO;
  }

  @Override
  public Game startGame(String playerName) {
    Game gameFound = findStartedGameByPlayerName(playerName);
    if(gameFound != null) {
      return gameFound;
    } else {
      Game game = createGame(playerName);
      return gameDAO.save(game);
    }
  }

  private Game findStartedGameByPlayerName(String playerName) {
    Example<Game> example = Example.of(new Game(playerName, GameStatus.STARTED));
    return gameDAO.findOne(example).orElse(null);
  }

  @Override
  public Game getGame(Long id) throws GameNotFoundException {
    return gameDAO.findById(id)
      .orElseThrow(() -> new GameNotFoundException("Game not found"));
  }

  @Override
  public Game endGame(Long id) throws GameNotFoundException {
    Game game = getGame(id);
    game.setStatus(GameStatus.FINISHED);
    gameDAO.save(game);
    return game;
  }

  @Override
  public Game playRound(Long id, Choice playerChoice, Choice computerChoice) throws GameNotFoundException, GameOverException {
    Game game = getGame(id);
    validateGameStatus(game);
    Round currentRound = createRound(playerChoice, computerChoice, game);
    addRoundToGame(game, currentRound);
    updatePlayerScore(currentRound, game);
    return gameDAO.save(game);
  }

  private void updatePlayerScore(Round currentRound, Game game) {
    if(currentRound.getPlayerResult().equals(Result.WIN)) {
      game.setPlayerScore(game.getPlayerScore() + 1);
    }
  }

  private void addRoundToGame(Game game, Round currentRound) {
    if(CollectionUtils.isEmpty(game.getRounds())) {
      List<Round> rounds = new ArrayList<Round>();
      game.setRounds(rounds);
    }
    game.getRounds().add(currentRound);
  }

  private void validateGameStatus(Game game) throws GameOverException {
    if (!GameStatus.STARTED.equals(game.getStatus())) {
      throw new GameOverException("Game is over, please create new game");
    }
  }

  private Round createRound(Choice playerChoice, Choice computerChoice, Game game) {
    Result result = evaluatePlayerChoice(playerChoice, computerChoice);
    Round round = new Round(playerChoice, computerChoice, result, game);

    return roundDAO.save(round);
  }

  private Result evaluatePlayerChoice(Choice playerChoice, Choice computerChoice) {
    Result result = Result.DRAW;
    if (playerChoice.beats(computerChoice)) {
        result = Result.WIN;
    } else if (computerChoice.beats(playerChoice)) {
        result = Result.LOOSE;
    }

    return result;
  }

  private Game createGame(String playerName) {
    Game game = new Game(playerName);
    game.setStatus(GameStatus.STARTED);

    return game;
  }
  
}
