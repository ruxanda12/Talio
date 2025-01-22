package server.api;


import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import commons.FelloList;
import commons.Task;
import server.service.FelloListService;

import java.util.List;

@RestController
@RequestMapping("/api/fellolists")



public class FelloListController {

    private final FelloListService felloListService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public FelloListController(FelloListService felloListService, SimpMessagingTemplate simpMessagingTemplate) {
        this.felloListService = felloListService;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
    public List<FelloList> getAll() {
        return felloListService.getAllLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FelloList> getById(@PathVariable("id") long id) {
        if (id < 0 || !felloListService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(felloListService.getList(id));
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<FelloList> add(@RequestBody FelloList felloList) {

        if(isNullOrEmpty(felloList.title)){
            return ResponseEntity.badRequest().build();
        }

        for(Task task : felloList.tasks){
            if(isNullOrEmpty(task.title)){
                return ResponseEntity.badRequest().build();
            }
        }

        FelloList saved = felloListService.addList(felloList);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + saved.parentBoard.id, saved.parentBoard);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FelloList> update(@PathVariable("id") long id,
                                            @RequestBody FelloList felloList) {
        if (!felloListService.exists(id) || felloList.id != id) {
            return ResponseEntity.badRequest().build();
        }

        if(isNullOrEmpty(felloList.title)){
            return ResponseEntity.badRequest().build();
        }

        for(Task task : felloList.tasks){
            if(isNullOrEmpty(task.title)){
                return ResponseEntity.badRequest().build();
            }
        }

        FelloList saved = felloListService.addList(felloList);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + saved.parentBoard.id,saved.parentBoard);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FelloList> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !felloListService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }

        FelloList ret = felloListService.deleteList(id);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + ret.parentBoard.id, ret.parentBoard);
        return ResponseEntity.ok(ret);
    }


}