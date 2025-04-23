package br.com.mowdev.todolist.task;

import br.com.mowdev.todolist.user.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @JoinColumn(name = "user_id")
    private UUID idUser;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "priority")
    private String priority;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception {

        if (title.length() > 50)  {
            throw new Exception("Title length exceeds 50 characters");
        }

        this.title = title;
    }
}
