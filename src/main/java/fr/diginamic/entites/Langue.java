package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Langue {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;

    @OneToMany(mappedBy = "langue")
    private Set<Film> films;

    public Langue() {}

    @JsonCreator
    private Langue(final String nom) {

        this.nom = nom;

    }

    public String getNom() { return nom; }

}
