package Saharni.Saharni_back.controller;

import Saharni.Saharni_back.entity.Bar;
import Saharni.Saharni_back.entity.Party;
import Saharni.Saharni_back.entity.Rate;
import Saharni.Saharni_back.entity.Sahar;
import Saharni.Saharni_back.repository.BarRepository;
import Saharni.Saharni_back.repository.PartyRepository;
import Saharni.Saharni_back.repository.SaharRepository;
import Saharni.Saharni_back.service.security.UserServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Saharni.Saharni_back.service.image_service.PreparePic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/party")
public class PartyController {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    BarRepository barRepository;

    @Autowired
    PreparePic preparePic;

    @Autowired
    SaharRepository saharRepository;

    @Autowired
    UserServices userServices;

    @GetMapping(value = "/", produces = "application/json")
    public @ResponseBody
    List<Party> getParties() {
        return (List<Party>) partyRepository.findAll();
    }

    @GetMapping(value = "/bar", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_BAR')")
    public @ResponseBody
    List<Party> getParty() {
        String barEmail = userServices.getCurrentUser().getUsername();
        List<Party> parties = (List<Party>) partyRepository.findAll();
        parties.removeIf(party -> !party.getBar().getEmail().equals(barEmail));
        return parties;
    }

    @PostMapping(value = "/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BAR')")
    public @ResponseBody
    Object addParty(@RequestParam("file") MultipartFile file,@RequestParam("party") String party1) {
        ObjectMapper mapper = new ObjectMapper();
        Party party = null;
        try {
            party = mapper.readValue(party1, Party.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    "something went wrong please retry!",
                    HttpStatus.BAD_REQUEST);
        }
        Bar bar = (Bar) barRepository.findByEmailIgnoreCase(userServices.getCurrentUser().getUsername());
        System.out.println(bar.getId());
        String path = preparePic.setAbsolutePath(file,"party_pictures");

        //save img in src/party_pictures
        try {
            preparePic.savePic(file, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        party.setPicture(path.substring(path.length()-8-file.getOriginalFilename().length()));
        party.setBar(bar);
        return partyRepository.save(party);
    }

    @PostMapping(value = "/rate", consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_SAHAR') or hasRole('ROLE_BAND') or hasRole('ROLE_ADMIN')")
    public @ResponseBody Object rateParty(@RequestBody Map<String, String> noteObj){

        //@RequestParam("note") float note, @RequestParam("partyId") Long partyId

        float note = Float.parseFloat(noteObj.get("note"));
        Long partyId = Long.parseLong(noteObj.get("partyId"));
        if(note > 5 || note < 0){
            return new ResponseEntity<>(
                    "rating must be between 0 and 5!",
                    HttpStatus.BAD_REQUEST
            );
        }
        Rate rate = new Rate(note);
        rate.setSahar((Sahar) saharRepository.findByEmailIgnoreCase(userServices.getCurrentUser().getUsername()));
        Optional<Party> party1 = partyRepository.findById(partyId);
        System.out.println(rate.getNote());
        if(party1.isPresent()){
            Party party = party1.get();
            if(party.getRatings() == null) party.setRatings(new ArrayList<Rate>());
            party.getRatings().add(rate);
            return partyRepository.save(party);
        }
        else return new ResponseEntity<>(
                "party does not exist!",
                HttpStatus.BAD_REQUEST
        );
    }

    //to display party pic in front end with it name
    @GetMapping("/picture/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException {
        // Load file as Resource
        Resource resource = null;
        try {
            resource = preparePic.loadFileAsResource(fileName,"party_pictures");
            if (resource==null){
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            System.out.print("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/totalrate/{id}")
    @PreAuthorize("hasRole('ROLE_SAHAR') or hasRole('ROLE_BAND') or hasRole('ROLE_ADMIN')")
    public @ResponseBody float calcTotal(@PathVariable Long id){
        Optional<Party> party = partyRepository.findById(id);
        if(party.isPresent())
            return  party.get().totalRatings();
        return 0;
    }
}
