package com.communityapp.group.controller;

import com.communityapp.group.model.Group;
import com.communityapp.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }


    @PostMapping
    public ResponseEntity<?> crearGrupo(@RequestBody Group group, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones básicas
            if (group.getNombreGrupo() == null || group.getNombreGrupo().isBlank()) {
                response.put("Error", "El nombre del grupo es obligatorio.");
                return ResponseEntity.badRequest().body(response);
            }

            if (group.getCategoria() == null || group.getCategoria().isBlank()) {
                response.put("Error", "La categoría es obligatoria.");
                return ResponseEntity.badRequest().body(response);
            }

            if (group.getTema() == null || group.getTema().isBlank()) {
                response.put("Error", "El tema es obligatorio.");
                return ResponseEntity.badRequest().body(response);
            }

            if (!group.isAceptaReglas()) {
                response.put("Error", "Debes aceptar las reglas para crear el grupo.");
                return ResponseEntity.badRequest().body(response);
            }

            // Asignar adminId desde autenticación si está disponible
            if (authentication != null && authentication.isAuthenticated()) {
                System.out.println("Usuario autenticado: " + authentication.getName());
                // Aquí debes mapear tu objeto UserDetails para obtener el ID real
                // Por ejemplo:
                // Long usuarioId = ((CustomUserDetails) authentication.getPrincipal()).getId();
                // group.setAdminId(usuarioId);
            } else if (group.getAdminId() == null) {
                response.put("Error", "No se pudo determinar el ID del administrador.");
                return ResponseEntity.badRequest().body(response);
            }

            //  Guardar en base de datos
            Group nuevoGrupo = groupService.crearGrupo(group);

            response.put("mensaje", "Grupo creado exitosamente.");
            response.put("grupo", nuevoGrupo);
            return ResponseEntity.status(201).body(response);

        } catch (IllegalArgumentException e) {
            //  Errores de validación desde el servicio
            response.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // Errores inesperados
            e.printStackTrace();
            response.put("Error", "Error al crear el grupo. Intenta nuevamente más tarde.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

   
    // Unirse a un grupo
 
    @PostMapping("/{id}/join")
    public ResponseEntity<?> unirseAGrupo(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("Error", "Debes estar autenticado para unirte a un grupo.");
                return ResponseEntity.status(401).body(response);
            }

            String username = authentication.getName();
            Long usuarioId = groupService.obtenerIdUsuarioPorUsername(username);

         if (usuarioId == null) {
                response.put("Error", "Usuario no encontrado.");
                return ResponseEntity.badRequest().body(response);
            }

            boolean unido = groupService.unirUsuarioAGrupo(usuarioId, id);

            if (unido) {
                response.put("mensaje", "Te has unido al grupo exitosamente.");
            } else {
                response.put("mensaje", "Ya perteneces a este grupo o no se pudo completar la acción.");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Error al intentar unirse al grupo.");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    // Eliminar un grupo (solo el admin puede hacerlo)

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarGrupo(@PathVariable Long id, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                response.put("Error", "Debes estar autenticado para eliminar un grupo.");
                return ResponseEntity.status(401).body(response);
            }

            String username = authentication.getName();
            Long usuarioId = groupService.obtenerIdUsuarioPorUsername(username);

            boolean eliminado = groupService.eliminarGrupo(id, usuarioId);

            if (eliminado) {
                response.put("mensaje", "Grupo eliminado correctamente.");
                return ResponseEntity.ok(response);
            } else {
                response.put("Error", "No se pudo eliminar el grupo.");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (SecurityException e) {
            response.put("Error", e.getMessage());
            return ResponseEntity.status(403).body(response);

        } catch (IllegalArgumentException e) {
            response.put("Error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("Error", "Error al eliminar el grupo. Intenta nuevamente.");
            return ResponseEntity.internalServerError().body(response);
    }
}

}
