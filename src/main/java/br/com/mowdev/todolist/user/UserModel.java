package br.com.mowdev.todolist.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name="tb_users")
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
