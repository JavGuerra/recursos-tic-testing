package com.grupo1.recursos_tic.controller;

import com.grupo1.recursos_tic.model.Rating;
import com.grupo1.recursos_tic.model.Resource;
import com.grupo1.recursos_tic.service.RatingService;
import com.grupo1.recursos_tic.service.ResourceService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ResourceApiController {

    private ResourceService resourceService;
    private RatingService ratingService;

    @GetMapping("/resources")
    @Operation(summary = "Obtener la lista de recursos",
            description = "Esta operación devuelve la lista de recursos disponibles")
    public ResponseEntity<List<Resource>> findAll() {
        return ResponseEntity.ok(resourceService.findAll());
    }

    @GetMapping("/resources/{id}")
    @Operation(summary = "Obtener detalles de un recurso",
            description = "Esta operación devuelve información detallada sobre un recurso")
    public ResponseEntity<Resource> findById(@PathVariable Long id) {
        return resourceService.findById(id)
                .map(resource -> {
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/resources/{id}/ratings")
    @Operation(summary = "Obtener valoraciones de un recurso",
            description = "Esta operación devuelve las valoraciones asociadas a un recurso")
    public ResponseEntity<List<Rating>> getRatings(@PathVariable Long id) {
        if (!resourceService.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        List<Rating> ratings = ratingService.findAllByResource_Id(id);
        if (ratings.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(ratings);
    }

    @PostMapping("/resources")
    @Operation(summary = "Crear un recurso nuevo",
            description = "Esta operación guarda un recurso nuevo y lo devuelve")
    public ResponseEntity<Resource> create(@RequestBody Resource resource) {
        if (resource.getId() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede crear un recurso con ID predefinido"
            );
        }

        try {
            Resource newResource = resourceService.save(resource);
            return ResponseEntity.status(HttpStatus.CREATED).body(newResource);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Error al crear el recurso: " + e.getMessage()
            );
        }
    }

    @PutMapping("/resources")
    @Operation(summary = "Actualizar un recurso existente",
            description = "Esta operación actualiza un recurso existente y lo devuelve")
    public ResponseEntity<Resource> update(@RequestBody Resource resource) {
        if(resource.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Resource newResource;
        try {
            newResource = resourceService.save(resource);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(newResource);
    }

    @PatchMapping(value = "/resources/{id}",
            consumes = {"application/json", "application/merge-patch+json"})
    @Operation(summary = "Actualizar un recurso existente parcialmente",
            description = "Esta operación actualiza un recurso existente parcialmente y lo devuelve")
    public ResponseEntity<Resource> partialUpdate(
            @PathVariable Long id, @RequestBody Resource resource) {
        if (id == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return resourceService.findById(id)
            .map(existingResource -> {
                Resource updateResource;
                try {
                    if (resource.getTitle() != null)
                        existingResource.setTitle(resource.getTitle());
                    if (resource.getUrl() != null)
                        existingResource.setUrl(resource.getUrl());
                    if (resource.getDescription() != null)
                        existingResource.setDescription(resource.getDescription());
                    if (resource.getAuthor() != null)
                        existingResource.setAuthor(resource.getAuthor());
                    if (resource.getType() != null)
                        existingResource.setType(resource.getType());
                    if (resource.getTags() != null)
                        existingResource.setTags(resource.getTags());
                    updateResource = resourceService.save(existingResource);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT);
                }
                return ResponseEntity.ok(updateResource); // 200
            })
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/resources/{id}")
    @Operation(summary = "Borrar un recurso existente",
            description = "Esta operación borra un recurso existente")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            resourceService.removeResourceWithDependencies(id);
            return ResponseEntity.noContent().build(); //204
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/resources/list")
    @Operation(summary = "Borrar una lista de recursos",
            description = "Esta operación borra una lista recursos existentes por su id")
    public ResponseEntity<Void> deleteAll(@RequestBody List<Long> ids) {
        try {
            resourceService.deleteAllByIdInBatch(ids);
            return ResponseEntity.noContent().build(); //204
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/resources")
    @Operation(summary = "Borrar todos los recursos",
            description = "Esta operación borra todos los recursos existentes")
    public ResponseEntity<Void> deleteAll() {
        try {
            resourceService.deleteAll();
            return ResponseEntity.noContent().build(); //204
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

}