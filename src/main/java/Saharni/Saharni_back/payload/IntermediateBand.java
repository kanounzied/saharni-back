package Saharni.Saharni_back.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IntermediateBand {

    String email;

    String password;

    String phoneNumber;

    private String name;

    private String lienOutil;

}
