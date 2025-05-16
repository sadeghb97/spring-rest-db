package ir.sbpro.springdb.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static Boolean saveMultipartFile(MultipartFile file, File outDir, String outFn) throws Exception {
        if(outDir.exists() || outDir.mkdirs()) {
            byte[] bytes = file.getBytes();
            Files.write(Paths.get(outDir.getAbsolutePath(), outFn), bytes);
            return true;
        }

        throw new Exception("Path not found!");
    }

    public static boolean deleteFile(File outDir, String fn) throws Exception {
        if (fn != null && !fn.isEmpty()) {
            File oldFile = new File(outDir, fn);
            if (oldFile.exists()) return oldFile.delete();
        }

        return false;
    }
}
