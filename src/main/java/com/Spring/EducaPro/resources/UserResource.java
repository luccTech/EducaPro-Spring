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
            
            // Processar foto (base64)
            if (userData.get("photoUrl") != null) {
                String photoUrl = (String) userData.get("photoUrl");
                if (!photoUrl.isEmpty()) {
                    user.setPhotoUrl(photoUrl);
                }
            }
            
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
            
            // Log para debug
            System.out.println("Registro - Usuário salvo: " + savedUser.getName());
            System.out.println("Registro - photoUrl: " + (savedUser.getPhotoUrl() != null ? savedUser.getPhotoUrl().substring(0, Math.min(50, savedUser.getPhotoUrl().length())) + "..." : "null"));
            
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
            
            // Log para debug
            System.out.println("Login - Usuário encontrado: " + user.getName());
            System.out.println("Login - photoUrl: " + (user.getPhotoUrl() != null ? user.getPhotoUrl().substring(0, Math.min(50, user.getPhotoUrl().length())) + "..." : "null"));
            
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        try {
            System.out.println("=== UPDATE USER ===");
            System.out.println("ID: " + id);
            System.out.println("Dados recebidos: " + userData.keySet());
            System.out.println("photoUrl presente? " + userData.containsKey("photoUrl"));
            
            User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

            // Atualizar campos
            if (userData.get("name") != null) {
                user.setName((String) userData.get("name"));
            }
            if (userData.get("phone") != null) {
                user.setPhone((String) userData.get("phone"));
            }
            if (userData.get("educationLevel") != null) {
                user.setEducationLevel((String) userData.get("educationLevel"));
            }
            if (userData.get("address") != null) {
                user.setAddress((String) userData.get("address"));
            }
            if (userData.get("objective") != null) {
                user.setObjective((String) userData.get("objective"));
            }
            
            // Processar foto (base64)
            if (userData.containsKey("photoUrl")) {
                Object photoUrlObj = userData.get("photoUrl");
                System.out.println("photoUrl objeto recebido: " + (photoUrlObj != null ? photoUrlObj.getClass().getName() : "null"));
                
                if (photoUrlObj != null) {
                    String photoUrl = photoUrlObj.toString();
                    System.out.println("photoUrl string, tamanho: " + photoUrl.length());
                    System.out.println("photoUrl começa com: " + photoUrl.substring(0, Math.min(50, photoUrl.length())));
                    
                    if (!photoUrl.isEmpty() && !photoUrl.equals("null") && !photoUrl.equals("undefined")) {
                        System.out.println("Atualizando photoUrl no banco de dados");
                        user.setPhotoUrl(photoUrl);
                    } else {
                        System.out.println("photoUrl é vazio ou 'null', não será atualizado");
                    }
                } else {
                    System.out.println("photoUrl é null");
                }
            } else {
                System.out.println("photoUrl não foi enviado no request (chave não existe)");
            }
            
            // Processar data de nascimento
            if (userData.get("birthDate") != null) {
                String birthDateStr = (String) userData.get("birthDate");
                if (birthDateStr != null && !birthDateStr.isEmpty()) {
                    try {
                        user.setBirthDate(java.time.LocalDate.parse(birthDateStr));
                    } catch (Exception e) {
                        user.setBirthDateFromString(birthDateStr);
                    }
                }
            }

            User updatedUser = userService.update(id, user);
            updatedUser.setPassword(null); // Não retornar senha

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Perfil atualizado com sucesso!");
            response.put("user", updatedUser);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao atualizar perfil: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
            
            user.setPassword(null); // Não retornar senha

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            System.out.println("=== DELETE USER ===");
            System.out.println("ID: " + id);
            
            userService.delete(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Conta excluída com sucesso!");
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Erro ao excluir conta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

