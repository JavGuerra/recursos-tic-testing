package com.grupo1.recursos_tic.service;

import com.grupo1.recursos_tic.model.Rating;
import com.grupo1.recursos_tic.model.Resource;
import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.repository.RatingRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepo ratingRepository;

    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    public List<Rating> findAllByResource_Id(Long id) {
        return ratingRepository.findAllByResource_Id(id);
    }

    public boolean existsById(Long Id) {
        return ratingRepository.existsById(Id);
    }

    public Optional<Rating> findById(long id) {
        return ratingRepository.findById(id);
    }

    public long count() {
        return ratingRepository.count();
    }

    // TODO revisar el uso de este método
    @Transactional
    public void deleteRatingsByUserId(Long id) {
        try {
            int deletedCount = ratingRepository.deleteRatingByUserId(id);
            System.out.println("Se han eliminado " + deletedCount + " ratings.");
            ratingRepository.deleteRatingByUserId(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar ratings", e);
        }
    }

    public void save(Rating rating) {
        ratingRepository.save(rating);
    }

    public void saveAll(List<Rating> ratings) {
        ratingRepository.saveAll(ratings);
    }

    @Transactional
    public void deleteRatingById(Long id) {
        try {
            ratingRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar ratings", e);
        }
    }

    @Transactional
    public void deleteAllByResource_Id(Long id) {
        ratingRepository.deleteAllByResource_Id(id);
    }

    @Transactional
    public void deleteAll() {
        ratingRepository.findAll().forEach(
                rating -> deleteRatingById(rating.getId())
        );
    }

}
