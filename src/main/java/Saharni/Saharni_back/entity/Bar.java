package Saharni.Saharni_back.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Pattern;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Bar extends User{

    public Bar(String email, String password, String barName, String phoneNumber, String profilePicture, String coverPic, String location, String openTime, String closeTime) {
        super(email, password, phoneNumber, profilePicture);
        this.barName = barName;
        this.coverPic = coverPic;
        this.location = location;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    @JsonProperty
    @NotNull
    private String barName;

    @JsonProperty
    private String coverPic;

    @JsonProperty
    @NotNull
    private String location;

    @JsonProperty
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$")
    @NotNull
    private String openTime;

    @JsonProperty
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$")
    @NotNull
    private String closeTime;

    @JsonProperty
    private Boolean firstLog = false;

    @OneToMany(mappedBy="bar")
    private List<Party> parties;
}
