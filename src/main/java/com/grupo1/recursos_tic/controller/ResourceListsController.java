package com.grupo1.recursos_tic.controller;

import com.grupo1.recursos_tic.model.Resource;
import com.grupo1.recursos_tic.model.ResourceList;
import com.grupo1.recursos_tic.service.ResourceListsService;
import com.grupo1.recursos_tic.service.ResourceService;

import com.grupo1.recursos_tic.util.ErrMsg;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static com.grupo1.recursos_tic.util.Utility.*;
import static com.grupo1.recursos_tic.util.Utility.userAuth;

@Controller
@AllArgsConstructor
public class ResourceListsController {

    private ResourceListsService resourceListsService;
    private ResourceService resourceService;

    public String formValidation(ResourceList resourceLists) {
        if (stringIsEmpty(resourceLists.getName())) return "Falta el nombre";
        return null;
    }

    @GetMapping("resourcelists")
    public String findAll(Model model) {
        model.addAttribute("resourcelists", resourceListsService.findAllById(userAuth().get().getId()));
        return "resourcelists/list";
    }

    @GetMapping("resourcelists/{id}")
    public String findById(Model model, @PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        ResourceList resourceList = resourceListsService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        if (!Objects.equals(resourceList.getOwner().getId(), userAuth().get().getId()))
            throw new NoSuchElementException(ErrMsg.INVALID_INPUT);

        model.addAttribute("resourcelist", resourceList);
        model.addAttribute("resources", resourceList.getResources());

        return "resourcelists/detail";
    }

    @GetMapping("resourcelists/create")
    public String getFormToCreate(Model model) {
        ResourceList resourceList = new ResourceList();
        resourceList.setOwner(userAuth().get());
        model.addAttribute("resourcelist", resourceList);
        return "resourcelists/form";
    }

    @GetMapping("resourcelists/update/{id}")
    public String getFormToUpdate(Model model, @PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        ResourceList resourceList = resourceListsService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        if (!Objects.equals(resourceList.getOwner().getId(), userAuth().get().getId()))
            throw new NoSuchElementException(ErrMsg.INVALID_INPUT);

        model.addAttribute("resourcelist", resourceList);
        return "resourcelists/form";
    }

    /**
     * Procesa la petición de la vista de la lista de recursos
     * @param model Modelo
     * @param id Identificador de la lista de recursos
     * @return Vista de la lista de petición
     */
    @GetMapping("resourcelists/add/{id}")
    public String addResources(Model model, @PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        ResourceList resourceList = resourceListsService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));
        List<Resource> allResources = resourceService.findAll();
        Set<Resource> resources = resourceList.getResources();

        model.addAttribute("resourceListObject", resourceList);
        model.addAttribute("allResources", allResources);
        model.addAttribute("resources", resources);
        model.addAttribute("listId", id);
        return "resourcelists/catalog";
    }

    /**
     * Agrega recursos a una lista de recursos
     * @param resourceListObject Lista de recursos seleccionada
     * @param listId Identificador de la lista de recursos
     * @return Vista de la lista de recursos
     */
    @PostMapping("/resourcelists/add")
    public String addToList(@ModelAttribute ResourceList resourceListObject,
                            @RequestParam(required = false) Long listId) {

        ResourceList resourceList = resourceListsService.findById(resourceListObject.getId())
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        Set<Resource> newResources = resourceListObject.getResources();
        if (!newResources.isEmpty()) {
            resourceList.setResources(newResources);
            resourceListsService.save(resourceList);
        }

        return "redirect:/resourcelists/" + listId;
    }

    @GetMapping("resourcelists/remove/{listId}/{id}")
    public String removeFromList(@PathVariable Long listId, @PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0 || invalidIntPosNumber(listId) || listId == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        ResourceList resourceList = resourceListsService.findById(listId)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));
        Resource resource = resourceService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        resourceList.removeResource(resource);
        resourceListsService.save(resourceList);

        return "redirect:/resourcelists/" + listId;
    }

    @GetMapping("resourcelists/remove/{listId}")
    public String removeAll(@PathVariable Long listId) {
        if (invalidIntPosNumber(listId) || listId == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        ResourceList resourceList = resourceListsService.findById(listId)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        resourceList.setResources(new HashSet<>());
        resourceListsService.save(resourceList);

        return "redirect:/resourcelists/" + listId;
    }

    @PostMapping("resourcelists")
    public String save(@ModelAttribute ResourceList resourcelist) {
        String error = formValidation(resourcelist);
        if (error != null) throw new NoSuchElementException(error);

        if (resourcelist.getId() == null) { // crear
            resourceListsService.save(resourcelist);
            return "redirect:/resourcelists/" + resourcelist.getId();
        } else { // editar
            return resourceListsService.findById(resourcelist.getId()).map(optResourceList -> {
                BeanUtils.copyProperties(resourcelist, optResourceList);
                resourceListsService.save(optResourceList);
                return "redirect:/resourcelists/" + optResourceList.getId();
            }).orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));
        }
    }

    @GetMapping("resourcelists/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        ResourceList resourceList = resourceListsService.findById(id)
                .orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));

        if (!Objects.equals(resourceList.getOwner().getId(), userAuth().get().getId()))
            throw new NoSuchElementException(ErrMsg.INVALID_INPUT);

        try {
            resourceListsService.deleteById(resourceList.getId());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT); // 409 status().isConflict()
        }
        return "redirect:/resourcelists";
    }

    @GetMapping("resourcelists/delete")
    public String deleteAll() {
        Long id = userAuth().get().getId();
        try {
            resourceListsService.deleteAll(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT); // 409 status().isConflict()
        }
        if (resourceListsService.count(id) != 0)
            throw new RuntimeException(ErrMsg.NOT_DELETED);
        return "redirect:/resourcelists";
    }

}