package server.api;


import commons.Palette;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.service.PaletteService;

import java.util.List;

@RestController
@RequestMapping("/api/palettes")



public class PaletteController {

    private final PaletteService paletteService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public PaletteController(PaletteService paletteService, SimpMessagingTemplate simpMessagingTemplate) {
        this.paletteService = paletteService;
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    @GetMapping(path = { "", "/" })
    public List<Palette> getAll() {
        return paletteService.getPalettes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Palette> getById(@PathVariable("id") long id) {
        if (id < 0 || !paletteService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(paletteService.getPaletteById(id));
    }

    @PostMapping(path = { "", "/" })
    public ResponseEntity<Palette> add(@RequestBody Palette palette) {

        if(isNullOrEmpty(palette.title)){
            return ResponseEntity.badRequest().build();
        }

        Palette saved = paletteService.addPalette(palette);
        simpMessagingTemplate.convertAndSend("/topic/palettes/" + saved.parentBoard.id, saved.parentBoard);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Palette> update(@PathVariable("id") long id,
                                            @RequestBody Palette palette) {
        if (!paletteService.exists(id) || palette.id != id) {
            return ResponseEntity.badRequest().build();
        }

        if(isNullOrEmpty(palette.title)){
            return ResponseEntity.badRequest().build();
        }

        Palette saved = paletteService.addPalette(palette);
        simpMessagingTemplate.convertAndSend("/topic/palettes/" + saved.parentBoard.id,saved.parentBoard);
        return ResponseEntity.ok(saved);
    }

    private static boolean isNullOrEmpty(String s) {
        return (s == null || s.isEmpty());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Palette> deleteById(@PathVariable("id") long id) {
        if (id < 0 || !paletteService.exists(id)) {
            return ResponseEntity.badRequest().build();
        }

        Palette ret = paletteService.getPaletteById(id);
        paletteService.deletePalette(id);
        simpMessagingTemplate.convertAndSend("/topic/boards/" + ret.parentBoard.id, ret.parentBoard);
        return ResponseEntity.ok(ret);
    }


}