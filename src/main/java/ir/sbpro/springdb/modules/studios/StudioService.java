package ir.sbpro.springdb.modules.studios;

import ir.sbpro.springdb._module_interfaces.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudioService extends ModuleService<StudioModel> {

    @Autowired
    public StudioService(StudioRepository studioRepository) {
        super(studioRepository);
    }
}
