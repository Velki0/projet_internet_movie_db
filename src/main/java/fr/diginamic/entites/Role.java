package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * La classe Role représente un rôle issu d'un film.
 */
@Entity
public class Role {

    /** Id du rôle */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Personnage qui est joué dans le film */
    @Column(name = "PERSONNAGE", nullable = false)
    private String personnage;
    /** Valeur booléenne stipulant si le rôle est principal dans le film */
    @Column(name = "ROLE_PRINCIPAL", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean rolePrincipal;

    /** Film dans lequel le rôle est joué */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILM_ID")
    private Film film;

    /** Acteur qui joue ce rôle */
    @ManyToOne
    @JoinColumn(name = "ACTEUR_ID", nullable = false)
    private Personne acteur;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Role() {}

    /**
     * Constructeur d'un rôle pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ contenu dans "rôles" du fichier .json pour le transformer en objet Java Role.
     * @param personnage Personnage joué dans le film.
     * @param acteur Acteur qui joue ce rôle.
     */
    @JsonCreator
    private Role(@JsonProperty("characterName") final String personnage,
                 @JsonProperty("acteur") final Acteur acteur) {

        this.personnage = personnage;
        this.acteur = acteur;

    }

    /**
     * Getter de l'acteur qui joue ce rôle.
      * @return L'acteur du rôle.
     */
    public Personne getActeur() { return acteur; }

    /**
     * Setter de l'acteur qui joue ce rôle.
     * @param acteur Nouvel acteur du rôle.
     */
    public void setActeur(Personne acteur) { this.acteur = acteur; }

    /**
     * Setter qui permet de stipuler si le rôle est principal dans le film.
     * @param rolePrincipal Valeur booléenne qui stipule si le rôle est principal ou non.
     */
    public void setRolePrincipal(boolean rolePrincipal) { this.rolePrincipal = rolePrincipal; }

}
