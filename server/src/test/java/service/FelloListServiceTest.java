package service;

import commons.Board;
import commons.FelloList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.FelloListService;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class FelloListServiceTest {
    private TestFelloListRepository repo;
    private FelloListService srv;

    @BeforeEach
    public void setup() {
        repo = new TestFelloListRepository();
        srv = new FelloListService(repo);
    }

    @Test
    public void addListShouldAddNewList() {
        FelloList list = new FelloList("title", new ArrayList<>(), new Board(), "blue", "black", "red", false);
        srv.addList(list);
        assertTrue(repo.felloLists.contains(list));
    }

    @Test
    public void getAllListsShouldReturnAllLists() {
        FelloList list1 = new FelloList("title", new ArrayList<>(), new Board(), "blue", "black", "red", false);
        FelloList list2 = new FelloList("title2", new ArrayList<>(), new Board(), "blue", "black", "red", false);

        srv.addList(list1);
        srv.addList(list2);

        assertEquals(repo.felloLists, srv.getAllLists());
    }

    @Test
    public void getListShouldReturnValidListWhenGivenValidID() {
        FelloList list = new FelloList("heawer", new ArrayList<>(), new Board(), "blue", "black", "red", false);
        srv.addList(list);

        assertEquals(list, srv.getList(0));
    }

    @Test
    public void getListToGetAParticularListNotValidID(){
        assertTrue(!srv.exists(Long.MAX_VALUE));
    }

    @Test
    public void getListShouldReturnNullWhenGivenInvalidID() {
        assertThrows(NoSuchElementException.class, () -> srv.getList(Long.MAX_VALUE));
    }

    @Test
    public void deleteListShouldDeleteAList() {
        FelloList list = new FelloList("asdfadsf", new ArrayList<>(), new Board(), "blue", "black", "red", false);

        srv.addList(list);
        srv.deleteList(0);
        assertFalse(srv.exists(0));

    }
}