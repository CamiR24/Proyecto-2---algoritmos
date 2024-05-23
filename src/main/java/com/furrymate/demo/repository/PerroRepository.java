package com.furrymate.demo.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import com.furrymate.demo.model.*;

public interface PerroRepository extends Neo4jRepository<Perro, Long> {
}
