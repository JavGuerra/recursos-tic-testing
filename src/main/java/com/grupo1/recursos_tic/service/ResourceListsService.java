package com.grupo1.recursos_tic.service;

import com.grupo1.recursos_tic.model.ResourceList;
import com.grupo1.recursos_tic.repository.ResourceListsRepo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResourceListsService {

    private ResourceListsRepo resourceListsRepository;

    public List<ResourceList> findAll() {
        return resourceListsRepository.findAll();
    }

    public Optional<ResourceList> findById_Eager(Long id) {
        return resourceListsRepository.findById_Eager(id);
    }

    public List<ResourceList> findAllById(Long Id) {
        return resourceListsRepository.findAllByOwner_Id(Id);
    }

    public boolean existsById(Long Id) {
        return resourceListsRepository.existsById(Id);
    }

    public Optional<ResourceList> findById(long id) {
        return resourceListsRepository.findById(id);
    }

    public long count() {
        return resourceListsRepository.count();
    }

    public Long count(Long Id) {
        return resourceListsRepository.countByOwner_Id(Id);
    }

    public boolean existsByIdAndOwnerId(Long listId, Long ownerId) {
        return resourceListsRepository.existsByIdAndOwner_Id(listId, ownerId);
    }

    public List<ResourceList> findByOwnerIdAndResourcesId(Long userId, Long resourceId) {
        return resourceListsRepository.findByOwner_IdAndResources_Id(userId, resourceId);
    }

    public ResourceList save(ResourceList resourcelist) {
        return resourceListsRepository.save(resourcelist);
    }

    public void saveAll(List<ResourceList> resourcelists) {
        resourceListsRepository.saveAll(resourcelists);
    }

    @Transactional
    public void deleteById(long id) {
        resourceListsRepository.deleteById(id);
    }

    @Transactional
    public void deleteAll(Long Id) {
        resourceListsRepository.deleteAllByOwner_Id(Id);
    }

    @Transactional
    public void deleteAll() {
        resourceListsRepository.findAll().forEach(
                resource -> deleteById(resource.getId())
        );
    }

}
