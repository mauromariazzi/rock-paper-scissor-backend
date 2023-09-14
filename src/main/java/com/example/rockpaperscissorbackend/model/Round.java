package com.example.rockpaperscissorbackend.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.rockpaperscissorbackend.enums.Choice;
import com.example.rockpaperscissorbackend.enums.Result;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Round {

  @Id
  @GeneratedValue
  @JsonIgnore
  private Long id;

  private Choice playerChoice;
  private Choice computerChoice;
  private Result playerResult;

  @ManyToOne
  @JoinColumn
  @JsonIgnore
  private Game game;

  @CreatedDate
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  @Column(name = "updated_at")
  private Instant lastUpdatedAt;

  public Round(Choice playerChoice, Choice computerChoice, Result playerResult, Game game) {
    this.playerChoice = playerChoice;
    this.computerChoice = computerChoice;
    this.playerResult = playerResult;
    this.game = game;
  }
  
}
