package sdu.edu.kz.authcontroller.util.AppUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppUtil {
    public static String getPhotoUploadPath(String filename, Long albumId) throws IOException {
        Files.createDirectories(Paths.get("src\\main\\resources\\static\\uploads\\" + albumId));
        return new File("src\\main\\resources\\static\\uploads\\" + albumId).getAbsolutePath() + "\\" + filename;
    }
}
