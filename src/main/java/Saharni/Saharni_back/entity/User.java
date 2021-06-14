package Saharni.Saharni_back.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Pattern;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {
    public User(String email, String password, String phoneNumber, String profilePicture) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
    }
    public User(String email, String password, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    protected String email;

    @NotNull
    @Length(min = 8)
    protected String password;

    @Pattern(regexp = "^[1-9][0-9]{7}$")
    protected String phoneNumber;

    protected String profilePicture = "";

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
}

