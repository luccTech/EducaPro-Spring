package com.Spring.EducaPro.services;

import com.Spring.EducaPro.entities.User;
import com.Spring.EducaPro.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User register(User user) {
        // Verificar se o email já existe
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        // Salvar o usuário
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("E-mail ou senha inválidos!");
        }

        User user = userOptional.get();
        
        // Verificar senha (em produção, use BCrypt ou similar)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("E-mail ou senha inválidos!");
        }

        return user;
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

