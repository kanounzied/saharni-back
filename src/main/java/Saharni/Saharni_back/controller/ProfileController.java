package Saharni.Saharni_back.controller;

import Saharni.Saharni_back.entity.Bar;
import Saharni.Saharni_back.entity.Sahar;
import Saharni.Saharni_back.repository.BarRepository;
import Saharni.Saharni_back.repository.SaharRepository;
import Saharni.Saharni_back.service.image_service.PreparePic;
import Saharni.Saharni_back.service.security.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    PreparePic preparePic;
    @Autowired
    SaharRepository saharRepository;
    @Autowired
    BarRepository barRepository;
    @Autowired
    UserServices userServices;

    @GetMapping("/bar/{id}")
    public @ResponseBody
    Bar getBarProfile(@PathVariable long id) {
        Bar bar;
        bar= barRepository.findOneById(id);
        return (bar);

    }

    //route to get the logged in sahar profile
    @GetMapping("/sahar")
    public @ResponseBody
    Sahar getLoggedSaharProfile() {
        Sahar sahar;
        String currentEmail=userServices.getCurrentUser().getUsername();
        sahar= (Sahar) saharRepository.findByEmailIgnoreCase(currentEmail);
        return (sahar);

    }

    //if logged in is a bar then request this route to get profile details
    @GetMapping("/bar")
    @PreAuthorize("hasRole('ROLE_BAR')")
    public @ResponseBody
    Bar getLoggedBarProfile() {
        Bar bar;
        String currentEmail=userServices.getCurrentUser().getUsername();
        bar= (Bar) barRepository.findByEmailIgnoreCase(currentEmail);
        return (bar);
    }

    //to get profile picture url to show in front
    @GetMapping("/picture/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException {
        // Load file as Resource
        Resource resource = null;
        try {
            resource = preparePic.loadFileAsResource(fileName, "profile_pictures");
            if (resource==null){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.print("Could not determine file type.");
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
