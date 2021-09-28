package ir.sbpro.springdb.modules.studios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudioService {
    StudioRepository studioRepository;

    @Autowired
    public StudioService(StudioRepository studioRepository) {
        this.studioRepository = studioRepository;
    }

    public List<StudioModel> getAllStudios(){
        return studioRepository.findAll();
    }

    public StudioModel registerStudio(StudioModel studio){
        return studioRepository.save(studio);
    }
}
