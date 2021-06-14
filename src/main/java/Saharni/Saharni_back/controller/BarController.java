package Saharni.Saharni_back.controller;

import Saharni.Saharni_back.entity.Bar;
import Saharni.Saharni_back.entity.ERole;
import Saharni.Saharni_back.entity.Role;
import Saharni.Saharni_back.repository.BarRepository;
import Saharni.Saharni_back.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/bar")
public class BarController {
    @Autowired
    BarRepository barRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody
    Optional<Bar> getBar(@PathVariable Long id) {
        Optional<Bar> bar = barRepository.findById(id);
        return bar;
    }

    @PostMapping("/add")
    public @ResponseBody Object addBar(@RequestBody Bar bar){

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_BAR));
        bar.setRoles(roles);

        return barRepository.save(bar);
    }
}
