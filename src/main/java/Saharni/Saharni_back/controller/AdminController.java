package Saharni.Saharni_back.controller;

import Saharni.Saharni_back.entity.Band;
import Saharni.Saharni_back.entity.Sahar;
import Saharni.Saharni_back.entity.User;
import Saharni.Saharni_back.repository.BandRepository;
import Saharni.Saharni_back.repository.SaharRepository;
import Saharni.Saharni_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SaharRepository saharRepository;
    @Autowired
    BandRepository bandRepository;

    @GetMapping("/users/{user}")
    public List<User> getAllUsers(@PathVariable String user) {
        List<User> list = new ArrayList<User>();
        if (user.equals("all")){
            return (List<User>) userRepository.findAll();
        }
        else if (user.equals("band")) {
            List<Band> users = (List<Band>) bandRepository.findAll();
            list.addAll(users);
            return list;
        }
        else if (user.equals("sahar")) {
            List<Sahar> users = (List<Sahar>) saharRepository.findAll();
            list.addAll(users);
            return list;
        }
        else return null;
    }

    @GetMapping("/bands/{state}")
    public List<Band> getNotVerifiedBands(@PathVariable String state){
        if(state.equals("verified")) return bandRepository.findDistinctByVerified(true);
        return bandRepository.findDistinctByVerified(false);
    }

    @PostMapping("/bands/{id}")
    public @ResponseBody List<Band>
    verifyBand(@PathVariable Long id, @RequestParam boolean validate){
        Optional<Band> band = bandRepository.findById(id);
        if(band.isPresent()){
            if (validate){
                band.get().setVerified(true);
                bandRepository.save(band.get());
            }
            else
                bandRepository.delete(band.get());
        }
        return bandRepository.findDistinctByVerified(false);
    }
}
