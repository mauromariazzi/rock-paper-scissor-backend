package com.example.rockpaperscissorbackend.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.rockpaperscissorbackend.enums.GameStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Game {
  
  @Id
  @GeneratedValue
  private Long id;

  private String playerName;
  private Integer playerScore;

  private GameStatus status;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant lastUpdatedAt;

  @OneToMany(fetch = FetchType.EAGER)
  private List<Round> rounds;

  public Game(String playerName) {
    this.playerName = playerName;
    this.playerScore = 0;
    this.status = GameStatus.STARTED;
    this.rounds = new ArrayList<Round>();
  }

  public Game(String playerName, GameStatus status) {
    this.playerName = playerName;
    this.status = status;
    this.playerScore = 0;
    this.rounds = new ArrayList<Round>();
  }

}
