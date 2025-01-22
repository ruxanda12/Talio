package server.service;

import commons.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TaskRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructor for TaskService
     *
     * @param taskRepository Repository storing the tasks
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public boolean exists(long id) {
        return taskRepository.existsById(id);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).get();
    }

    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    public Task deleteTask(long id) {
        Task task = taskRepository.findById(id).get();
        taskRepository.deleteById(id);
        return task;
    }

    public Task updateTask(Task updatedTask) {
        if (updatedTask == null) {
            throw new IllegalArgumentException("Invalid task or task ID");
        }

        Optional<Task> existingTaskOptional = taskRepository.findById(updatedTask.id);

        if (!existingTaskOptional.isPresent()) {
            throw new EntityNotFoundException("Task not found");
        }

        return taskRepository.save(updatedTask);
    }
}
