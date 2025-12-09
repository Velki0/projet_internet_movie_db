package fr.diginamic.interfacesutilisateur;

import fr.diginamic.entites.Film;
import fr.diginamic.entites.Personne;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static fr.diginamic.interfacesutilisateur.InterfaceUtilisateur.demanderAnnee;
import static fr.diginamic.interfacesutilisateur.InterfaceUtilisateur.demanderFilm;
import static fr.diginamic.interfacesutilisateur.InterfaceUtilisateur.demanderIdentiteActeur;

/**
 * La classe AffichageUtilisateur est une classe utilitaire permettant de réaliser les différentes demandes de l'utilisateur et de les afficher dans la console.
 */
public class AffichageUtilisateur {

    /**
     * Méthode permettant d'afficher la filmographie pour un acteur renseigné par l'utilisateur.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     */
    protected static void afficherFilmographieActeur(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        System.out.println("-------------------------------------------------------------------------------------------------------\n" +
                           "Vous avez choisi la catégorie d'affichage de la filmographie d’un acteur donné."
        );
        String nomActeur = demanderIdentiteActeur();
        List<Film> filmographie = em.createQuery("SELECT f FROM Film f JOIN f.listeRoles r " +
                                                    "WHERE r.acteur.identite = :identiteActeur",  Film.class)
                                    .setParameter("identiteActeur", nomActeur)
                                    .getResultList();
        if  (filmographie.isEmpty()) {
            System.out.println("L'acteur désigné n'est pas présent dans la base de données.");
        } else {
            System.out.println("Voici la filmographie de " + nomActeur + " :");
            for (Film film : filmographie) {
                System.out.println(film.getTitre());
            }
        }
        em.close();

    }

    /**
     * Méthode permettant d'afficher le casting d'un film renseigné par l'utilisateur.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     */
    protected static void afficherCastingFilm(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        System.out.println("-------------------------------------------------------------------------------------------------------\n" +
                           "Vous avez choisi la catégorie d'affichage du casting d'un film."
        );
        String titreFilm = demanderFilm();
        List<Personne> acteurs = em.createQuery("SELECT a FROM Acteur a JOIN a.roles r " +
                                                   "WHERE r.film.titre = :titreFilm",  Personne.class)
                                   .setParameter("titreFilm", titreFilm)
                                   .getResultList();
        if  (acteurs.isEmpty()) {
            System.out.println("Le film désigné n'est pas présent dans la base de données.");
        } else {
            System.out.println("Voici le casting du film " + titreFilm + " :");
            for (Personne acteur : acteurs) {
                System.out.println(acteur.getIdentite());
            }
        }
        em.close();

    }

    /**
     * Méthode permettant d'afficher la liste de films sortis entre deux années renseignées par l'utilisateur.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     */
    protected static void afficherFilmsSortisEntreDeuxAnnees(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        System.out.println("-------------------------------------------------------------------------------------------------------\n" +
                           "Vous avez choisi la catégorie d'affichage des films sortis entre deux années"
        );
        String premiereAnnee = demanderAnnee();
        String secondeAnnee = demanderAnnee();
        if (Integer.parseInt(premiereAnnee) > Integer.parseInt(secondeAnnee)) {
            System.out.println("Vous n'avez pas renseigné des années correctes !");
        } else {
            List<Film> films = em.createQuery("FROM Film f " +
                                                 "WHERE f.annee " +
                                                 "BETWEEN :premiereAnnee " +
                                                 "AND :secondeAnnee", Film.class)
                                 .setParameter("premiereAnnee", premiereAnnee)
                                 .setParameter("secondeAnnee", secondeAnnee)
                                 .getResultList();
            if (films.isEmpty()) {
                System.out.println("Aucun film n'est présent dans la base de données entre ces deux dates.");
            } else {
                System.out.println("Voici la liste de films parus entre " + premiereAnnee + " et " + secondeAnnee + " :");
                for (Film film : films) {
                    System.out.println(film.getTitre());
                }
            }
        }
        em.close();

    }

    /**
     * Méthode permettant d'afficher la liste de films en commun où jouent deux acteurs renseignés par l'utilisateur.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     */
    protected static void afficherFilmsCommunsEntreDeuxActeurs(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        System.out.println("-------------------------------------------------------------------------------------------------------\n" +
                           "Vous avez choisi la catégorie d'affichage des films en communs entre deux acteurs ou actrices."
        );
        String premierActeur = demanderIdentiteActeur();
        String secondActeur = demanderIdentiteActeur();

        List<Film> films = em.createQuery("SELECT f FROM Film f JOIN f.listeRoles r1 JOIN f.listeRoles r2 " +
                                             "WHERE r1.acteur.identite = :premierActeur " +
                                             "AND r2.acteur.identite = :secondActeur " +
                                             "AND r1.id <> r2.id", Film.class)
                             .setParameter("premierActeur", premierActeur)
                             .setParameter("secondActeur", secondActeur)
                             .getResultList();
        if (films.isEmpty()) {
            System.out.println("Aucun film en commun n'est présent dans la base de données avec ces deux acteurs");
        } else {
            System.out.println("Voici la liste de films parus avec " + premierActeur + " et " + secondActeur + " :");
            for (Film film : films) {
                System.out.println(film.getTitre());
            }
        }
        em.close();

    }

    /**
     * Méthode permettant d'afficher la filmographie pour un acteur et entre deux années renseignés par l'utilisateur.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     */
    protected static void afficherFilmsCommunsEntreDeuxActeursEntreDeuxAnnees(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        System.out.println("-------------------------------------------------------------------------------------------------------\n" +
                           "Vous avez choisi la catégorie d'affichage des films sortis entre deux années pour un acteur / actrice donné.e."
        );
        String premiereAnnee = demanderAnnee();
        String secondeAnnee = demanderAnnee();
        String nomActeur = demanderIdentiteActeur();
        if (Integer.parseInt(premiereAnnee) > Integer.parseInt(secondeAnnee)) {
            System.out.println("Vous n'avez pas renseigné des années correctes !");
        } else {
            List<Film> films = em.createQuery("SELECT f FROM Film f JOIN f.listeRoles r " +
                                                 "WHERE r.acteur.identite = :nomActeur " +
                                                 "AND f.annee " +
                                                 "BETWEEN :premiereAnnee " +
                                                 "AND :secondeAnnee", Film.class)
                                 .setParameter("nomActeur", nomActeur)
                                 .setParameter("premiereAnnee", premiereAnnee)
                                 .setParameter("secondeAnnee", secondeAnnee)
                                 .getResultList();
            if (films.isEmpty()) {
                System.out.println("Aucun film n'est présent dans la base de données entre ces deux dates et pour cet acteur.");
            } else {
                System.out.println("Voici la liste de films parus entre " + premiereAnnee + " et " + secondeAnnee + " pour l'acteur / actrice " +  nomActeur + " :");
                for (Film film : films) {
                    System.out.println(film.getTitre());
                }
            }
        }
        em.close();

    }

    /**
     * Méthode permettant d'afficher des acteurs et actrices en commun entre deux films renseignés par l'utilisateur.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     */
    protected static void afficherActeursCommunsEntreDeuxFilms(EntityManagerFactory emf) {

        EntityManager em = emf.createEntityManager();
        System.out.println("-------------------------------------------------------------------------------------------------------\n" +
                           "Vous avez choisi la catégorie d'affichage des acteurs / actrices communs entre deux films."
        );
        String premierTitreFilm = demanderFilm();
        String secondTitreFilm = demanderFilm();
        List<Personne> acteurs = em.createQuery("SELECT DISTINCT a FROM Acteur a JOIN a.roles r1 JOIN a.roles r2 " +
                                                   "WHERE r1.film.titre = :premierTitreFilm " +
                                                   "AND r2.film.titre = :secondTitreFilm " +
                                                   "AND r1.id <> r2.id",  Personne.class)
                                    .setParameter("premierTitreFilm", premierTitreFilm)
                                    .setParameter("secondTitreFilm", secondTitreFilm)
                                    .getResultList();
        if  (acteurs.isEmpty()) {
            System.out.println("Il n'y a aucune correspondance d'acteurs ou d'actrices pour ces deux films.");
        } else {
            System.out.println("Voici le casting commun entre les deux films " + premierTitreFilm + " et " + secondTitreFilm + " :");
            for (Personne acteur : acteurs) {
                System.out.println(acteur.getIdentite());
            }
        }
        em.close();

    }

}
