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

        JoinRequest joinRequest = joinRequestRepository
                .findByUserIdAndGroupIdAndStatus(userId, groupId, JoinRequestStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("No existe una solicitud pendiente."));

        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new RuntimeException("El usuario ya es miembro del grupo.");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        GroupMember member = new GroupMember();
        member.setUserId(user.getId());
        member.setGroupId(group.getId());
        member.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(member);

        joinRequest.setStatus(JoinRequestStatus.APPROVED);
        joinRequest.setRespondedAt(LocalDateTime.now());

        joinRequestRepository.save(joinRequest);


        return "La solicitud fue aceptada correctamente.";
    }
// Rechazar SOLICITUD DE UNIRSE A GRUPO
    @Transactional
    public String rejectJoinRequest(Long requestId, Long adminId) {
        JoinRequest joinRequest = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("No existe la solicitud."));

        if (joinRequest.getStatus() != JoinRequestStatus.PENDING) {
            throw new IllegalStateException("La solicitud no está pendiente o ya fue respondida.");
        }

        Long groupId = joinRequest.getGroupId();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupo no encontrado"));


        verifyAdmin(adminId, group);

       
        joinRequest.setStatus(JoinRequestStatus.REJECTED);
        joinRequest.setRespondedAt(LocalDateTime.now());
        joinRequestRepository.save(joinRequest);

       
        return "La solicitud fue rechazada correctamente.";
    }

    private void verifyAdmin(Long adminId, Group group) {
     
        try {
            Long ownerId = group.getAdminId();
            if (ownerId == null || !ownerId.equals(adminId)) {
                throw new SecurityException("No estás autorizado para gestionar solicitudes de este grupo.");
            }
            return;
        } catch (Throwable ex) {
          
            throw new SecurityException("No se pudo verificar que seas administrador del grupo. Ajusta verifyAdmin() según tu modelo Group.");
        }
    }
}