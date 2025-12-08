package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.diginamic.utils.FormatUtils;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * La classe personne représente une personne physique, elle peut être actrice, réalisatrice ou les deux.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PREMIERE_APPARITION_EN_TANT_QUE")
public class Personne {

    /** Id de la personne, il est repris à partir de l'identification de la base de données mère. */
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;
    /** Identité (nom et prénom) de la personne. */
    @Column(name = "IDENTITE", nullable = false)
    private String identite;
    /** Date de naissance de la personne. */
    @Column(name = "DATE_NAISSANCE")
    private LocalDate dateNaissance;
    /** Url de la base de donnée mère "Internet Movie Database" de la personne. */
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;

    /** Métiers de la personne. */
    @ElementCollection(targetClass = TypePersonne.class)
    @CollectionTable(name = "PERSONNE_TYPES", joinColumns = @JoinColumn(name = "ID"))
    @Column(name = "TYPES")
    @Enumerated(EnumType.STRING)
    private Set<TypePersonne> types = new HashSet<>();

    /** Lieu de naissance de la personne. */
    @ManyToOne
    @JoinColumn(name = "LIEU_NAISSANCE_ID")
    private LieuNaissance lieuNaissance;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Personne() {}

    /**
     * Constructeur de la personne pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ précisant une personne du fichier .json pour le transformer en objet Java Personne.
     * @param id Id de la personne.
     * @param identite Identité de la personne.
     * @param urlIMDB L'url de la personne dans la base de donnée mère.
     * @param naissance Les informations de naissance de la personne, regroupant la date et le lieu.
     */
    @JsonCreator
    protected Personne(@JsonProperty("id") final String id,
                       @JsonProperty("identite") final String identite,
                       @JsonProperty("url") final String urlIMDB,
                       @JsonProperty("naissance") final Map<String, String> naissance) {

        this.id = id;
        this.identite = identite;
        this.urlIMDB = urlIMDB;
        dateNaissance = FormatUtils.formatterDate(naissance.get("dateNaissance"));
        lieuNaissance = FormatUtils.formatterLieuNaissance(naissance.get("lieuNaissance"));

    }

    /**
     * Getter de l'id de la personne.
     * @return L'id de la personne.
     */
    public String getId() { return id; }

    /**
     * Getter de l'identité de la personne.
     * @return L'identité de la personne.
     */
    public String getIdentite() { return identite; }

    /**
     * Getter de l'ensemble des métiers de la personne qui peut être "acteur", "réalisateur" ou les deux.
     * @return La collection de métier de la personne.
     */
    public Set<TypePersonne> getTypes() { return types; }

    /**
     * Getter du lieu de naissance de la personne.
     * @return Le lieu de naissance de la personne.
     */
    public LieuNaissance getLieuNaissance() { return lieuNaissance; }

    /**
     * Setter du lieu de naissance de la personne.
     * @param lieuNaissance Nouveau lieu de naissance de la personne.
     */
    public void setLieuNaissance(LieuNaissance lieuNaissance) { this.lieuNaissance = lieuNaissance; }

}
