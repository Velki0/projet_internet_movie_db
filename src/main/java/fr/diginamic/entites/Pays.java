package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Pays {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;

    @OneToMany(mappedBy = "pays")
    private Set<Film> films;

    @OneToMany(mappedBy = "pays")
    private Set<Lieu> lieus;

    public Pays() {}

    @JsonCreator
    private Pays(@JsonProperty("nom") final String nom,
                @JsonProperty("url") final String url) {

        this.nom = nom;
        this.urlIMDB = url;

    }

    @Override
    public String toString() {
        return "Pays{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", urlIMDB='" + urlIMDB + '\'' +
                '}';
    }

    public String getNom() {
        return nom;
    }

}
