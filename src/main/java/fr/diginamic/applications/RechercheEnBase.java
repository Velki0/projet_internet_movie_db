package fr.diginamic.applications;

import fr.diginamic.interfacesutilisateur.InterfaceUtilisateur;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe exécutable permettant à l'utilisateur d'interagir avec la base de données pour rechercher diverses informations.
 */
public class RechercheEnBase {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("internet_movie_db");
        int choixUtilisateur;
        do {
            choixUtilisateur = InterfaceUtilisateur.demanderChoixUtilisateurMenuPrincipal(emf);
        } while (choixUtilisateur != 7);
        emf.close();

    }

}
