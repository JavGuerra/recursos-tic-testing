package com.grupo1.recursos_tic.repository;

import com.grupo1.recursos_tic.model.Resource;
import com.grupo1.recursos_tic.model.ResourceList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ResourceListsRepo extends JpaRepository<ResourceList, Long> {


    @Query("""
    select r from ResourceList r
    join fetch r.resources
    where r.id = ?1
    """)
    Optional<ResourceList> findById_Eager(Long id);

    //@Query("SELECT COUNT(*) FROM ResourceList rl WHERE rl.owner.id = ?1")
    Long countByOwner_Id(Long id);

    //@Query("SELECT rl FROM ResourceList rl WHERE rl.owner.id = ?1")
    List<ResourceList> findAllByOwner_Id(Long id);

    boolean existsByIdAndOwner_Id(Long listId, Long ownerId);

    List<ResourceList> findByOwner_IdAndResources_Id(Long ownerId, Long resourceId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ResourceList rl WHERE rl.owner.id = :id")
    void deleteResourceListByUserId(@Param("id") Long id);

    @Transactional
        //@Modifying
        //@Query("DELETE FROM ResourceList rl WHERE rl.owner.id = ?1")
    void deleteAllByOwner_Id(Long id);

}