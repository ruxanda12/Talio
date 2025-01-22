package server.service;

import commons.SubTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.SubTaskRepository;

import java.util.List;


@Service
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;

    /**
     * Constructor for TaskService
     *
     * @param subTaskRepository Repository storing the tasks
     */
    @Autowired
    public SubTaskService(SubTaskRepository subTaskRepository) {
        this.subTaskRepository = subTaskRepository;
    }

    public List<SubTask> getSubTasks() {
        return subTaskRepository.findAll();
    }

    public boolean exists(long id) {
        return subTaskRepository.existsById(id);
    }

    public SubTask getSubTaskById(Long id) {
        return subTaskRepository.findById(id).get();
    }

    public SubTask addSubTask(SubTask subTask) {
        return subTaskRepository.save(subTask);
    }

    public SubTask deleteSubTask(long id) {
        SubTask ret = subTaskRepository.findById(id).get();
        subTaskRepository.deleteById(id);
        return ret;
    }
}
