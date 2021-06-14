package Saharni.Saharni_back.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rate")
@Getter
@Setter
@NoArgsConstructor
public class Rate {

    public Rate(float note){
        this.note = note;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private float note;

    @ManyToOne
    @JoinColumn(name = "sahar_id")
    private Sahar sahar;
}
