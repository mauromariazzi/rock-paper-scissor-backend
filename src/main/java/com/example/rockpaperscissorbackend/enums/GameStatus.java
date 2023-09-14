package com.example.rockpaperscissorbackend.enums;

import lombok.Getter;

@Getter
public enum GameStatus {
  
  STARTED(0),
  FINISHED(1);

  private Integer value;

  private GameStatus(Integer value) {
    this.value = value;
  }
}
