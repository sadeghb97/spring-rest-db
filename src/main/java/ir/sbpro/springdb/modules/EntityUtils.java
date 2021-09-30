package ir.sbpro.springdb.modules;

import ir.sbpro.springdb.responses.ErrorsResponseMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class EntityUtils<T> {
    JpaRepository<T, Long> repository;
    ModuleEntity entity;

    public EntityUtils(JpaRepository<T, Long> repository, ModuleEntity entity){
        this.repository = repository;
        this.entity = entity;
    }

    public boolean isDuplicate(){
        if(entity.getPrimaryKey() == null) return false;
        return repository.existsById(entity.getPrimaryKey());
    }

    public boolean hasPrimaryKey(){
        return entity.getPrimaryKey() != null;
    }

    public static ResponseEntity<Object> getDuplicateResponse(String entityName){
        return new ErrorsResponseMap("pk",
                "This " + entityName + " already exists!").getEntityResponse();
    }

    public static ResponseEntity<Object> entityNotFoundResponse(String entityName){
        return new ErrorsResponseMap("pk",
                entityName + " not found!").getEntityResponse();
    }

    public static String saveFile(String outPath, MultipartFile file, String defSuffix,
                                String oldFileName) throws Exception {
        File classPath =
                ResourceUtils.getFile("classpath:static");
        File outDir = new File(classPath, outPath);

        if(outDir.exists() || outDir.mkdirs()) {
            byte[] bytes = file.getBytes();
            String suffix = defSuffix;
            String orgFn = file.getOriginalFilename();
            int lastDotPos = orgFn != null ? orgFn.lastIndexOf(".") : -1;

            if (lastDotPos >= 0) {
                suffix = orgFn.substring(lastDotPos);
            }

            String name = UUID.randomUUID() + suffix;
            Files.write(Paths.get(outDir.getAbsolutePath(), name), bytes);

            if (oldFileName != null && !oldFileName.isEmpty()) {
                File oldFile = new File(outDir, oldFileName);
                if (oldFile.exists()) oldFile.delete();
            }

            return name;
        }

        throw new Exception("Path not found!");
    }
}