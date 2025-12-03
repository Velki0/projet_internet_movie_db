package fr.diginamic.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class LieuTournage extends Lieu {

    @Column(name = "PRECISION_ADRESSE")
    private String precisionAdresse;

    @OneToMany(mappedBy = "lieuTournage")
    private Set<Film> films;

    public LieuTournage() {}

}
