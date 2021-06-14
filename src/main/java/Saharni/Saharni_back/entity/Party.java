package Saharni.Saharni_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "party")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Party {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date timing;

    private String title;

    private String place;

    private String picture = "party.jpg";

    private String description;

    private String requirements;

    private String theme;

    @JsonIgnore
    @OneToMany(mappedBy = "party")
    private List<Reservation> reservations;

    private int maxReservation;

    @ManyToOne
    @JoinColumn(name="bar_id")
    private Bar bar;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Rate> ratings;

    public float totalRatings(){
        float total =0;
        for (Rate rate : ratings) {
            total += rate.getNote();
        }
        return total/ratings.size();
    }

}
