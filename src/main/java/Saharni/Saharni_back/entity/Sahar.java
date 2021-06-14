package Saharni.Saharni_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import Saharni.Saharni_back.payload.IntermediateSahar;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sahar extends User {

    @NotNull
    @Temporal(TemporalType.DATE)
    @JsonProperty
    private Date dateDeNaissance;

    @NotNull
    @JsonProperty
    private String firstname;

    @NotNull
    @JsonProperty
    private String lastname;

    @JsonIgnore
    @OneToMany(mappedBy = "sahar")
    private List<Reservation> reservations;

//    @ManyToMany(mappedBy = "invitedSahara")
//    private List<Reservation> invites;


    public Sahar(IntermediateSahar sahar){
        super(sahar.getEmail(), sahar.getPassword(), sahar.getPhoneNumber());
        this.dateDeNaissance = sahar.getDateDeNaissance();
        this.firstname = sahar.getFirstname();
        this.lastname = sahar.getLastname();
    }

}