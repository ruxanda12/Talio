package server.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import commons.Board;
import commons.FelloList;
import commons.Task;
import org.springframework.web.context.request.async.DeferredResult;
import server.service.BoardService;
import server.service.FelloListService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/boards")

public class BoardController {

    private final BoardService boardService;
    private final FelloListService felloListService;
    private final SimpMessagingTemplate simpmessagingTemplate;

    private Map<Object, Consumer<Board>> listeners = new HashMap<>();

    public BoardController(BoardService boardService, FelloListService felloListService, SimpMessagingTemplate simpmessagingTemplate) {
        this.boardService = boardService;
        this.felloListService = felloListService;
        this.simpmessagingTemplate = simpmessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
    public List<Board> getAll() {
        return boardService.getBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getById(@PathVariable("id") long id) {
        if (id < 0 || !boardService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Board> add(@RequestBody Board board) {

        if (isNullOrEmpty(board.title)){
            return ResponseEntity.badRequest().build();
        }

        for(FelloList list : board.felloLists){
            if(isNullOrEmpty(list.title)){
                return ResponseEntity.badRequest().build();
            }

            for(Task task : list.tasks){
                if(isNullOrEmpty(task.title)){
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        listeners.forEach((k,l) -> l.accept(board));

        Board saved = boardService.addBoard(board);
        simpmessagingTemplate.convertAndSend("/topic/boards/" + saved.id, saved);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Board> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !boardService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        Board ret = boardService.getBoardById(id);
        boardService.deleteBoard(id);
        simpmessagingTemplate.convertAndSend("/topic/boards/" + ret.id, ret);
        return ResponseEntity.ok(ret);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Board> update(@PathVariable("id") long id,
                                        @RequestBody Board board) {
        if (!boardService.exists(id) || board.id != id) {
            return ResponseEntity.badRequest().build();
        }

        if(isNullOrEmpty(board.title)){
            return ResponseEntity.badRequest().build();
        }

        board = boardService.addBoard(board);
        simpmessagingTemplate.convertAndSend("/topic/boards/" + board.id, board);
        return ResponseEntity.ok(board);
    }

    @GetMapping(path = {  "/updates" })
    public DeferredResult<ResponseEntity<Board>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Board>>(5000L, noContent);

        var key = new Object();
        listeners.put(key, b -> {
            res.setResult(ResponseEntity.ok(b));
        });

        res.onCompletion(() -> {
            listeners.remove(key);
        });

        return res;
    }
}
