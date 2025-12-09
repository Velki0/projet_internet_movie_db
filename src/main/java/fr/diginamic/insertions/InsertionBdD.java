package fr.diginamic.insertions;

import fr.diginamic.entites.Film;
import fr.diginamic.entites.Genre;
import fr.diginamic.entites.Langue;
import fr.diginamic.entites.Lieu;
import fr.diginamic.entites.LieuNaissance;
import fr.diginamic.entites.LieuTournage;
import fr.diginamic.entites.Pays;
import fr.diginamic.entites.Personne;
import fr.diginamic.entites.Role;
import fr.diginamic.entites.TypePersonne;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe utilitaire permettant la mise en base de données des différents objets.
 */
public class InsertionBdD {

    /** L'entité manager qui permet de gérer l'API. */
    private static EntityManager em;
    /** Logger permettant de repérer les informations du nombre de données ou les éventuels mauvais réglages */
    private static final Logger LOGGER_INSERTION = LoggerFactory.getLogger("fr.diginamic.insertions.LoggerFilm");

    /**
     * Méthode permettant de mettre en base de donnée chaque film à partir d'une liste.
     * @param films Liste de films à insérer.
     * @param emf L'Entity Manager Factory pour la gestion de la base de donnée MariaDB.
     */
    public static void insererBdD(List<Film> films, EntityManagerFactory emf) {

        em = emf.createEntityManager();

        double debut = System.currentTimeMillis();

        LOGGER_INSERTION.info("Nombre total de films à parser : {}", films.size());
        insererChaqueElement(films);
        em.close();

        double fin = System.currentTimeMillis();
        LOGGER_INSERTION.info("Fin du parse. Temps écoulé : {} secondes", (fin - debut) / 1000);

    }

    /**
     * Sous-méthode permettant le séquencement des insertions en base de données.
     * Ainsi, les objets seront assigner par ordre de prérequis suivant la séquence suivante :
     * Les langues > Les pays > les genres > les lieux de tournage > les réalisateurs > les rôles > et enfin les films.
     * @param films Liste de films à insérer en base de données.
     */
    private static void insererChaqueElement(List<Film> films) {

        EntityTransaction transaction = em.getTransaction();
        int index = 1;
        int batch = 200;
        transaction.begin();
        for (Film film : films) {
            if (em.find(Film.class, film.getId()) == null) {
                LOGGER_INSERTION.info("Film numéro : {}", index++);
                insererLangue(film);
                insererPays(film);
                insererGenres(film);
                insererLieuTournage(film);
                insererRealisateurs(film);
                insererRoles(film);
                insererFilm(film);
                if (index % batch == 0) {
                    em.flush();
                    em.clear();
                    transaction.commit();
                    transaction.begin();
                }
            } else {
                LOGGER_INSERTION.warn("Film avec l'id {} déjà existant !", film.getId());
                index++;
            }
        }
        transaction.commit();

    }

    /**
     * Méthode d'insertion de la langue du film.
     * Si la langue existe déjà en base, une réatribution est réalisée sinon la nouvelle langue est insérée.
     * @param film Film dont la langue doit être insérée.
     */
    private static void insererLangue(Film film) {

        Langue langue = film.getLangue();
        if (langue != null) {
            try {
                film.setLangue(em.createQuery("FROM Langue l WHERE l.nom = :langue", Langue.class)
                                 .setParameter("langue", langue.getNom())
                                 .getSingleResult());
            } catch (Exception e) {
                em.persist(langue);
            }
        }

    }

    /**
     * Méthode d'insertion du pays du film.
     * Si le pays existe déjà en base, une réatribution est réalisée avec l'ajout si possible de l'url IMDB sinon le nouveau pays est inséré.
     * @param film Film dont le pays doit être inséré.
     */
    private static void insererPays(Film film) {

        Pays pays = film.getPays();
        if (pays != null) {
            try {
                Pays paysBdD = em.createQuery("FROM Pays p WHERE p.nom = :nomPays", Pays.class)
                                 .setParameter("nomPays", pays.getNom())
                                 .getSingleResult();
                if (paysBdD.getUrlIMDB() == null) {
                    paysBdD.setUrlIMDB(pays.getUrlIMDB());
                }
                film.setPays(paysBdD);
            } catch (Exception e) {
                em.persist(pays);
            }
        }

    }

    /**
     * Méthode d'insertion des genres du film.
     * Si les genres existent déjà en base, ils sont récupérés dans une liste sinon les nouveaux genres sont insérés.
     * Une réattribution est réalisée dans tous les cas.
     * @param film Film dont les genres doivent être insérés.
     */
    private static void insererGenres(Film film) {

        Set<Genre> genres = film.getGenres();
        if(!genres.isEmpty()) {
            Set<Genre> collectionGenres = new HashSet<>();
            for(Genre genre : genres) {
                try {
                    Genre genreBdD = em.createQuery("FROM Genre g WHERE g.nom = :genre", Genre.class)
                                       .setParameter("genre", genre.getNom())
                                       .getSingleResult();
                    collectionGenres.add(genreBdD);
                } catch (Exception e) {
                    em.persist(genre);
                    collectionGenres.add(genre);
                }
            }
            film.setGenres(collectionGenres);
        }

    }

    /**
     * Méthode d'insertion du lieu de tournage du film.
     * Si le lieu de tournage existe déjà en base, une réatribution est réalisée sinon le nouveau lieu est inséré.
     * Une vérification pour le pays du lieu doit être réalisée avant chaque nouvelle insertion.
     * @param film Film dont le lieu de tournage doit être inséré.
     */
    private static void insererLieuTournage(Film film) {

        LieuTournage lieuTournage = film.getLieuTournage();
        if (lieuTournage != null) {
            try {
                film.setLieuTournage(em.createQuery("FROM LieuTournage l " +
                                                       "WHERE l.region = :region " +
                                                       "AND l.pays.nom = :nomPays " +
                                                       "AND l.precisionAdresse = :precisionAdresse", LieuTournage.class)
                                       .setParameter("region", lieuTournage.getRegion())
                                       .setParameter("nomPays", lieuTournage.getPays().getNom())
                                       .setParameter("precisionAdresse", lieuTournage.getPrecisionAdresse())
                                       .getSingleResult());
            } catch (Exception e) {
                assignerPays(lieuTournage);
                em.persist(lieuTournage);
            }
        }

    }

    /**
     * Méthode d'assignation du pays d'un lieu de tournage.
     * Si le pays existe déjà en base, une réatribution est réalisée sinon le nouveau pays est inséré.
     * @param lieu Lieu dont le pays doit être inséré.
     */
    private static void assignerPays(Lieu lieu) {

        Pays pays = lieu.getPays();
        if (pays != null) {
            try {
                lieu.setPays(em.createQuery("FROM Pays p WHERE p.nom = :nomPays", Pays.class)
                               .setParameter("nomPays", pays.getNom())
                               .getSingleResult());
            } catch (Exception e) {
                em.persist(pays);
            }
        }

    }

    /**
     * Méthode d'insertion des réalisateurs du film.
     * Si le réalisateur n'existe pas déjà en base, le nouveau réalisateur est inséré.
     * Pour chaque nouvelle insertion, le lieu de naissance est vérifié s'il n'existe pas déjà en base.
     * Une réatribution de la liste de réalisateurs est réalisée dans tous les cas.
     * @param film Film dont la liste de réalisateur doit être insérée.
     */
    private static void insererRealisateurs(Film film) {

        Set<Personne> realisateurs = film.getRealisateurs();
        if (!realisateurs.isEmpty()) {
            Set<Personne> collectionRealisateurs = new HashSet<>();
            for (Personne realisateur : realisateurs) {
                if (realisateur != null) {
                    Personne realisateurBdD = em.find(Personne.class, realisateur.getId());
                    if (realisateurBdD != null) {
                        realisateurBdD.getTypes().add(TypePersonne.REALISATEUR);
                        em.merge(realisateurBdD);
                        collectionRealisateurs.add(realisateurBdD);

                    } else {
                        assignerLieuNaissance(realisateur);
                        em.persist(realisateur);
                        collectionRealisateurs.add(realisateur);
                    }
                }
            }
            film.setRealisateurs(collectionRealisateurs);
        }

    }

    /**
     * Méthode d'insertion des rôles du film.
     * Pour chaque nouvelle insertion, l'acteur est vérifié s'il n'existe pas déjà en base.
     * @param film Film dont la liste des rôles doit être insérée.
     */
    public static void insererRoles(Film film) {

        Set<Role> roles = film.getRoles();
        for (Role role : roles) {
            insererActeur(role);
            em.persist(role);
        }

    }

    /**
     * Méthode d'insertion d'un acteur de film.
     * Si un acteur existe déjà en base, une réatribution est effectuée sinon le nouvel acteur est inséré.
     * Pour chaque nouvelle insertion, le lieu de naissance est vérifié s'il n'existe pas déjà en base.
     * @param role Rôle d'un acteur qui doit être inséré.
     */
    public static void insererActeur(Role role) {

        Personne acteur = role.getActeur();
        if (acteur != null) {
            Personne acteurBdD = em.find(Personne.class, acteur.getId());
            if (acteurBdD != null) {
                acteurBdD.getTypes().add(TypePersonne.ACTEUR);
                em.merge(acteurBdD);
                role.setActeur(acteurBdD);
            } else {
                assignerLieuNaissance(acteur);
                em.persist(acteur);
            }
        }

    }

    /**
     * Méthode d'assignation du lieu de naissance d'une personne.
     * Si le lieu de naissance existe déjà en base, une réatribution est réalisée sinon le nouveau lieu est inséré.
     * Une vérification pour le pays du lieu doit être réalisée avant chaque nouvelle insertion.
     * @param personne Personne dont le lieu de naissance doit être assigné.
     */
    public static void assignerLieuNaissance(Personne personne) {

        LieuNaissance lieuNaissance = personne.getLieuNaissance();
        if (lieuNaissance != null) {
            try {
                personne.setLieuNaissance(em.createQuery("FROM LieuNaissance l " +
                                                            "WHERE l.region = :region " +
                                                            "AND l.pays.nom = :nomPays " +
                                                            "AND l.ville = :ville", LieuNaissance.class)
                                            .setParameter("region", lieuNaissance.getRegion())
                                            .setParameter("nomPays", lieuNaissance.getPays().getNom())
                                            .setParameter("ville", lieuNaissance.getVille())
                                            .getSingleResult());
            } catch (Exception e) {
                assignerPays(lieuNaissance);
                em.persist(lieuNaissance);
            }
        }

    }

    /**
     * Méthode d'insertion du film.
     * @param film Film à insérer en base.
     */
    public static void insererFilm(Film film) {

            em.persist(film);

    }

}
