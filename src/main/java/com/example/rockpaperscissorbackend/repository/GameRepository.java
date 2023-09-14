package com.example.rockpaperscissorbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rockpaperscissorbackend.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {

}
