package fr.diginamic.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personne {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private int id;
    @Column(name = "IDENTITE", nullable = false)
    private String identite;
    @Column(name = "DATE_NAISSANCE")
    private LocalDate dateNaissance;
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;

    @ManyToOne
    @JoinColumn(name = "LIEU_NAISSANCE_ID")
    private LieuNaissance lieuNaissance;

    public Personne() {}

}
