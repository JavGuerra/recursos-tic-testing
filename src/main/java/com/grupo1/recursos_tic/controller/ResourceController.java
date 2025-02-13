package com.grupo1.recursos_tic.controller;

import com.grupo1.recursos_tic.model.Resource;
import com.grupo1.recursos_tic.model.ResourceList;
import com.grupo1.recursos_tic.service.RatingService;
import com.grupo1.recursos_tic.service.ResourceListsService;
import com.grupo1.recursos_tic.service.ResourceService;

import com.grupo1.recursos_tic.util.ErrMsg;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

import static com.grupo1.recursos_tic.util.Utility.*;

@Controller
@AllArgsConstructor
@RequestMapping("resources")
public class ResourceController {

    private ResourceService resourceService;
    private ResourceListsService resourceListsService;
    private RatingService ratingService;

    public String formValidation(Resource resource) {
        if (stringIsEmpty(resource.getTitle())) return "Falta el título";
        if (stringIsEmpty(resource.getUrl())) return "Falta la URL";
        if (resource.getType() == null)
            return "Falta el tipo de recurso";
        return null;
    }

    /**
     * Muestra la lista de recursos
     * @param model Model
     * @return String
     */
    @GetMapping("")
    public String findAll(Model model) {
        model.addAttribute("resources", resourceService.findAll());
        return "resource/list";
    }

    // Probar @AuthenticationPrincipal String username
    // https://www.baeldung.com/get-user-in-spring-security
    /**
     * Muestra el recurso con el id pasado como parámetro
     * @param model Model
     * @param id Long
     * @return String
     */
    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable Long id) {
        validateId(id);

        Resource resource = resourceService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        model.addAttribute("resource", resource);
        model.addAttribute("ratings", ratingService.findAllByResource_Id(id));
        if (isAuth()) model.addAttribute("lists",
                resourceListsService.findByOwnerIdAndResourcesId(userAuth().get().getId(), id));

        return "resource/detail";
    }

    /**
     * Muestra el formulario para crear un recurso
     * @param model Model
     * @return String
     */
    @GetMapping("/create")
    public String getFormToCreate(Model model) {
        model.addAttribute("resource", new Resource());
        return "resource/form";
    }

    /**
     * Muestra el formulario para crear un recurso dentro de una lista de recursos
     * @param model Model
     * @param listId Long
     * @return String
     */
    @GetMapping("/create/{listId}")
    public String getFormToCreateNew(Model model, @PathVariable Long listId) {
        validateId(listId);

        if (!resourceListsService.existsById(listId))
            throw new NoSuchElementException(ErrMsg.NOT_FOUND);

        model.addAttribute("resource", new Resource());
        model.addAttribute("listId", listId);

        return "resource/form";
    }

    /**
     * Muestra el formulario para actualizar un recurso
     * @param model Model
     * @param id Long
     * @return String
     */
    @GetMapping("/update/{id}")
    public String getFormToUpdate(Model model, @PathVariable Long id) {
        validateId(id);

        Resource resource = resourceService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        model.addAttribute("resource", resource);

        return "resource/form";
    }

    /**
     * Muestra el formulario para actualizar un recurso dentro de una lista de recursos
     * @param model Model
     * @param id Long
     * @param listId Long
     * @return String
     */
    @GetMapping("/update/{id}/{listId}")
    public String getFormToUpdateAndList(Model model, @PathVariable Long id, @PathVariable Long listId) {
        validateId(id);
        validateId(listId);

        if (!resourceListsService.existsById(listId))
            throw new NoSuchElementException(ErrMsg.NOT_FOUND);

        Resource resource = resourceService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        model.addAttribute("resource", resource);
        model.addAttribute("listId", listId);

        return "resource/form";
    }

    /**
     * Guarda un recurso
     * @param resource Resource
     * @param listId Long
     * @return String
     */
    @PostMapping("")
    public String save(@ModelAttribute Resource resource,
                       @RequestParam(required = false) Long listId) {
        if (listId != null && listId <= 0)
                throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        String error = formValidation(resource);
        if (error != null)
            throw new ResponseStatusException(HttpStatus.CONFLICT, error);

        if (resource.getId() == null) { // crear
            Resource savedResource = resourceService.save(resource);
            if (listId != null) {
                ResourceList resourceList = resourceListsService.findById(listId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrMsg.NOT_FOUND));
                resourceList.addResource(savedResource);
                resourceListsService.save(resourceList);
                return "redirect:/resourcelists/" + listId;
            }
            return "redirect:/resources/" + savedResource.getId();
        } else { // editar
            return resourceService.findById(resource.getId()).map(optResource -> {
                BeanUtils.copyProperties(resource, optResource);
                resourceService.save(optResource);
                if (listId != null) return "redirect:/resourcelists/" + listId;
                return "redirect:/resources/" + optResource.getId();
            }).orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));
        }
    }

    /**
     * Elimina un recurso
     * @param id Long
     * @return String
     */
    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        validateId(id);

        if (!resourceService.existsById(id))
            throw new NoSuchElementException(ErrMsg.NOT_FOUND);

        try {
            resourceService.removeResourceWithDependencies(id);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Error al eliminar el recurso: " + e.getMessage());
            // 409 status().isConflict()
        }
        return "redirect:/resources";
    }

    /**
     * Elimina todos los recursos
     * @return String
     */
    @GetMapping("resources/delete")
    public String deleteAll() {
        try {
            resourceService.deleteAll();
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Error al eliminar el recurso: " + e.getMessage());
            // 409 status().isConflict()
        }
        if (resourceService.count() != 0)
            throw new RuntimeException(ErrMsg.NOT_DELETED);
        return "redirect:/resources";
    }
}