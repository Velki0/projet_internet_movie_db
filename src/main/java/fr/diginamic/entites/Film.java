package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Film {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;
    @Column(name = "TITRE", nullable = false)
    private String titre;
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;
    @Column(name = "NOTE")
    private float note;
    @Column(name = "RESUME")
    private String resume;
    @Column(name = "ANNEE", nullable = false)
    private int annee;

    @ManyToOne
    @JoinColumn(name = "PAYS_ID")
    private Pays pays;

    @ManyToOne
    @JoinColumn(name = "LANGUE_ID")
    private Langue langue;

    @ManyToOne
    @JoinColumn(name = "LIEU_TOURNAGE_ID")
    private LieuTournage lieuTournage;

    @ManyToMany
    @JoinTable(name = "FILM_REALISATEUR",
            joinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "REALISATEUR_ID", referencedColumnName = "ID"))
    private Set<Realisateur> realisateurs;

    @OneToMany(mappedBy = "film")
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "FILM_GENRE",
            joinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID"))
    private Set<Genre> genres;

    public Film() {}

    @JsonCreator
    private Film(@JsonProperty("id") final String id,
                 @JsonProperty("pays") final Pays pays,
                 @JsonProperty("nom") final String titre,
                 @JsonProperty("url") final String url,
                 @JsonProperty("anneeSortie") final String anneeSortie,
                 @JsonProperty("genres") final Set<Genre> genres) {

        this.id = id;
        this.pays = pays;
        this.titre = titre;
        this.urlIMDB = url;
        this.annee =  Integer.parseInt(anneeSortie);
        this.genres = genres;

    }

    @Override
    public String toString() {
        return id + " " + pays.toString() + " " + titre + " " + urlIMDB + " " + annee + " " + genres.toString();
    }

    public Pays getPays() {
        return pays;
    }

}
