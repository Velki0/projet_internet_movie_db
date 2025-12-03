package fr.diginamic.entites;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Realisateur extends Personne {

    @ManyToMany
    @JoinTable(name = "FILM_REALISATEUR",
            joinColumns = @JoinColumn(name = "REALISATEUR_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"))
    private Set<Film> films;

    public Realisateur() {}

}
