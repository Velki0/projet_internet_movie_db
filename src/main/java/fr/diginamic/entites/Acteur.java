package fr.diginamic.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Acteur extends Personne {

    @Column(name = "TAILLE")
    private String taille;

    @OneToMany(mappedBy = "acteur")
    private Set<Role> roles;

    public Acteur() {}

}
