package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Entity
public class Film {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private int id;
    @Column(name = "TITRE", nullable = false)
    private String titre;
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;
    @Column(name = "NOTE")
    private float note;
    @Column(name = "RESUME", length = 4000)
    private String resume;
    @Column(name = "ANNEE", nullable = false)
    private String annee;

    @ManyToOne
    @JoinColumn(name = "PAYS_ID")
    private Pays pays;

    @ManyToOne
    @JoinColumn(name = "LANGUE_ID")
    private Langue langue;

    @ManyToOne
    @JoinColumn(name = "LIEU_TOURNAGE_ID")
    private LieuTournage lieuTournage;

    @ManyToMany
    @JoinTable(name = "FILM_REALISATEUR",
            joinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "REALISATEUR_ID", referencedColumnName = "ID"))
    private Set<Realisateur> realisateurs;

    @OneToMany(mappedBy = "film")
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "FILM_GENRE",
            joinColumns = @JoinColumn(name = "FILM_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID"))
    private Set<Genre> genres = new HashSet<>();

    public Film() {}

    @JsonCreator
    private Film(@JsonProperty("id") final String id,
                 @JsonProperty("nom") final String titre,
                 @JsonProperty("url") final String url,
                 @JsonProperty("rating") String note,
                 @JsonProperty("plot") final String resume,
                 @JsonProperty("langue") final Langue langue,
                 @JsonProperty("lieuTournage") final LieuTournage lieuTournage,
                 @JsonProperty("realisateurs") final Set<Realisateur> realisateurs,
                 @JsonProperty("pays") final Pays pays,
                 @JsonProperty("anneeSortie") final String anneeSortie,
                 @JsonProperty("roles") final Set<Role> roles,
                 @JsonProperty("genres") final Set<Genre> genres,
                 @JsonProperty("castingPrincipal") final List<CastingPrincipal> artistesPrincipaux) {

        this.id = Integer.parseInt(id.substring(2));
        this.titre = titre;
        this.urlIMDB = url;
        if(!note.isEmpty()) {
            note = note.replaceAll(",", ".");
            this.note = Float.parseFloat(note);
        }
        this.resume = resume;
        this.langue = langue;
        this.lieuTournage = lieuTournage;
        this.realisateurs = realisateurs;
        this.pays = pays;
        this.annee =  anneeSortie;
        this.roles = roles;
        this.genres = genres;

        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            for (CastingPrincipal artiste : artistesPrincipaux) {
                int artisteId = Integer.parseInt(artiste.getId().trim().substring(2));
                if (role.getActeur().getId() ==  artisteId) {
                    role.setRolePrincipal(true);
                }
            }
        }

    }

    public void assignerLangueBdD(EntityManager em) {

        if (langue != null) {
            EntityTransaction transaction = em.getTransaction();
            try {
                langue = em.createQuery("FROM Langue l WHERE l.nom = :langue ", Langue.class).setParameter("langue", langue.getNom()).getSingleResult();
            } catch (Exception e) {
                transaction.begin();
                em.persist(langue);
                transaction.commit();
            }
        }

    }

    public void assignerLieuTournageBdD(EntityManager em) {

        if (lieuTournage != null) {
            EntityTransaction transaction = em.getTransaction();
            try {
                lieuTournage = em.createQuery("FROM LieuTournage l WHERE l.region = :region AND l.pays = :pays AND l.precisionAdresse = :precisionAdresse", LieuTournage.class)
                        .setParameter("region", lieuTournage.getRegion())
                        .setParameter("pays", lieuTournage.getPays())
                        .setParameter("precisionAdresse", lieuTournage.getPrecisionAdresse())
                        .getSingleResult();
            } catch (Exception e) {
                lieuTournage.assignerPaysBdD(em);
                transaction.begin();
                em.persist(lieuTournage);
                transaction.commit();
            }
        }

    }

    public void assignerRealisateursBdD(EntityManager em) {

        if (!realisateurs.isEmpty()) {
            EntityTransaction transaction = em.getTransaction();
            Iterator<Realisateur> iterator = realisateurs.iterator();
            Set<Realisateur> collection = new HashSet<>();
            while (iterator.hasNext()) {
                Realisateur realisateur = iterator.next();
                try {
                    Realisateur realisateurBdD = em.createQuery("FROM Realisateur r WHERE r.id = :id", Realisateur.class)
                            .setParameter("id", realisateur.getId())
                            .getSingleResult();
                    iterator.remove();
                    collection.add(realisateurBdD);
                } catch (Exception e) {
                    realisateur.assignerLieuNaissanceBdD(em);
                    transaction.begin();
                    em.persist(realisateur);
                    transaction.commit();
                }
            }
            realisateurs.addAll(collection);
        }

    }

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

    public void assignerRolesBdD(EntityManager em) {

        EntityTransaction transaction = em.getTransaction();
        Iterator<Role> iterator = roles.iterator();
        while (iterator.hasNext()) {
            Role role = iterator.next();
            role.assignerActeurBdD(em);
            transaction.begin();
            em.persist(role);
            transaction.commit();
        }

    }

    public void assignerGenresBdD(EntityManager em) {

        if(!genres.isEmpty()) {
            EntityTransaction transaction = em.getTransaction();
            Iterator<Genre> iterator = genres.iterator();
            Set<Genre> collection = new HashSet<>();
            while (iterator.hasNext()) {
                Genre genre = iterator.next();
                try {
                    Genre genreBdD = em.createQuery("FROM Genre g WHERE g.nom = :genre", Genre.class).setParameter("genre", genre.getNom()).getSingleResult();
                    iterator.remove();
                    collection.add(genreBdD);
                } catch (Exception e) {
                    transaction.begin();
                    em.persist(genre);
                    transaction.commit();
                }
            }
            genres.addAll(collection);
        }

    }

}
