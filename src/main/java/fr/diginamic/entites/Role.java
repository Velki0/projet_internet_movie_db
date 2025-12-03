package fr.diginamic.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Role {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "PERSONNAGE", nullable = false, unique = true)
    private String personnage;
    @Column(name = "ROLE_PRINCIPAL", nullable = false)
    private boolean rolePrincipal;

    @ManyToOne
    @JoinColumn(name = "FILM_ID", nullable = false)
    private Film film;

    @ManyToOne
    @JoinColumn(name = "ACTEUR_ID", nullable = false)
    private Acteur acteur;

    public Role() {}

}
