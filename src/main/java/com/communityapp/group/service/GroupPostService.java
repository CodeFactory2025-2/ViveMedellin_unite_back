package com.communityapp.group.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.communityapp.group.model.GroupPost;
import com.communityapp.group.repository.GroupPostRepository;
import com.communityapp.group.repository.GroupRepository;
import com.communityapp.user.repository.UserRepository;
import com.communityapp.group.model.Group;
import com.communityapp.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GroupPostService {
     private final GroupPostRepository postRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupPostService(GroupPostRepository postRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

     public GroupPost crearPublicacion(Long groupId, String username, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío.");
        }

        Group grupo = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("El grupo no existe."));

        User usuario = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        GroupPost post = new GroupPost();
        post.setGroup(grupo);
        post.setAuthor(usuario);
        post.setContent(content);

        return postRepository.save(post);
    }

    public List<GroupPost> obtenerPublicaciones(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new IllegalArgumentException("El grupo no existe.");
        }
        return postRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
    }

    
}
