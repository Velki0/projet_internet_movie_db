package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Map;
import java.util.Set;

/**
 * La classe Acteur représente chaque acteur qui a joué au moins une fois dans un film présent dans la base de données.
 * La classe hérite directement de la classe Personne.
 */
@Entity
@DiscriminatorValue("Acteur")
public class Acteur extends Personne {

    /** La taille de l'acteur en mètre (m). */
    @Column(name = "TAILLE")
    private String taille;

    /** La collection des rôles que l'acteur a joués au cours de sa carrière. */
    @OneToMany(mappedBy = "acteur")
    private Set<Role> roles;


    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Acteur() {}

    /**
     * Constructeur de l'acteur pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ précisant un acteur dans un rôle du fichier .json pour le transformer en objet Java Acteur.
     * @param id L'id de l'acteur.
     * @param identite L'identité de l'acteur.
     * @param urlIMDB L'url de l'acteur dans la base de donnée mère.
     * @param naissance Les informations de naissance de l'acteur.
     * @param taille La taille de l'acteur.
     */
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
