package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Map;
import java.util.Set;

@Entity
public class Acteur extends Personne {

    @Column(name = "TAILLE")
    private String taille;

    @OneToMany(mappedBy = "acteur")
    private Set<Role> roles;

    public Acteur() {}

    @JsonCreator
    private Acteur(@JsonProperty("id") final String id,
                   @JsonProperty("identite") final String identite,
                   @JsonProperty("url") final String urlIMDB,
                   @JsonProperty("naissance") Map<String, String> naissance,
                   @JsonProperty("height") String taille) {

        super(id, identite, urlIMDB, naissance);
        this.taille = taille;

    }

}
