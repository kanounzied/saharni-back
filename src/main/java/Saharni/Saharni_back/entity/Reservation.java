package Saharni.Saharni_back.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "reservation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    public Reservation(Date date) {
        this.date = date;
    }

    public Reservation(Date date, int nbSahara) {
        this.date = date;
        this.nbSahara = nbSahara;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date date;

    private int nbSahara = 1;

    @ManyToOne
    @JoinColumn(name = "sahar_id")
    private Sahar sahar;

//    @ManyToMany
//    private List<Sahar> invitedSahara;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

}
