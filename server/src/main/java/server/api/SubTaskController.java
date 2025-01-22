package server.api;


import commons.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.SubTask;
import server.service.TaskService;
import server.service.SubTaskService;

import java.util.List;


@RestController
@RequestMapping("/api/subtasks")


public class SubTaskController {
    private final SubTaskService subTaskService;

    private final TaskService taskService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public SubTaskController(SubTaskService subTaskService, TaskService taskService, SimpMessagingTemplate simpMessagingTemplate) {
        this.subTaskService = subTaskService;
        this.taskService = taskService;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
    public List<SubTask> getAll() {
        return subTaskService.getSubTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubTask> getById(@PathVariable("id") long id) {
        if (id < 0 || !subTaskService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(subTaskService.getSubTaskById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<SubTask> add(@RequestBody SubTask subTask, @PathVariable("id") long taskId) {
        Task task = taskService.getTaskById(taskId); //have to do checks

        SubTask saved = subTaskService.addSubTask(subTask);
        task.subTasks.add(saved);
        taskService.addTask(task);

        simpMessagingTemplate.convertAndSend("/topic/boards/" + saved.parentTask.parentFelloList.parentBoard.id,
                saved.parentTask.parentFelloList.parentBoard);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SubTask> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !subTaskService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }

        SubTask ret = subTaskService.deleteSubTask(id);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + ret.parentTask.parentFelloList.parentBoard.id,
                ret.parentTask.parentFelloList.parentBoard);
        return ResponseEntity.ok(ret);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubTask> edit(@PathVariable("id") long id, @RequestBody SubTask updatedSubTask) {
        if (id < 0 || !subTaskService.exists(id)  || updatedSubTask.id != id) {
            return ResponseEntity.badRequest().build();
        }

        SubTask editedSubTask = subTaskService.addSubTask(updatedSubTask);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + editedSubTask.parentTask.parentFelloList.parentBoard.id,
                editedSubTask.parentTask.parentFelloList.parentBoard);
        return ResponseEntity.ok(editedSubTask);
    }

}
