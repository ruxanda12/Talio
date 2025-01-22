package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}
