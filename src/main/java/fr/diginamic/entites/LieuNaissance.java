package fr.diginamic.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class LieuNaissance extends Lieu {

    @Column(name = "VILLE")
    private String ville;

    @OneToMany(mappedBy = "lieuNaissance")
    private Set<Personne> personnes;

    public LieuNaissance() {}

}
