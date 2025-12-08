package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * La classe abstraite Lieu représente un lieu dans le monde.
 * Il est défini par un pays et une région dans ce pays.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Lieu {

    /** Id du lieu. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Région du pays où ce lieu se trouve. */
    @Column(name ="REGION")
    private String region;

    /** Pays où ce lieu se trouve. */
    @ManyToOne
    @JoinColumn(name = "PAYS_ID")
    private Pays pays;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Lieu() {}

    /**
     * Constructeur pour la création d'un nouveau lieu à partir de ses données de région et pays.
     * @param region Région où le lieu se trouve.
     * @param pays Pays dans lequel ce lieu se trouve.
     */
    protected Lieu(String region, Pays pays) {

        this.region = region;
        this.pays = pays;

    }

    /**
     * Constructeur du lieu pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ stipulant des lieux du fichier .json pour le transformer en objet Java Lieu.
     * @param region  Région précisée par le fichier source.
     * @param nomPays Pays précisé par le fichier source.
     */
    @JsonCreator
    protected Lieu(@JsonProperty("etatDept") final String region,
                   @JsonProperty("pays") final String nomPays) {

        this.region = region;
        this.pays = new Pays(nomPays);

    }

    /**
     * Getter de la région du lieu.
     * @return Région où le lieu se trouve.
     */
    public String getRegion() { return region; }

    /**
     * Getter du pays du lieu.
     * @return Pays où le lieu se trouve.
     */
    public Pays getPays() { return pays; }

    /**
     * Setter du pays du lieu.
     * @param pays Nouveau pays où le lieu se trouve.
     */
    public void setPays(Pays pays) { this.pays = pays; }

}
