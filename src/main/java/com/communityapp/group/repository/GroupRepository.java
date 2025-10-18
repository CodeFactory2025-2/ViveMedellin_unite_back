package com.communityapp.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.communityapp.group.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByNombreGrupoIgnoreCase(String nombreGrupo);
}

