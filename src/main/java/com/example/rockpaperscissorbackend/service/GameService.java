package com.example.rockpaperscissorbackend.service;

import com.example.rockpaperscissorbackend.enums.Choice;
import com.example.rockpaperscissorbackend.exception.GameNotFoundException;
import com.example.rockpaperscissorbackend.exception.GameOverException;
import com.example.rockpaperscissorbackend.model.Game;

public interface GameService {

  Game startGame(String playerName);

  Game getGame(Long id) throws GameNotFoundException;
  
  Game endGame(Long id) throws GameNotFoundException;

  Game playRound(Long id, Choice playerChoice, Choice computerChoice) throws GameNotFoundException, GameOverException;
}
