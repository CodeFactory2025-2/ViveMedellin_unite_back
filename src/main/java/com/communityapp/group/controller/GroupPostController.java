package com.communityapp.group.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.communityapp.group.model.GroupPost;
import com.communityapp.group.service.GroupPostService;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")

public class GroupPostController {

    private final GroupPostService groupPostService;

    @Autowired
    public GroupPostController(GroupPostService groupPostService) {
        this.groupPostService = groupPostService;
    }

   // Crear una publicación en un grupo 
    
    @PostMapping("/{groupId}/posts")
    public ResponseEntity<?> crearPost(@PathVariable Long groupId, @RequestBody Map<String, String> body, Principal principal) {
        Map<String, Object> response = new HashMap<>();

        try {
            String content = body.get("content");
            String username = principal.getName();

            GroupPost post = groupPostService.crearPublicacion(groupId, username, content);

            response.put("mensaje", "Publicación creada correctamente");
            response.put("post", post);
            return ResponseEntity.status(201).body(response);

        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "Error al crear la publicación.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    //Obtener todas las publicaciones de un grupo
    
    @GetMapping("/{groupId}/posts")
    public ResponseEntity<?> obtenerPosts(@PathVariable Long groupId) {
        try {
            List<GroupPost> posts = groupPostService.obtenerPublicaciones(groupId);
            return ResponseEntity.ok(posts);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    
  //Buscar publicaciones por palabra clave en un grupo
 
    @GetMapping("/{groupId}/posts/search")
    public ResponseEntity<?> buscarPosts(@PathVariable Long groupId,
                                        @org.springframework.web.bind.annotation.RequestParam String keyword) {
        try {
            List<GroupPost> resultados = groupPostService.buscarPublicaciones(groupId, keyword);
            return ResponseEntity.ok(resultados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
             e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al buscar publicaciones."));
    }
    
}
}