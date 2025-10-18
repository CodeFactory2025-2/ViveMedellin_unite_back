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

    /**
     * Endpoint para registrar un nuevo grupo en la base de datos.
     * URL: POST http://localhost:8080/api/groups
     * Ejemplo JSON:
     * {
     *   "nombreGrupo": "Amantes de la Tecnología",
     *   "descripcion": "Un grupo para hablar de innovación",
     *   "categoria": "Tecnología",
     *   "categoriaOtro": "",
     *   "privacidad": "Público",
     *   "aceptaReglas": true,
     *   "tema": "Innovación"
     * }
     */
    @PostMapping
    public ResponseEntity<?> crearGrupo(@RequestBody Group group, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        try {
            // ✅ Validaciones básicas
            if (group.getNombreGrupo() == null || group.getNombreGrupo().isBlank()) {
                response.put("error", "El nombre del grupo es obligatorio.");
                return ResponseEntity.badRequest().body(response);
            }

            if (group.getCategoria() == null || group.getCategoria().isBlank()) {
                response.put("error", "La categoría es obligatoria.");
                return ResponseEntity.badRequest().body(response);
            }

            if (group.getTema() == null || group.getTema().isBlank()) {
                response.put("error", "El tema es obligatorio.");
                return ResponseEntity.badRequest().body(response);
            }

            if (!group.isAceptaReglas()) {
                response.put("error", "Debes aceptar las reglas para crear el grupo.");
                return ResponseEntity.badRequest().body(response);
            }

            // ✅ Asignar adminId desde autenticación si está disponible
            if (authentication != null && authentication.isAuthenticated()) {
                System.out.println("Usuario autenticado: " + authentication.getName());
                // Aquí debes mapear tu objeto UserDetails para obtener el ID real
                // Por ejemplo:
                // Long usuarioId = ((CustomUserDetails) authentication.getPrincipal()).getId();
                // group.setAdminId(usuarioId);
            } else if (group.getAdminId() == null) {
                response.put("error", "No se pudo determinar el ID del administrador.");
                return ResponseEntity.badRequest().body(response);
            }

            // ✅ Guardar en base de datos
            Group nuevoGrupo = groupService.crearGrupo(group);

            response.put("mensaje", "Grupo creado exitosamente.");
            response.put("grupo", nuevoGrupo);
            return ResponseEntity.status(201).body(response);

        } catch (IllegalArgumentException e) {
            // ⚠️ Errores de validación desde el servicio
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            // ⚠️ Errores inesperados
            e.printStackTrace();
            response.put("error", "Error al crear el grupo. Intenta nuevamente más tarde.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
