package Saharni.Saharni_back.entity;

import Saharni.Saharni_back.payload.IntermediateBand;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Band extends User{

    @NotNull
    private String name;

    private String lienOutil;

    @NotNull
    private boolean verified = false;

    public Band(String email, String password, String phoneNumber, String profilePicture, String name, String lienOutil) {
        super(email, password, phoneNumber);
        this.name = name;
        this.lienOutil = lienOutil;
    }

    public Band(IntermediateBand band){
        super(band.getEmail(),band.getPassword(),band.getPhoneNumber());
        this.name = band.getName();
        this.lienOutil = band.getLienOutil();
    }


}
