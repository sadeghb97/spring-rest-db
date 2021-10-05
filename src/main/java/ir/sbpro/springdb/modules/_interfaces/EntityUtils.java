package ir.sbpro.springdb.modules._interfaces;

import ir.sbpro.springdb.responses.ErrorsResponseMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class EntityUtils<U extends ModuleEntity, T extends JpaRepository<U, Long>> {
    U entity;
    T repository;
    String entityName;

    public EntityUtils(T repository, U entity, String entityName){
        this.repository = repository;
        this.entity = entity;
        this.entityName = entityName;
    }

    public boolean isDuplicate(){
        if(entity.getPrimaryKey() == null) return false;
        return repository.existsById(entity.getPrimaryKey());
    }

    public ResponseEntity<Object> patchEntity(MultipartFile file, boolean duplicateAllowed){
        boolean isDup = isDuplicate();
        if(!duplicateAllowed && isDuplicate()){
            return EntityUtils.getDuplicateResponse(entityName);
        }

        U oldEntity = null;
        if(isDup){
            oldEntity = repository.findById(entity.getPrimaryKey()).get();
        }

        try {
            if (entity instanceof HasCover && file != null && file.getSize() > 0) {
                String oldFileName = ((HasCover) entity).getCover();
                String coverPath =
                        EntityUtils.saveCover(file, oldFileName);
                ((HasCover) entity).setCover(coverPath);
            }

            boolean needRecoverPassword = isDup && entity instanceof HasPassword;
            needRecoverPassword &= ((HasPassword) entity).getPassword() == null
                    || ((HasPassword) entity).getPassword().isEmpty();

            if(needRecoverPassword){
                ((HasPassword) entity).setPassword(
                        ((HasPassword) oldEntity).getPassword()
                );
            }
        }
        catch (Exception ex){}

        return new ResponseEntity<Object>(repository.save(entity), HttpStatus.ACCEPTED);
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

    public static boolean deleteFile(String outPath, String filename) throws Exception {
        File classPath =
                ResourceUtils.getFile("classpath:static");
        File outDir = new File(classPath, outPath);

        if (filename != null && !filename.isEmpty()) {
            File oldFile = new File(outDir, filename);
            if (oldFile.exists()) return oldFile.delete();
        }

        return false;
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

    public static boolean deleteCover(String filename) throws Exception {
        return deleteFile("img/covers/", filename);
    }

    public static String saveCover(MultipartFile file, String oldFileName) throws Exception {
        return saveFile("img/covers/", file, ".jpg", oldFileName);
    }
}