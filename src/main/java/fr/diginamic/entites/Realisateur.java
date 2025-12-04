package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Map;
import java.util.Set;

@Entity
public class Realisateur extends Personne {

    @ManyToMany
    @JoinTable(name = "FILM_REALISATEUR",
            joinColumns = @JoinColumn(name = "REALISATEUR_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"))
    private Set<Film> films;

    public Realisateur() {}

    @JsonCreator
    private Realisateur(@JsonProperty("id") final String id,
                        @JsonProperty("identite") final String identite,
                        @JsonProperty("url") final String urlIMDB,
                        @JsonProperty("naissance") Map<String, String> naissance) {

        super(id, identite, urlIMDB, naissance);

    }

}
