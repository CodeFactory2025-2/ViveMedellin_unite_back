package com.communityapp.group.request;

import com.communityapp.group.model.Group;
import com.communityapp.group.repository.GroupRepository;
import com.communityapp.group.repository.GroupMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.MeterRegistry;


import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class JoinRequestService {
    @Autowired
    private JoinRequestRepository joinRequestRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

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
   
    }

    

