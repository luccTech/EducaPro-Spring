package com.Spring.EducaPro.resources;

import com.Spring.EducaPro.entities.User;
import com.Spring.EducaPro.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> userData) {
        try {
            User user = new User();
            user.setName((String) userData.get("name"));
            user.setPhone((String) userData.get("phone"));
            user.setEducationLevel((String) userData.get("educationLevel"));
            user.setEmail((String) userData.get("email"));
            user.setAddress((String) userData.get("address"));
            user.setObjective((String) userData.get("objective"));
            user.setPassword((String) userData.get("password"));
            
            // Processar data de nascimento
            if (userData.get("birthDate") != null) {
                String birthDateStr = (String) userData.get("birthDate");
                if (!birthDateStr.isEmpty()) {
                    try {
                        user.setBirthDate(java.time.LocalDate.parse(birthDateStr));
                    } catch (Exception e) {
                        // Se falhar, tentar converter de dd/mm/yyyy
                        user.setBirthDateFromString(birthDateStr);
                    }
                }
            }
            
            User savedUser = userService.register(user);
            
            // Não retornar a senha na resposta
            savedUser.setPassword(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Cadastro realizado com sucesso!");
            response.put("user", savedUser);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao realizar cadastro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "E-mail e senha são obrigatórios!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            User user = userService.login(email, password);
            
            // Não retornar a senha na resposta
            user.setPassword(null);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Login realizado com sucesso!");
            response.put("user", user);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao realizar login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

