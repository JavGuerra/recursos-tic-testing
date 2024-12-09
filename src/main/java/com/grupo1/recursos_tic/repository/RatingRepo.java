package com.grupo1.recursos_tic.repository;

import com.grupo1.recursos_tic.model.Rating;

import com.grupo1.recursos_tic.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface RatingRepo extends JpaRepository<Rating, Long> {

    @Query("select r from Rating r where r.user.id = :userId")
    List<Rating> findAllByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Rating r WHERE r.user.id = :userId")
    int deleteRatingByUserId(@Param("userId") Long userId);

    List<Rating> findAllByResource_Id(Long userId);

    void deleteAllByResource_Id(Long userId);
}

