package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * La classe Langue représente la langue d'un film.
 */
@Entity
public class Langue {

    /** L'id de la langue. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Le nom de la langue. */
    @NaturalId
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;

    /** La collection de films où cette langue est la principale. */
    @OneToMany(mappedBy = "langue")
    private Set<Film> films;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Langue() {}

    /**
     * Constructeur de la langue pour le parsing de données avec jakarta.
     * Permet de récupérer une chaine de caractères contenu dans "langue" du fichier .json pour le transformer en objet Java Langue.
     * @param nom Nom de la langue du film.
     */
    @JsonCreator
    private Langue(final String nom) {

        this.nom = nom;

    }

    /**
     * Getter du nom de la langue.
     * @return Le nom de la langue.
     */
    public String getNom() { return nom; }

}
