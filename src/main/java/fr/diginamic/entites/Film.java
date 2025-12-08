package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.diginamic.utils.FormatUtils;
import fr.diginamic.utils.RolesPrincipauxUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * La classe Film représente chaque film qui a été produit et listé dans le fichier source issu de IMDB.
 */
@Entity
public class Film {

    /** Id du film, il est repris à partir de l'identification de la base de données mère. */
    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;
    /** Titre du film. */
    @Column(name = "TITRE", nullable = false)
    private String titre;
    /** Url de la base de donnée mère "Internet Movie Database" du film. */
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;
    /** Note du film donnée par les critiques. */
    @Column(name = "NOTE")
    private Float note;
    /** Résumé du film. */
    @Column(name = "RESUME", length = 4000)
    private String resume;
    /** Année de sortie du film. */
    @Column(name = "ANNEE", nullable = false)
    private String annee;

    /** Pays de production du film. */
    @ManyToOne
    @JoinColumn(name = "PAYS_ID")
    private Pays pays;

    /** Langue du film. */
    @ManyToOne
    @JoinColumn(name = "LANGUE_ID")
    private Langue langue;

    /** Lieu de tournage principal du film. */
    @ManyToOne
    @JoinColumn(name = "LIEU_TOURNAGE_ID")
    private LieuTournage lieuDeTournage;

    /** Liste des réalisateurs qui ont contribué à la production du film. */
    @ManyToMany
    @JoinTable(name = "FILM_REALISATEUR",
            joinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "REALISATEUR_ID", referencedColumnName = "ID"))
    private Set<Personne> listeRealisateurs = new HashSet<>();

    /** Liste des rôles du film. */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILM_ID")
    private Set<Role> listeRoles = new HashSet<>();

    /** Liste des genres du film. */
    @ManyToMany
    @JoinTable(name = "FILM_GENRE",
            joinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID"))
    private Set<Genre> listeGenres = new HashSet<>();

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public Film() {}

    /**
     * Constructeur du film pour le parsing de données avec jakarta.
     * Permet de récupérer chaque champ du fichier .json pour le transformer en objet Java Film.
     * @param id Id du film.
     * @param titre Titre du film.
     * @param urlIMDB L'url du film dans la base de donnée mère.
     * @param note Note du film.
     * @param resume Résumé du film.
     * @param langue Langue du film.
     * @param pays Pays de production du film.
     * @param lieuTournage Lieu de tournage principal du film.
     * @param realisateurs Liste des réalisateurs du film.
     * @param anneeSortie Année de sortie du film.
     * @param roles Liste des rôles du film.
     * @param genres Liste des genres du film.
     * @param artistesPrincipaux Liste des artistes principaux du film.
     */
    @JsonCreator
    private Film(@JsonProperty("id") final String id,
                 @JsonProperty("nom") final String titre,
                 @JsonProperty("url") final String urlIMDB,
                 @JsonProperty("rating") final String note,
                 @JsonProperty("plot") final String resume,
                 @JsonProperty("langue") final Langue langue,
                 @JsonProperty("pays") final Pays pays,
                 @JsonProperty("lieuTournage") final LieuTournage lieuTournage,
                 @JsonProperty("realisateurs") final Set<Realisateur> realisateurs,
                 @JsonProperty("anneeSortie") final String anneeSortie,
                 @JsonProperty("roles") final Set<Role> roles,
                 @JsonProperty("genres") final Set<Genre> genres,
                 @JsonProperty("castingPrincipal") final List<CastingPrincipal> artistesPrincipaux) {

        this.id = id;
        this.titre = titre;
        this.urlIMDB = urlIMDB;
        this.note = FormatUtils.formatterNote(note);
        this.resume = resume;
        this.langue = langue;
        this.pays = pays;
        lieuDeTournage = lieuTournage;
        if (realisateurs != null) { listeRealisateurs.addAll(realisateurs); }
        annee =  anneeSortie;
        listeRoles = roles;
        listeGenres = genres;
        RolesPrincipauxUtils.assigner(this, artistesPrincipaux);

    }

    /**
     * Getter de l'id du film.
     * @return L'id du film.
     */
    public String getId() { return id; }

    /**
     * Getter du titre du film.
     * @return Le titre du film.
     */
    public String getTitre() { return titre; }

    /**
     * Getter du pays du film.
     * @return Le pays de production du film.
     */
    public Pays getPays() { return pays; }

    /**
     * Getter de la langue du film.
     * @return La langue principale du film.
     */
    public Langue getLangue() { return langue; }

    /**
     * Getter du lieu de tournage du film.
     * @return Le lieu de tournage du film.
     */
    public LieuTournage getLieuTournage() { return lieuDeTournage; }

    /**
     * Getter de la liste des réalisateurs qui ont contribué au film.
     * @return La collection des réalisateurs du film.
     */
    public Set<Personne> getRealisateurs() { return listeRealisateurs; }

    /**
     * Getter de la liste des rôles qui ont contribué au film.
     * @return La collection des rôles du film.
     */
    public Set<Role> getRoles() { return listeRoles; }

    /**
     * Getter de la liste des genres du film.
     * @return La collection des genres du film.
     */
    public Set<Genre> getGenres() { return listeGenres; }

    /**
     * Setter de l'id du film.
     * @param id Nouveau id du film.
     */
    public void setId(String id) { this.id = id; }

    /**
     * Setter du pays du film.
     * @param pays Nouveau pays de production du film.
     */
    public void setPays(Pays pays) { this.pays = pays; }

    /**
     * Setter de la langue du film.
     * @param langue Nouvelle langue du film.
     */
    public void setLangue(Langue langue) { this.langue = langue; }

    /**
     * Setter du lieu de tournage du film.
     * @param lieuTournage Nouveau lieu de tournage du film.
     */
    public void setLieuTournage(LieuTournage lieuTournage) { this.lieuDeTournage = lieuTournage; }

    /**
     * Setter de la liste des réalisateurs du film.
     * @param realisateurs Nouvelle collection de réalisateurs du film.
     */
    public void setRealisateurs(Set<Personne> realisateurs) { this.listeRealisateurs = realisateurs; }

    /**
     * Setter de la liste des rôles du film.
     * @param roles Nouvelle collection de rôles du film.
     */
    public void setRoles(Set<Role> roles) { this.listeRoles = roles; }

    /**
     * Setter de la liste des genres du film.
     * @param genres Nouvelle collection de genres du film.
     */
    public void setGenres(Set<Genre> genres) { this.listeGenres = genres; }

}
