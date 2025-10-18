package com.communityapp.group.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.communityapp.group.model.Group;
import com.communityapp.group.repository.GroupRepository;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group crearGrupo(Group group) {
        // Validación: no permitir crear grupos sin aceptar las reglas
        if (!group.isAceptaReglas()) {
            throw new IllegalArgumentException("El grupo no puede crearse si no se aceptan las reglas.");
        }

        // Validación: nombre de grupo duplicado
        if (groupRepository.existsByNombreGrupoIgnoreCase(group.getNombreGrupo())) {
            throw new IllegalArgumentException("Ya existe un grupo con ese nombre. Elige otro.");
        }

        // Guardar grupo en la base de datos
        return groupRepository.save(group);
    }
}
