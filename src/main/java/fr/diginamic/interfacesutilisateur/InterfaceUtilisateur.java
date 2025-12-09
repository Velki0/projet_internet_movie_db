package fr.diginamic.interfacesutilisateur;

import javax.persistence.EntityManagerFactory;
import java.util.Scanner;

import static fr.diginamic.interfacesutilisateur.AffichageUtilisateur.afficherActeursCommunsEntreDeuxFilms;
import static fr.diginamic.interfacesutilisateur.AffichageUtilisateur.afficherCastingFilm;
import static fr.diginamic.interfacesutilisateur.AffichageUtilisateur.afficherFilmographieActeur;
import static fr.diginamic.interfacesutilisateur.AffichageUtilisateur.afficherFilmsCommunsEntreDeuxActeurs;
import static fr.diginamic.interfacesutilisateur.AffichageUtilisateur.afficherFilmsCommunsEntreDeuxActeursEntreDeuxAnnees;
import static fr.diginamic.interfacesutilisateur.AffichageUtilisateur.afficherFilmsSortisEntreDeuxAnnees;

/**
 * La classe InterfaceUtilisateur est une classe utilitaire permettant l'affichage du menu principal d'application.
 * Elle permet aussi via l'utilisation de trois autres méthodes de demander à l'utilisateur de rentrer un Acteur, une Année ou bien le nom d'un film.
 */
public class InterfaceUtilisateur {

    /**
     * Méthode permettant l'affichage du menu principal.
     * Elle demande aussi à l'utilisateur son choix pour naviguer dans le menu.
     * @param emf L'entité manager factory de la base de donnée, permettant le lien avec l'application.
     * @return Le dernier choix de l'utilisateur.
     */
    public static int demanderChoixUtilisateurMenuPrincipal(EntityManagerFactory emf) {

        Scanner scanner = new Scanner(System.in);
        int choixUtilisateur = 0;
        while (choixUtilisateur != 7) {
            do {
                System.out.println(
                        "-------------------------------------------------------------------------------------------------------\n" +
                        "                        Bienvenue dans la base de donnée !                                             \n" +
                        "   Que voulez vous faire ?                                                                             \n" +
                        "   1 - Affichage de la filmographie d’un acteur donné                                                  \n" +
                        "   2 - Affichage du casting d’un film donné                                                            \n" +
                        "   3 - Affichage des films sortis entre 2 années données                                               \n" +
                        "   4 - Affichage des films communs à 2 acteurs/actrices donnés.                                        \n" +
                        "   5 - Affichage des films sortis entre 2 années données et qui ont un acteur/actrice donné au casting.\n" +
                        "   6 - Affichage des acteurs communs à 2 films donnés                                                  \n" +
                        "   7 - Quitter l'application.                                                                            "
                );
                choixUtilisateur = scanner.nextInt();
            } while (choixUtilisateur != 1 &&
                     choixUtilisateur != 2 &&
                     choixUtilisateur != 3 &&
                     choixUtilisateur != 4 &&
                     choixUtilisateur != 5 &&
                     choixUtilisateur != 6 &&
                     choixUtilisateur != 7);

            switch (choixUtilisateur) {
                case 1:
                    afficherFilmographieActeur(emf);
                    break;
                case 2:
                    afficherCastingFilm(emf);
                    break;
                case 3:
                    afficherFilmsSortisEntreDeuxAnnees(emf);
                    break;
                case 4:
                    afficherFilmsCommunsEntreDeuxActeurs(emf);
                    break;
                case 5:
                    afficherFilmsCommunsEntreDeuxActeursEntreDeuxAnnees(emf);
                    break;
                case 6:
                    afficherActeursCommunsEntreDeuxFilms(emf);
                    break;
            }
        }
        scanner.close();
        return choixUtilisateur;

    }

    /**
     * Méthode permettant de demander à l'utilisateur d'entrer le nom d'un acteur ou actrice.
     * @return Le nom inscrit par l'utilisateur.
     */
    protected static String demanderIdentiteActeur(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez choisir le nom d'un acteur ou une actrice.");
        return scanner.nextLine().trim();

    }

    /**
     * Méthode permettant de demander à l'utilisateur d'entrer le titre d'un film.
     * @return Le titre du film inscrit par l'utilisateur.
     */
    protected static String demanderFilm(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez choisir le titre d'un film.");
        return scanner.nextLine().trim();

    }

    /**
     * Méthode permettant de demander à l'utilisateur d'entrer une année de parution.
     * @return L'année inscrite par l'utilisateur.
     */
    protected static String demanderAnnee(){

        Scanner scanner = new Scanner(System.in);
        System.out.println("Veuillez entrer une année de parution.");
        return scanner.nextLine().trim();

    }

}
