package server.service;

import commons.Palette;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.PaletteRepository;

import java.util.List;

@Service
public class PaletteService {

    private final PaletteRepository paletteRepository;

    @Autowired
    public PaletteService(PaletteRepository paletteRepository) {
        this.paletteRepository = paletteRepository;
    }

    public List<Palette> getPalettes() {
        return paletteRepository.findAll();
    }

    public boolean exists(long id) {
        return paletteRepository.existsById(id);
    }

    public Palette getPaletteById(long id) {
        return paletteRepository.findById(id).get();
    }

    public Palette addPalette(Palette palette) {
        return paletteRepository.save(palette);
    }

    public void deletePalette(long id) {
        paletteRepository.deleteById(id);
    }
}