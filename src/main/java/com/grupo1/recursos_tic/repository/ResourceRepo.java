package com.grupo1.recursos_tic.repository;

import com.grupo1.recursos_tic.model.Resource;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepo extends JpaRepository<Resource, Long> {}