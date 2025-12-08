package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.diginamic.utils.FormatUtils;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * La classe Pays représente un pays de production d'un film ou bien d'un pays où une personne est née.
 */
@Entity
public class Pays {

    /** L'id du pays. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Nom du pays. */
    @NaturalId
    @Column(name = "NOM", nullable = false, unique = true)
    private String nom;
    /** Url de la base de donnée mère "Internet Movie Database" du pays. */
    @Column(name = "URL_IMDB", unique = true)
    private String urlIMDB;

    /** Collection de film dont le pays a été le pays de production. */
    @OneToMany(mappedBy = "pays")
    private Set<Film> films;

    /** Collection de lieu qui se situe dans ce pays. */
    @OneToMany(mappedBy = "pays")
    private Set<Lieu> lieus;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Pays() {}

    /**
     * Constructeur permettant la création d'un nouveau pays à partir de son nom.
     * @param nom Nom du nouveau pays.
     */
    protected Pays(String nom) {

        this.nom = FormatUtils.formatterPays(nom);

    }

    /**
     * Constructeur du pays pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ précisant le nom et l'url (si existante) d'un pays du fichier .json pour le transformer en objet Java Pays.
     * @param nom Nom du nouveau pays.
     * @param urlIMDB Url du nouveau pays dans la base de donnée mère.
     */
    @JsonCreator
    private Pays(@JsonProperty("nom") final String nom,
                @JsonProperty("url") final String urlIMDB) {

        this.nom = nom;
        this.urlIMDB = urlIMDB;

    }

    /**
     * Getter du nom du pays.
     * @return Nom du pays.
     */
    public String getNom() { return nom; }

    /**
     * Getter de l'url dans la base de donnée mère du pays.
     * @return Url dans la base de données IMDB du pays.
     */
    public String getUrlIMDB() { return urlIMDB; }

    /**
     * Setter de l'url dans la base de donnée mère du pays.
     * @param urlIMDB Nouvelle url dans la base de données IMDB du pays.
     */
    public void setUrlIMDB(String urlIMDB) { this.urlIMDB = urlIMDB; }

}
