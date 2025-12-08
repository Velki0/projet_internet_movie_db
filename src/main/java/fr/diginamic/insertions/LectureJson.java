package fr.diginamic.insertions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.diginamic.entites.Film;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * Classe utilitaire permettant de lire un fichier .json pour le transformer en une liste d'objet Java Film.
 */
public class LectureJson {

    /**
     * Méthode permettant de lire un fichier .json contenant un certain nombre de films à partir de la base de données IMDB.
     * @return Une liste de films.
     * @throws IOException Exception jetée si le film est introuvable.
     */
    public static List<Film> lireJSON() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode = mapper.readTree(Paths.get("src/main/resources/films.json").toFile());
        String jsonString = mapper.writeValueAsString(jsonNode);
        return mapper.readValue(jsonString, new TypeReference<>() {});

    }

}
