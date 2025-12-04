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
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Lieu {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name ="REGION")
    private String region;

    @ManyToOne
    @JoinColumn(name = "PAYS_ID")
    private Pays pays;

    public Lieu() {}

    protected Lieu(String region, Pays pays) {

        this.region = region;
        this.pays = pays;

    }

    @JsonCreator
    protected Lieu(@JsonProperty("etatDept") final String region,
                   @JsonProperty("pays") final String nomPays) {

        this.region = region;
        this.pays = new Pays(nomPays);

    }

    public String getRegion() { return region; }

    public Pays getPays() { return pays; }

    public void assignerPaysBdD(EntityManager em) {

        if (pays != null) {
            EntityTransaction transaction = em.getTransaction();
            try {
                pays = em.createQuery("FROM Pays p WHERE p.nom = :pays ", Pays.class).setParameter("pays", pays.getNom()).getSingleResult();
            } catch (Exception e) {
                transaction.begin();
                em.persist(pays);
                transaction.commit();
            }
        }

    }

}
