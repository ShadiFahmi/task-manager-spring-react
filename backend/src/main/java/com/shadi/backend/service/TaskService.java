package com.shadi.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shadi.backend.exception.ResourceNotFoundException;
import com.shadi.backend.model.Task;
import com.shadi.backend.model.User;
import com.shadi.backend.repository.TaskRepository;
import com.shadi.backend.repository.UserRepository;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + id)
                );
    }

    public Task createTask(Task task) {
        Long userId = task.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId)
                );

        task.setUser(user);

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);

        Long userId = updatedTask.getUser().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId)
                );

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setUser(user);

        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        Task existingTask = getTaskById(id);

        taskRepository.delete(existingTask);
    }
}