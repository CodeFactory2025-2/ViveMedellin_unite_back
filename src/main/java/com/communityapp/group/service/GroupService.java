package com.communityapp.group.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.communityapp.group.model.Group;
import com.communityapp.group.model.GroupMember;
import com.communityapp.group.repository.GroupMemberRepository;
import com.communityapp.group.repository.GroupRepository;
import com.communityapp.user.model.User;
import com.communityapp.user.repository.UserRepository;

@Service
public class GroupService {
    
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository,
                        GroupMemberRepository groupMemberRepository,
                        UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    public Group crearGrupo(Group group) {
        if (!group.isAceptaReglas()) {
            throw new IllegalArgumentException("El grupo no puede crearse si no se aceptan las reglas.");
        }

        if (groupRepository.existsByNombreGrupoIgnoreCase(group.getNombreGrupo())) {
            throw new IllegalArgumentException("Ya existe un grupo con ese nombre. Elige otro.");
        }

        return groupRepository.save(group);
    }

    //Obtener el ID del usuario autenticado a partir del username
     
    public Long obtenerIdUsuarioPorUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));
        return user.getId();
    }

    // Permitir que un usuario se una a un grupo
    public boolean unirUsuarioAGrupo(Long usuarioId, Long grupoId) {
        if (!groupRepository.existsById(grupoId)) {
            throw new IllegalArgumentException("El grupo con ID " + grupoId + " no existe.");
        }

        boolean yaMiembro = groupMemberRepository.existsByUserIdAndGroupId(usuarioId, grupoId);
        if (yaMiembro) {
            return false;
        }

        GroupMember miembro = new GroupMember();
        miembro.setGroupId(grupoId);
        miembro.setUserId(usuarioId);
        miembro.setRole("member");
        miembro.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(miembro);
        return true;
        
    }

    // Eliminar un grupo por ID (solo el admin puede hacerlo)
    public boolean eliminarGrupo(Long grupoId, Long usuarioId) {
 
        Group grupo = groupRepository.findById(grupoId)
                .orElseThrow(() -> new IllegalArgumentException("El grupo con ID " + grupoId + " no existe."));

        if (!grupo.getAdminId().equals(usuarioId)) {
            throw new SecurityException("Solo el administrador del grupo puede eliminarlo.");
    }

   
        groupMemberRepository.deleteAll(
                groupMemberRepository.findAll()
                    .stream()
                    .filter(m -> m.getGroupId().equals(grupoId))
                    .toList()
    );


        groupRepository.delete(grupo);

        return true;
}

}