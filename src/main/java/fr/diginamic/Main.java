package fr.diginamic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.entites.Film;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("internet_movie_db");
//        EntityManager em = emf.createEntityManager();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = mapper.readTree(Paths.get("src/main/resources/minifils.json").toFile());
        String jsonString = mapper.writeValueAsString(jsonNode);
        System.out.println(jsonString);

        List<Film> films = mapper.readValue(jsonString, new TypeReference<>() {});
        System.out.println(films.toString());

    }

}
