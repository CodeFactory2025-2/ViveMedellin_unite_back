package com.communityapp.group.controller;

import com.communityapp.group.model.Group;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    // Estructura temporal en memoria para simular almacenamiento de grupos
    private static final List<Group> grupos = new CopyOnWriteArrayList<>();

    private static final List<String> TEMAS_VALIDOS = Arrays.asList("arte", "cultura", "deporte", "medio ambiente", "idiomas", "educación", "otro");

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody Map<String, Object> body, Authentication authentication) {
        System.out.println("Intento de crear grupo recibido");
        try {
            // Validar autenticación
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("No autenticado");
                return ResponseEntity.status(401).body(Map.of("error", "Debes estar autenticado para crear un grupo."));
            }

            // Validar que el cuerpo no esté vacío
            if (body == null || body.isEmpty()) {
                System.out.println("Cuerpo vacío o nulo");
                return ResponseEntity.badRequest().body(Map.of("error", "El cuerpo de la solicitud no puede estar vacío y debe contener los campos requeridos."));
            }

            // Obtener campos
            String nombreGrupo = (String) body.get("nombreGrupo");
            String descripcion = (String) body.getOrDefault("descripcion", "");
            String tema = (String) body.get("tema");
            String categoriaOtro = (String) body.get("categoriaOtro");
            Object reglasObj = body.get("reglas");
            List<String> reglas = null;
            if (reglasObj instanceof List<?>) {
                reglas = ((List<?>) reglasObj).stream().map(Object::toString).toList();
            }
            String privacidad = (String) body.get("privacidad");

            // Validaciones
            if (nombreGrupo == null || nombreGrupo.isBlank() || nombreGrupo.length() > 60) {
                System.out.println("Validación fallida: nombreGrupo");
                return ResponseEntity.badRequest().body(Map.of("error", "El campo 'nombreGrupo' es obligatorio y debe tener máximo 60 caracteres."));
            }
            if (descripcion != null && descripcion.length() > 5000) {
                System.out.println("Validación fallida: descripcion");
                return ResponseEntity.badRequest().body(Map.of("error", "La descripción no puede superar los 5000 caracteres."));
            }
            if (tema == null || !TEMAS_VALIDOS.contains(tema)) {
                System.out.println("Validación fallida: tema");
                return ResponseEntity.badRequest().body(Map.of("error", "El campo 'tema' es obligatorio y debe ser uno de: " + String.join(", ", TEMAS_VALIDOS)));
            }
            if ("otro".equals(tema)) {
                if (categoriaOtro == null || categoriaOtro.isBlank() || categoriaOtro.length() > 20) {
                    System.out.println("Validación fallida: categoriaOtro");
                    return ResponseEntity.badRequest().body(Map.of("error", "Si el tema es 'otro', el campo 'categoriaOtro' es obligatorio y debe tener máximo 20 caracteres."));
                }
            }
            if (reglas == null || reglas.isEmpty()) {
                System.out.println("Validación fallida: reglas");
                return ResponseEntity.badRequest().body(Map.of("error", "Debes seleccionar al menos una regla."));
            }
            if (privacidad == null || !("publico".equals(privacidad) || "privado".equals(privacidad))) {
                System.out.println("Validación fallida: privacidad");
                return ResponseEntity.badRequest().body(Map.of("error", "El campo 'privacidad' es obligatorio y debe ser 'publico' o 'privado'."));
            }

            // Simular unicidad del nombre del grupo
            boolean existe = grupos.stream().anyMatch(g -> g.getNombreGrupo().equalsIgnoreCase(nombreGrupo));
            if (existe) {
                System.out.println("Validación fallida: unicidad nombreGrupo");
                return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un grupo con ese nombre. Elige otro nombre de grupo."));
            }

            // Crear grupo
            Group grupo = new Group();
            grupo.setNombreGrupo(nombreGrupo);
            grupo.setDescripcion(descripcion);
            grupo.setTema(tema);
            grupo.setCategoriaOtro("otro".equals(tema) ? categoriaOtro : null);
            grupo.setReglas(reglas);
            grupo.setPrivacidad(privacidad);
            grupo.setAdminId(1L); // Simulación: el usuario autenticado es admin (puedes usar el id real si lo tienes)

            grupos.add(grupo);
            System.out.println("Grupo creado correctamente");

            return ResponseEntity.status(201).body(Map.of(
                "mensaje", String.format("El grupo “%s” se ha creado correctamente.", nombreGrupo),
                "grupo", grupo
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "error", "Hubo un error técnico al procesar tu solicitud. No se pudo crear el grupo. Por favor, inténtalo nuevamente más tarde."
            ));
        }
    }
}
