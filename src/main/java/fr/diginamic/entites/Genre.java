package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * La classe Genre représente les genres des films.
 * Par exemple : un drame ou une comédie.
 */
@Entity
public class Genre {

    /** L'id du genre. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Le nom du genre. */
    @NaturalId
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;

    /** La collection de films possèdant ce genre. */
    @ManyToMany
    @JoinTable(name = "FILM_GENRE",
               joinColumns = @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID"),
               inverseJoinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"))
    private Set<Film> films;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Genre() {}

    /**
     * Constructeur du genre pour le parsing de données avec jakarta.
     * Permet de récupérer une chaine de caractères contenu dans "genres" du fichier .json pour le transformer en objet Java Genre.
     * @param nom Nom du genre du film.
     */
    @JsonCreator
    private Genre(final String nom) {

        this.nom = nom;

    }

    /**
     * Getter du nom du genre.
     * @return Le nom du genre.
     */
    public String getNom() { return nom; }

}
