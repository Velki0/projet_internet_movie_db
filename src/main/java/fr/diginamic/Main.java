package fr.diginamic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.entites.Film;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("internet_movie_db");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = mapper.readTree(Paths.get("src/main/resources/minifilms.json").toFile());
        String jsonString = mapper.writeValueAsString(jsonNode);

        double debut = System.currentTimeMillis();

        List<Film> films = mapper.readValue(jsonString, new TypeReference<>() {});

        int index = 1;
        for (Film film : films) {

            System.err.println("Dépendance film numéro : " + index++);
            film.assignerLangueBdD(em);
            film.assignerPaysBdD(em);
            film.assignerGenresBdD(em);
            film.assignerLieuTournageBdD(em);
            film.assignerRealisateursBdD(em);
            film.assignerRolesBdD(em);

        }

        for (Film film : films) {
            transaction.begin();
            em.persist(film);
            transaction.commit();
        }

        double fin = System.currentTimeMillis();
        double tempsEcoule = (fin - debut) / 1000;
        System.out.println("Temps écoulé : " + tempsEcoule + " secondes");

        em.close();
        emf.close();

    }

}
