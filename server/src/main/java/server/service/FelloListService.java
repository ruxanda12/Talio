package server.service;

import commons.FelloList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.database.FelloListRepository;
import java.util.List;

@Service
public class FelloListService {

    private final FelloListRepository felloListRepository;

    @Autowired
    public FelloListService(FelloListRepository felloListRepository){
        this.felloListRepository = felloListRepository;
    }

    public FelloList addList(FelloList felloList){
        return felloListRepository.save(felloList);
    }

    public List<FelloList> getAllLists(){
        return felloListRepository.findAll();
    }
    public FelloList getList(long id){
        return felloListRepository.findById(id).get();
    }

    public FelloList deleteList(long id){
        FelloList felloList = felloListRepository.findById(id).get();
        felloListRepository.deleteById(id);
        return felloList;
    }

    public boolean exists(long id) {
        return felloListRepository.existsById(id);
    }
}
