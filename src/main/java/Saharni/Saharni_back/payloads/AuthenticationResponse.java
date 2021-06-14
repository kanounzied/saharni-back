package Saharni.Saharni_back.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private List<String> roles;
    private String email; //TODO: Delete after checking the front

    public AuthenticationResponse(String token, List<String> roles, String email) {
        this.token = token;
        this.roles = roles;
        this.email = email; //TODO: Delete after checking the front

    }
}
