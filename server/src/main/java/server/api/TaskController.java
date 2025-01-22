package server.api;


import commons.FelloList;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.Task;
import server.service.FelloListService;
import server.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")

public class TaskController {
    private final TaskService taskService;

    private final FelloListService felloListService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public TaskController(TaskService taskService, FelloListService felloListService, SimpMessagingTemplate simpMessagingTemplate) {
        this.taskService = taskService;
        this.felloListService = felloListService;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
    public List<Task> getAll() {
        return taskService.getTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable("id") long id) {
        if (id < 0 || !taskService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Task> add(@RequestBody Task task, @PathVariable("id") long listId) {
        if(isNullOrEmpty(task.title)){
            return ResponseEntity.badRequest().build();
        }

        FelloList list = felloListService.getList(listId); //have to do checks

        Task saved = taskService.addTask(task);
        list.tasks.add(saved);
        felloListService.addList(list);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + saved.parentFelloList.parentBoard.id, saved.parentFelloList.parentBoard);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !taskService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }

        Task ret =  taskService.deleteTask(id);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + ret.parentFelloList.parentBoard.id, ret.parentFelloList.parentBoard);
        return ResponseEntity.ok(ret);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> edit(@PathVariable("id") long id, @RequestBody Task updatedTask) {
        if (id < 0 || !taskService.exists(id)  || updatedTask.id != id) {
            return ResponseEntity.badRequest().build();
        }

        Task editedTask = taskService.addTask(updatedTask);
        //for multi board do it as : /topic/boards/board.id
        simpMessagingTemplate.convertAndSend("/topic/boards/" + editedTask.parentFelloList.parentBoard.id, editedTask.parentFelloList.parentBoard);
        return ResponseEntity.ok(editedTask);
    }

}
