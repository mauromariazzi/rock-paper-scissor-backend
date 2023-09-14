package com.example.rockpaperscissorbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rockpaperscissorbackend.model.Round;

public interface RoundRepository extends JpaRepository<Round, Long> {
  
}
