package ir.sbpro.springdb.modules.studios;

import ir.sbpro.springdb.modules.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<Object> registerStudio(StudioModel studio){
        EntityUtils<StudioModel> entityUtils =
                new EntityUtils<StudioModel>(studioRepository, studio);

        if(entityUtils.isDuplicate()){
            return EntityUtils.getDuplicateResponse("studio");
        }

        return new ResponseEntity<Object>(studioRepository.save(studio), HttpStatus.ACCEPTED);
    }
}
