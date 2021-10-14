package ir.sbpro.springdb._module_interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public class ModuleTemplateUtils<T extends ModuleEntity> {
    ModuleService<T> service;

    public ModuleTemplateUtils(ModuleService<T> service) {
        this.service = service;
    }

    public void bindAllRecords(Model model){
        List<T> records = service.getAllRecords();
        model.addAttribute("records", records);
    }

    public void bindPagingRecords(Model model, Pageable pageable){
        Page<T> records = service.getPagingRecords(pageable);
        model.addAttribute("records", records);
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

    public boolean bindSingleRecord(Model model, Long userPk){
        Optional<T> recordOptional = service.getRecord(userPk);
        if(recordOptional.isEmpty()) return false;

        model.addAttribute("record", recordOptional.get());
        return true;
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
