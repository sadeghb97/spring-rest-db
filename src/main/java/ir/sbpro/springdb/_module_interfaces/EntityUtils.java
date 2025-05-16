package ir.sbpro.springdb._module_interfaces;

import ir.sbpro.springdb.AppSingleton;
import ir.sbpro.springdb.modules.games.GameModel;
import ir.sbpro.springdb.responses.ErrorsResponseMap;
import ir.sbpro.springdb.utils.FileUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

public class EntityUtils<U extends ModuleEntity, T extends JpaRepository<U, Long>> {
    U entity;
    T repository;
    String entityName;

    public EntityUtils(T repository, U entity) {
        this.repository = repository;
        this.entity = entity;

        String tmp = entity.getClass().getName();
        entityName = tmp.substring(tmp.lastIndexOf(".") + 1).toLowerCase(Locale.ROOT);
    }

    public boolean isDuplicate() {
        if (entity.getPrimaryKey() == null) return false;
        return repository.existsById(entity.getPrimaryKey());
    }

    public ResponseEntity<Object> patchEntity(MultipartFile file, boolean duplicateAllowed) {
        boolean isDup = isDuplicate();
        if (!duplicateAllowed && isDuplicate()) {
            return EntityUtils.getDuplicateResponse(entityName);
        }

        U oldEntity = null;
        if (isDup) {
            oldEntity = repository.findById(entity.getPrimaryKey()).get();
        }

        try {
            boolean needRecoverPassword = isDup && entity instanceof HasPassword;
            needRecoverPassword &= ((HasPassword) entity).getPassword() == null
                    || ((HasPassword) entity).getPassword().isEmpty();

            if (needRecoverPassword) {
                ((HasPassword) entity).setPassword(
                        ((HasPassword) oldEntity).getPassword()
                );
            }
        } catch (Exception ex) {
        }

        U savedEntity = repository.save(entity);
        try {
            if (file != null && file.getSize() > 0) {
                String oldFileName = entity instanceof HasCover ? ((HasCover) entity).getCover() : null;
                String coverPath = EntityUtils.saveCover(file, oldFileName, entityName, savedEntity.pk.toString());

                if (savedEntity instanceof HasCover){
                    ((HasCover) savedEntity).setCover(coverPath);
                    savedEntity = repository.save(savedEntity);
                }
            }
        }
        catch (Exception ex){}

        if (savedEntity instanceof GameModel) {
            try {
                Long pk = savedEntity.pk;
                File outDir = new File(AppSingleton.GAME_AVATARS_ALT_PATH);
                FileUtils.saveMultipartFile(file, outDir, String.valueOf(pk));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return new ResponseEntity<Object>(savedEntity, HttpStatus.ACCEPTED);
    }

    public boolean hasPrimaryKey() {
        return entity.getPrimaryKey() != null;
    }

    public static ResponseEntity<Object> getDuplicateResponse(String entityName) {
        return new ErrorsResponseMap("pk",
                "This " + entityName + " already exists!").getEntityResponse();
    }

    public static ResponseEntity<Object> entityNotFoundResponse(String entityName) {
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

    public static String saveFile(MultipartFile file, String oldFileName, String outPath, String fn) throws Exception {
        File classPath =
                ResourceUtils.getFile("classpath:static");
        File outDir = new File(classPath, outPath);

        if (outDir.exists() || outDir.mkdirs()) {
            byte[] bytes = file.getBytes();
            Files.write(Paths.get(outDir.getAbsolutePath(), fn), bytes);

            if (oldFileName != null && !oldFileName.isEmpty()) {
                File oldFile = new File(outDir, oldFileName);
                if (oldFile.exists()) oldFile.delete();
            }

            return fn;
        }

        throw new Exception("Path not found!");
    }

    public static boolean deleteCover(String outFolder, String filename) throws Exception {
        return deleteFile("img/covers/" + outFolder, filename);
    }

    public static String saveCover(MultipartFile file, String oldFileName, String outFolder, String fn) throws Exception {
        return saveFile(file, oldFileName, "img/covers/" + outFolder, fn);
    }
}