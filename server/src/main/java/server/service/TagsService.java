package server.service;

import commons.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.TagsRepository;

import java.util.List;

@Service
public class TagsService {

    private final TagsRepository tagsRepository;

    @Autowired
    public TagsService(TagsRepository tagsRepository){
        this.tagsRepository = tagsRepository;
    }

    public List<Tags> getAllTags(){
        return tagsRepository.findAll();
    }

    public boolean exists(long id){
        return tagsRepository.existsById(id);
    }

    public Tags getTagById(long id){
        return tagsRepository.findById(id).get();
    }

    public Tags addTag(Tags tag){
        return tagsRepository.save(tag);
    }

    public Tags deleteTag(long id){
        Tags tag = tagsRepository.findById(id).get();
        tagsRepository.deleteById(id);
        return tag;
    }
}
