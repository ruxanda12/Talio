package server.database;

import org.springframework.data.jpa.repository.JpaRepository;

import commons.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}