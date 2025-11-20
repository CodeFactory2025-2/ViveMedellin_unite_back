package com.communityapp.group.request;
import com.communityapp.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class JoinRequestController {
    @Autowired
    private JoinRequestService joinRequestService;

    @PostMapping("/{groupId}/join-request")
    public ResponseEntity<?> requestToJoin(@PathVariable Long groupId, @AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no autenticado."));
        }

        Long userId = user.getId();

        try {
            joinRequestService.createJoinRequest(groupId, userId);
            return ResponseEntity.ok(Map.of(
                    "message", "Solicitud enviada exitosamente.",
                    "status", "PENDING",
                    "buttonLabel", "Solicitud enviada"
            ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Hubo un error técnico al procesar tu solicitud. No fue posible enviarla. Por favor, inténtalo nuevamente más tarde"));
        }
    }
    
}
