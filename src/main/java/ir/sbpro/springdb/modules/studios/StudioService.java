package ir.sbpro.springdb.modules.studios;

import ir.sbpro.springdb.modules._interfaces.EntityUtils;
import ir.sbpro.springdb.modules._interfaces.ModuleEntity;
import ir.sbpro.springdb.modules._interfaces.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudioService extends ModuleService<StudioModel> {

    @Autowired
    public StudioService(StudioRepository studioRepository) {
        super(studioRepository);
    }
}
