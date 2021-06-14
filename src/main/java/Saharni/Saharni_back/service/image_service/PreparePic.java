package Saharni.Saharni_back.service.image_service;

import Saharni.Saharni_back.controller.AuthController;
import Saharni.Saharni_back.service.string_generator.RandomStringGenerator;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
@Configuration
public class PreparePic {
    private static String picPath= AuthController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    //TODO: Check the picture's filename to remove the absolute path
    public  String setAbsolutePath(MultipartFile file,String dir){
        String filename = RandomStringGenerator.getAlphaNumericString(8) + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String path = picPath.substring(0, picPath.length() - 15) + "src/uploads/"+dir+"/" + filename;
        return path.replace("%20", " ");
    }

    public  void savePic(MultipartFile file, String path) throws IOException {
        byte[] bytes = file.getBytes();
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(bytes);
        fos.close();
    }
    public Resource loadFileAsResource(String fileName,String dir) throws MalformedURLException, URISyntaxException {


            Path filePath = Paths.get(System.getProperty("user.dir")+"\\src\\uploads\\"+dir+"\\"+fileName);

            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            }else{
                return null;
            }

    }

}
