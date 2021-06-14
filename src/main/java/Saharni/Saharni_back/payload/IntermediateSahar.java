package Saharni.Saharni_back.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IntermediateSahar {

    String email;

    String password;

    String phoneNumber;

    Date dateDeNaissance;

    String firstname;

    String lastname;
}
