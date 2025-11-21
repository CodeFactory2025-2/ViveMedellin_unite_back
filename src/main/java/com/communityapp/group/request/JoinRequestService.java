package com.communityapp.group.request;

import com.communityapp.group.model.Group;
import com.communityapp.group.model.GroupMember;
import com.communityapp.group.repository.GroupRepository;
import com.communityapp.group.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;


import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import javax.management.Notification;

import com.communityapp.user.model.User;
import com.communityapp.user.repository.UserRepository;

@Service
public class JoinRequestService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JoinRequestRepository joinRequestRepository;


    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    @Transactional
    public JoinRequest createJoinRequest(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado."));

        if (group.getPrivacidad() == null || !group.getPrivacidad().equalsIgnoreCase("Privado")) {
            throw new IllegalArgumentException("Solo se puede solicitar unirse a grupos privados.");
        }

        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new IllegalArgumentException("Ya eres miembro de este grupo.");
        }

        if (joinRequestRepository.existsByUserIdAndGroupIdAndStatus(userId, groupId, JoinRequestStatus.PENDING)) {
            throw new IllegalStateException("Ya tienes una solicitud pendiente para este grupo.");
        }

        JoinRequest jr = new JoinRequest();
        jr.setGroupId(groupId);
        jr.setUserId(userId);
        jr.setStatus(JoinRequestStatus.PENDING);
        jr.setCreatedAt(LocalDateTime.now());

        JoinRequest saved = joinRequestRepository.save(jr);

        if (meterRegistry != null) {
            meterRegistry.counter("join_requests_total", "status", "pending").increment();
        }

        return saved;
    }

    
    //ACEPTAR SOLICITUD DE UNIRSE A GRUPO
     
    public String acceptJoinRequest(Long userId, Long groupId) {

        // 1. Buscar la solicitud que esté PENDIENTE
        JoinRequest joinRequest = joinRequestRepository
                .findByUserIdAndGroupIdAndStatus(userId, groupId, JoinRequestStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No existe una solicitud pendiente."));

        // 2. Validar que el usuario no sea ya miembro
        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new RuntimeException("El usuario ya es miembro del grupo.");
        }

        // 3. Crear el nuevo miembro
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        GroupMember member = new GroupMember();
        member.setUserId(user.getId());
        member.setGroupId(group.getId());
        member.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(member);

        // 4. Actualizar solicitud como ACEPTADA
        joinRequest.setStatus(JoinRequestStatus.APPROVED);
        joinRequest.setRespondedAt(LocalDateTime.now());

        joinRequestRepository.save(joinRequest);

        // 5. Retornar mensaje para mostrar al administrador y notificar al usuario
        return "La solicitud fue aceptada correctamente.";
    }
}