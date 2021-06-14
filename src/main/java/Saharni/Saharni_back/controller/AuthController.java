package Saharni.Saharni_back.controller;

import Saharni.Saharni_back.entity.*;
import Saharni.Saharni_back.payloads.AuthenticationResponse;
import Saharni.Saharni_back.payloads.LoginRequest;
import Saharni.Saharni_back.repository.*;
import Saharni.Saharni_back.payload.*;
import Saharni.Saharni_back.service.password_check.PasswordRegVerif;
import Saharni.Saharni_back.service.image_service.PreparePic;
import Saharni.Saharni_back.service.security.JwtUtil;
import Saharni.Saharni_back.service.security.UserDetailsServiceImpl;
import Saharni.Saharni_back.service.security.UserServices;
import Saharni.Saharni_back.repository.BandRepository;
import Saharni.Saharni_back.repository.RoleRepository;
import Saharni.Saharni_back.repository.SaharRepository;
import Saharni.Saharni_back.repository.UserRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import Saharni.Saharni_back.entity.Bar;
import Saharni.Saharni_back.repository.BarRepository;
import Saharni.Saharni_back.config.EmailConfig;
import Saharni.Saharni_back.service.email_service.Mailer;
import Saharni.Saharni_back.service.string_generator.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    //en  bas sont des test

    @Autowired
    SaharRepository saharRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserServices userServices;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    BandRepository bandRepository;
    @Autowired
    BarRepository barRepository;
    @Autowired
    EmailConfig emailConfig;
    @Autowired
    PreparePic preparePic;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);


    //for testing
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_SAHAR')")
    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    @GetMapping("/sahars")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SAHAR')")
    public List<Sahar> getAllSahars() {
        System.out.println(userServices.getCurrentUser().getUsername());
        return (List<Sahar>) saharRepository.findAll();
    }

    @PostMapping("/signup")
    public User createUser(@RequestBody Sahar sahar) {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_SAHAR));
        sahar.setRoles(roles);
        sahar.setPassword(passwordEncoder.encode(sahar.getPassword()));
        return saharRepository.save(sahar);
    }
    //for testing fin

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AuthenticationResponse(jwt, roles, userDetails.getUsername()));
    }

    @PostMapping("/signup/sahar")
    public @ResponseBody Object createUser(@RequestParam("file") MultipartFile file, @RequestParam("user") String user) throws IOException, IOException {

        // convert string user to json format
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        try {
            IntermediateSahar intermidateSahar = mapper.readValue(user, IntermediateSahar.class);

            //verify email existance
            String email = intermidateSahar.getEmail();
            if(userRepository.findByEmailIgnoreCase(email) != null){
                return new ResponseEntity<>(
                        "Email already exist!",
                        HttpStatus.BAD_REQUEST);
            }

            //verify password regex
            if (PasswordRegVerif.validate(intermidateSahar.getPassword())) {

                //encrypt password
                intermidateSahar.setPassword(passwordEncoder.encode(intermidateSahar.getPassword()));

            //set profile picture's path and name
            String path = preparePic.setAbsolutePath(file,"profile_pictures");

            //save img in src/profile_pictures
            preparePic.savePic(file, path);

            Sahar sahar = new Sahar(intermidateSahar);
            sahar.setProfilePicture(path.substring(path.length()-8-file.getOriginalFilename().length()));

                Set<Role> roles = new HashSet<>();
                roles.add(roleRepository.findByName(ERole.ROLE_SAHAR));
                sahar.setRoles(roles);

                return saharRepository.save(sahar);
            }
            else{
                System.out.println("password not good");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(
                "Password must contain at least a digit, an upper-case letter and a lower-case letter, " +
                        "a special character '@#$%^&+=' and at least 8 characters!",
                HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/signup/band")
    public @ResponseBody Object createBand(@RequestParam("file")MultipartFile file, @RequestParam("user") String user) throws IOException {

        // convert string user to json format
        ObjectMapper mapper = new ObjectMapper();
        IntermediateBand intermidateBand = mapper.readValue(user, IntermediateBand.class);

        String email = intermidateBand.getEmail();
        //check email existance
        if(userRepository.findByEmailIgnoreCase(email) != null){
            return new ResponseEntity<>(
                    "Email already exist!",
                    HttpStatus.BAD_REQUEST);
        }

        //verify password regex
        if(PasswordRegVerif.validate(intermidateBand.getPassword())) {

            //encrypt password
            intermidateBand.setPassword(passwordEncoder.encode(intermidateBand.getPassword()));

            //set profile picture's path and name
            String path = preparePic.setAbsolutePath(file,"profile_pictures");

            //save img in src/profile_pictures
            preparePic.savePic(file, path);

            Band band = new Band(intermidateBand);
            band.setProfilePicture(path.substring(path.length()-8-file.getOriginalFilename().length()));

            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByName(ERole.ROLE_BAND));
            band.setRoles(roles);

            return bandRepository.save(band);
        }

        return new ResponseEntity<>(
                "Password must contain at least a digit, an upper-case letter and a lower-case letter, " +
                        "a special character '@#$%^&+=' and at least 8 characters!",
                HttpStatus.BAD_REQUEST);
    }

    //TODO: Check the possibility of a UNIQUE attribute
    @GetMapping("/signup/checkmail")
    public @ResponseBody Object checkEmail(@RequestParam String email){
        System.out.println(email);
        if(userRepository.findByEmailIgnoreCase(email) != null)
            return new ResponseEntity<>(
                    "email found",
                    HttpStatus.ACCEPTED);
        return new ResponseEntity<>(
                "email not found",
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/signup/mail/bar", consumes = "application/json", produces = "application/json")
    public @ResponseBody Bar mailBar(@RequestBody Map<String, String> credentials) {
        Mailer mailer = new Mailer(emailConfig);
        String password = RandomStringGenerator.getAlphaNumericString(20);
        Bar bar = new Bar(credentials.get("barEmail"), passwordEncoder.encode(password), "", "", "", "", "", "", "") ;
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(ERole.ROLE_BAR));
        bar.setRoles(roles);
        try{
            mailer.sendEmail(credentials.get("barEmail"), credentials.get("barName"), password);
            return barRepository.save(bar);
        }catch (MailException e){
            logger.info("message is " + e.getMessage());
        }
        return null;
    }

    @PostMapping(value = "/register/bar", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    Bar registerBar(@RequestBody Bar bar) {
            return barRepository.save(bar);
    }

}
