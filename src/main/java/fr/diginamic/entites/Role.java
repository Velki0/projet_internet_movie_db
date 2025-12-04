package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
    @Column(name = "PERSONNAGE", nullable = false)
    private String personnage;
    @Column(name = "ROLE_PRINCIPAL")
    private boolean rolePrincipal;

    @ManyToOne
    @JoinColumn(name = "FILM_ID")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "ACTEUR_ID", nullable = false)
    private Acteur acteur;

    public Role() {}

    @JsonCreator
    private Role(@JsonProperty("characterName") final String personnage,
                 @JsonProperty("acteur") final Acteur acteur) {

        this.personnage = personnage;
        this.acteur = acteur;

    }

    public Acteur getActeur() { return acteur; }

    public void setRolePrincipal(boolean rolePrincipal) { this.rolePrincipal = rolePrincipal; }

    public void assignerActeurBdD(EntityManager em) {

        EntityTransaction transaction = em.getTransaction();
        try {
            acteur = em.createQuery("FROM Acteur a WHERE a.id = :id ", Acteur.class).setParameter("id", acteur.getId()).getSingleResult();
        } catch (Exception e) {
            acteur.assignerLieuNaissanceBdD(em);
            transaction.begin();
            em.persist(acteur);
            transaction.commit();
        }

    }

}
