package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Map;
import java.util.Set;

/**
 * La classe Realisateur représente chaque réalisateur qui a participé au tournage d'un film présent dans la base de données.
 * La classe hérite directement de la classe Personne.
 */
@Entity
@DiscriminatorValue("Réalisateur")
public class Realisateur extends Personne {


     /** La collection des films dont le réalisateur a contribué au cours de sa carrière. */
    @ManyToMany
    @JoinTable(name = "FILM_REALISATEUR",
            joinColumns = @JoinColumn(name = "REALISATEUR_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"))
    private Set<Film> films;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Realisateur() {}

    /**
     * Constructeur du réalisateur pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ précisant un réalisateur du fichier .json pour le transformer en objet Java Realisateur.
     * @param id L'id du réalisateur.
     * @param identite L'identité du réalisateur.
     * @param urlIMDB L'url du réalisateur dans la base de donnée mère.
     * @param naissance Les informations de naissance du réalisateur.
     */
    @JsonCreator
    private Realisateur(@JsonProperty("id") final String id,
                        @JsonProperty("identite") final String identite,
                        @JsonProperty("url") final String urlIMDB,
                        @JsonProperty("naissance") Map<String, String> naissance) {

        super(id, identite, urlIMDB, naissance);

    }

}
