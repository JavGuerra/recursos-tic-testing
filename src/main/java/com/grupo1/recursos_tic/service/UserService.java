package com.grupo1.recursos_tic.service;

import com.grupo1.recursos_tic.model.Rating;
import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.repository.ResourceListsRepo;
import com.grupo1.recursos_tic.repository.UserRepo;
import com.grupo1.recursos_tic.repository.RatingRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.grupo1.recursos_tic.util.Utility.stringIsEmpty;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepo userRepository;
    private ResourceListsRepo resourceListRepository;
    private RatingRepo ratingRepository;
    private PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean existsById(Long Id) {
        return userRepository.existsById(Id);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public List<Rating> findAllByUserId(User user) {
        return ratingRepository.findAllByUserId(user.getId());
    }

    public boolean existsByUsername(String username) {
        if (stringIsEmpty(username)) return false;
        return userRepository.existsByUsername(username);
    }

    public Optional<User> findByUsername(String username) {
        if (stringIsEmpty(username)) return Optional.empty();
        return userRepository.findByUsername(username);
    }

    public boolean existsByEmail(String email) {
        if (stringIsEmpty(email)) return false;
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByEmail(String email) {
        if (stringIsEmpty(email)) return Optional.empty();
        return userRepository.findByEmail(email);
    }

    public long count() {
        return userRepository.count();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    public User save2(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User already exists");
        }
        return userRepository.save(user);
    }

    public String saveWithId(Long id, User user) {
        if (!isValidId(id)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (userRepository.existsById(id)) {
            throw new IllegalArgumentException("User already exists");
        }
        userRepository.save(user);
        return "redirect:/users";
    }

    private boolean isValidId(Long id) {
        return id != null && id > 0;
    }


    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Verificar si se está actualizando la contraseña
        if (!user.getPassword().equals(existingUser.getPassword())) {
            // Desencriptar la contraseña existente
            String decryptedPassword = new String(passwordEncoder.encode(existingUser.getPassword()));

            // Verificar si la nueva contraseña es válida (por ejemplo, si coincide con la actual)
            if (!passwordEncoder.matches(user.getPassword(), decryptedPassword)) {
                // Actualizar la contraseña con el PasswordEncoder
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserWithDependencies(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            try {
                // Eliminar 'ratings' asociados al usuario
                ratingRepository.deleteRatingByUserId(userId);
                // System.out.println("Se han eliminado " + deletedCount + " ratings.");
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar ratings", e);
            }
            try {
                // Eliminar 'resourceList' asociadas al usuario
                resourceListRepository.deleteResourceListByUserId(userId);
                // System.out.println("Se han eliminado los recursos creados por el usuario a borrar.");
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar el usuario", e);
            }
            try {
                // Eliminar el usuario
                userRepository.deleteById(userId);
                System.out.println("Se ha eliminado un usuario.");
            } catch (Exception e) {
                throw new RuntimeException("Error al eliminar el usuario", e);
            }
        }
    }

    @Transactional
    public void deleteAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            int deletedCount = ratingRepository.deleteRatingByUserId(user.getId());
            System.out.println("Se han eliminado " + deletedCount + " ratings.");
            resourceListRepository.deleteResourceListByUserId(user.getId());
            System.out.println("Se han eliminado los recursos creados por el usuario a borrar.");
            userRepository.deleteById(user.getId());
            System.out.println("Se ha eliminado un usuario.");
        }
    }

}
