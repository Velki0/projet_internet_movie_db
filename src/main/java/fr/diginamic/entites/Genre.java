package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Genre {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;

    @ManyToMany
    @JoinTable(name = "FILM_GENRE",
               joinColumns = @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID"),
               inverseJoinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"))
    private Set<Film> films;

    public Genre() {}

    @JsonCreator
    private Genre(final String nom) {

        this.nom = nom;

    }

    public String getNom() { return nom; }

}
