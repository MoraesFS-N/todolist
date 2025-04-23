package br.com.mowdev.todolist.task;

import br.com.mowdev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository repository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskModel dto, HttpServletRequest request) {
        dto.setIdUser(UUID.fromString(request.getAttribute("userId").toString()));

        if (validDate(dto.getStartAt(), dto.getEndAt())) {
            return ResponseEntity.status(400).body("Check body, but has one or more invalid dates");
        }

        return ResponseEntity.status(201).body(repository.save(dto));
    }

    @GetMapping
    public ResponseEntity<?> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var tasks = repository.findAllByIdUser((UUID) userId);

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> update(@PathVariable UUID taskId, @RequestBody TaskModel dto, HttpServletRequest request) {

        UUID userId = UUID.fromString(request.getAttribute("userId").toString());

        TaskModel task = repository.findById(taskId).orElse(null);

        if (task == null) {
            return ResponseEntity.status(403).body("Task not found");
        }

        if (!validOwnerTask(task, userId)) {
            return ResponseEntity.status(403).body("You are not the owner of this task");
        }

        Utils.copyNonNullProperties(dto, task);

        if (dto.getStartAt() != null && dto.getEndAt() != null) {
            if (validDate(dto.getStartAt(), dto.getEndAt())) {
                return ResponseEntity.status(400).body("Check body, but has one or more invalid dates");
            }
        }

        return ResponseEntity.ok(repository.save(task));
    }

    private boolean validDate(LocalDateTime startAt, LocalDateTime endAt) {
        var currentDate = LocalDateTime.now();

        return currentDate.isAfter(startAt) || currentDate.isAfter(endAt) || startAt.isAfter(endAt);
    }

    private boolean validOwnerTask(TaskModel task, UUID userId) {
        return task.getIdUser().equals(userId);
    }
}
