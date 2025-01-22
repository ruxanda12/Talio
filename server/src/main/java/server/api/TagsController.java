package server.api;

import commons.Tags;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.service.TagsService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {
    private final TagsService tagsService;

    public TagsController(TagsService tagsService){
        this.tagsService = tagsService;
    }

    @GetMapping(path = {"", "/"})
    public List<Tags> getAll(){
        return tagsService.getAllTags();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tags> getById(@PathVariable("id") long id){
        if(id < 0 || !tagsService.exists(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(tagsService.getTagById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Tags> add(@RequestBody Tags tag, @PathVariable("id") long id){
        if(tag.title.isEmpty() || tag.title == null || tag.color == null){
            return ResponseEntity.badRequest().build();
        }

        Tags saved = tagsService.addTag(tag);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tags> deleteById(@PathVariable("id") long id){
        if(id < 0 || !tagsService.exists(id)){
            return ResponseEntity.badRequest().build();
        }

        Tags deleted = tagsService.deleteTag(id);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tags> edit(@PathVariable("id") long id, @RequestBody Tags updatedTag){
        if(id < 0 || !tagsService.exists(id) || updatedTag.id != id){
            return ResponseEntity.badRequest().build();
        }

        Tags editedTag = tagsService.addTag(updatedTag);
        return ResponseEntity.ok(editedTag);
    }
}
