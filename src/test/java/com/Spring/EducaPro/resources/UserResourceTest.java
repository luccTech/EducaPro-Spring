package com.Spring.EducaPro.resources;

import com.Spring.EducaPro.entities.User;
import com.Spring.EducaPro.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() throws Exception {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "João Silva");
        userData.put("phone", "(71) 99999-9999");
        userData.put("educationLevel", "superior");
        userData.put("email", "joao@teste.com");
        userData.put("birthDate", "1990-01-15");
        userData.put("address", "Rua Teste, 123");
        userData.put("objective", "carreira");
        userData.put("password", "senha123");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Cadastro realizado com sucesso!"))
                .andExpect(jsonPath("$.user.email").value("joao@teste.com"));
    }

    @Test
    void testRegisterUserWithDuplicateEmail() throws Exception {
        // Criar usuário primeiro
        User user = new User();
        user.setName("Maria Silva");
        user.setPhone("(71) 88888-8888");
        user.setEducationLevel("medio");
        user.setEmail("maria@teste.com");
        user.setAddress("Rua Teste, 456");
        user.setObjective("pessoal");
        user.setPassword("senha123");
        userRepository.save(user);

        // Tentar cadastrar com mesmo email
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "João Silva");
        userData.put("phone", "(71) 99999-9999");
        userData.put("educationLevel", "superior");
        userData.put("email", "maria@teste.com");
        userData.put("birthDate", "1990-01-15");
        userData.put("address", "Rua Teste, 123");
        userData.put("objective", "carreira");
        userData.put("password", "senha123");

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userData)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("E-mail já cadastrado!"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Criar usuário primeiro
        User user = new User();
        user.setName("João Silva");
        user.setPhone("(71) 99999-9999");
        user.setEducationLevel("superior");
        user.setEmail("joao@teste.com");
        user.setAddress("Rua Teste, 123");
        user.setObjective("carreira");
        user.setPassword("senha123");
        userRepository.save(user);

        // Fazer login
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "joao@teste.com");
        credentials.put("password", "senha123");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login realizado com sucesso!"))
                .andExpect(jsonPath("$.user.email").value("joao@teste.com"));
    }

    @Test
    void testLoginWithWrongPassword() throws Exception {
        // Criar usuário primeiro
        User user = new User();
        user.setName("João Silva");
        user.setPhone("(71) 99999-9999");
        user.setEducationLevel("superior");
        user.setEmail("joao@teste.com");
        user.setAddress("Rua Teste, 123");
        user.setObjective("carreira");
        user.setPassword("senha123");
        userRepository.save(user);

        // Tentar login com senha errada
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "joao@teste.com");
        credentials.put("password", "senhaerrada");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("E-mail ou senha inválidos!"));
    }

    @Test
    void testLoginWithNonExistentEmail() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "naoexiste@teste.com");
        credentials.put("password", "senha123");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("E-mail ou senha inválidos!"));
    }
}

