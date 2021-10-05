package ir.sbpro.springdb.modules._interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class ModuleTemplateUtils<T extends ModuleEntity> {
    ModuleService<T> service;

    public ModuleTemplateUtils(ModuleService<T> service) {
        this.service = service;
    }

    public String getInsertRecord(T model, MultipartFile file, String success, String fail){
        ResponseEntity<Object> responseEntity = null;
        if(model.getPk() == null || model.getPk() == 0) {
            responseEntity = service.registerRecord(model, file, false);
        }
        else {
            responseEntity = service.registerRecord(model, file, true);
        }

        if (responseEntity.getBody() instanceof ModuleEntity) {
            ModuleEntity moduleEntity = (ModuleEntity) responseEntity.getBody();
            return "redirect:/" + success + "/" + moduleEntity.getPk() + "/";
        }

        return "redirect:/" + fail;
    }

    public String deleteRecord(String redirect, Long recordPk){
        Optional<T> modelOptional = service.getRecord(recordPk);

        if(modelOptional.isPresent()) {
            T model = modelOptional.get();

            if(model instanceof HasCover) {
                String coverFileName = ((HasCover) model).getCover();

                try {
                    EntityUtils.deleteCover(coverFileName);
                }catch (Exception ex){}
            }

            service.repository.deleteById(recordPk);
        }

        return "redirect:" + redirect;
    }
}
