package com.example.rockpaperscissorbackend.enums;

import lombok.Getter;

@Getter
public enum Result {
  
  WIN(0),
  LOOSE(1),
  DRAW(2);

  private Integer value;

  private Result(Integer value) {
    this.value = value;
  }
}
