package server.service;

import commons.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.BoardRepository;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * Constructor for TaskService
     *
     * @param boardRepository Repository storing the tasks
     */
    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> getBoards() {
        return boardRepository.findAll();
    }

    public boolean exists(long id) {
        return boardRepository.existsById(id);
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).get();
    }

    public Board addBoard(Board board) {
        return boardRepository.save(board);
    }

    public void deleteBoard(long id) {
        boardRepository.deleteById(id);
    }
}
