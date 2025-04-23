package br.com.mowdev.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserModel dto) {

        UserModel userAlreadyExists = repository.findByUsername(dto.getUsername());

        if (userAlreadyExists != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        String passwordEncrypted = BCrypt.withDefaults().hashToString(12, dto.getPassword().toCharArray());
        dto.setPassword(passwordEncrypted);

        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(dto));
    }
}
