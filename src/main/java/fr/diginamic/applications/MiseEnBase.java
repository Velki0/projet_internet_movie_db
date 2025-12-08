package fr.diginamic.applications;

import fr.diginamic.entites.Film;
import fr.diginamic.insertions.InsertionBdD;
import fr.diginamic.insertions.LectureJson;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;

/**
 * Classe exécutable permettant de remplir la base de données avec les informations présente dans le fichier films.json (situé dans resources).
 */
public class MiseEnBase {

    public static void main(String[] args) throws IOException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("internet_movie_db_drop_and_create");
        List<Film> films = LectureJson.lireJSON();
        InsertionBdD.insererBdD(films, emf);
        emf.close();

    }

}
