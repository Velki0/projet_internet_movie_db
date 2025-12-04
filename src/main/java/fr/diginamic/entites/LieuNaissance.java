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

    protected LieuNaissance(String ville, String region, String pays) {

        super(region, pays);
        this.ville = ville;

    }

    public String getVille() { return ville; }

}
