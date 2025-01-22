package service;

import commons.Board;
import commons.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.TagsRepository;
import server.service.TagsService;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class TagsServiceTest {
    @Mock
    private TagsRepository tagsRepository;

    @InjectMocks
    private TagsService tagsService;

    private Tags tag1;
    private Tags tag2;
    private List<Tags> tags;

    private Board parentBoard;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        parentBoard = new Board("Parent", false, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),"blue",
                "blue", "blue", "blue", "blue", "blue");
        tag1 = new Tags("BLACK", "Tag 1", parentBoard);
        tag2 = new Tags("BLUE", "Tag 2", parentBoard);
        tag1.setId(1L);
        tag2.setId(2L);
        tags = new ArrayList<>();
        tags.add(tag1);
        tags.add(tag2);
    }

    @Test
    public void getAllTagsTest(){
        when(tagsRepository.findAll()).thenReturn(tags);
        List<Tags> res = tagsService.getAllTags();
        assertTrue(res.contains(tag1));
        assertTrue(res.contains(tag2));
    }

    @Test
    public void existsTest(){
        when(tagsRepository.existsById(tag1.id)).thenReturn(true);
        assertTrue(tagsService.exists(tag1.id));
        assertFalse(tagsService.exists(tag2.id));
    }

    @Test
    public void getTagByIdTest(){
        return;
    }


}
