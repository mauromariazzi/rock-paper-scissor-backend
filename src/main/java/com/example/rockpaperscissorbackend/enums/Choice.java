package com.example.rockpaperscissorbackend.enums;

import java.util.Random;

public enum Choice {

  ROCK {
    @Override
    public boolean beats(Choice choice) {
      return SCISSOR.equals(choice);
    }
  },

  PAPER {
    @Override
    public boolean beats(Choice choice) {
      return ROCK.equals(choice); 
    }
  },

  SCISSOR {
    @Override
    public boolean beats(Choice choice) {
      return PAPER.equals(choice);
    }
  };

  public static Choice getComputerChoice() {
    Integer choiceIndex = new Random().nextInt(Choice.values().length);
    return Choice.values()[choiceIndex];
  }

  public abstract boolean beats(Choice choice);
  
}
