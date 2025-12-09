package fr.diginamic.utils;

import fr.diginamic.entites.LieuNaissance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La classe FormatUtils est une classe utilitaire utilisée pour le formatage des données.
 * Elle regroupe des méthodes pour les dates, les notes, les pays et les lieux de naissance.
 */
public class FormatUtils {

    /** Logger afin d'éventuellement mettre en valeur les améliorations possibles du parsing de l'application. */
    private static final Logger LOGGER_FORMAT = LoggerFactory.getLogger("fr.diginamic.utils.LoggerFormat");

    /**
     * Méthode permettant de formater les notes brutes reçues, lorsqu'elle contient des valeurs décimales.
     * @param note Note brute reçue du fichier .json.
     * @return Note sur 10 du film.
     */
    public static Float formatterNote(String note) {

        if(!note.isBlank()) {
            note = note.replaceAll(",", ".");
            return Float.parseFloat(note);
        } else {
            return null;
        }

    }

    /**
     * Méthode permettant de remplacer les abréviations des noms de pays.
     * @param nomPays Nom du pays brut issu du fichier .json.
     * @return Nom du pays sans abréviation.
     */
    public static String formatterPays(String nomPays) {

        if (nomPays.equals("USA")) {
            return "United States";
        } else if (nomPays.equals("UK")) {
            return "United Kingdom";
        } else {
            return nomPays;
        }

    }

    /**
     * Méthode permettant de formatter les différentes dates présentes dans le fichier brut.
     * @param date Date brut issu du fichier .json.
     * @return Date au format LocalDate.
     */
    public static LocalDate formatterDate(String date) {

        if (!date.isBlank()) {
            date = date.trim();
            Set<DateTimeFormatter> formatsDate = new HashSet<>();
            formatsDate.add(DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH));
            formatsDate.add(DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH));
            formatsDate.add(DateTimeFormatter.ofPattern("MM d yyyy", Locale.ENGLISH));
            formatsDate.add(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH));
            formatsDate.add(DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.FRENCH));
            formatsDate.add(DateTimeFormatter.ofPattern("MMM d yyyy", Locale.FRENCH));
            formatsDate.add(DateTimeFormatter.ofPattern("MM d yyyy", Locale.FRENCH));
            formatsDate.add(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.FRENCH));
            for (DateTimeFormatter format : formatsDate) {
                try {
                    return LocalDate.parse(date, format);
                } catch (DateTimeParseException ignored) {}
            }
            LOGGER_FORMAT.warn("La date {} n'a pas pu être formattée.", date);
        }
        return null;

    }

    /**
     * Méthode permettant de lire les différents formats de lieu de naissance du fichier source.
     * @param lieuNaissance Lieu de naissance brut en chaine de caractère issu du fichier source.
     * @return Une nouvelle instance de LieuNaissance possèdant les attributs formalisés.
     */
    public static LieuNaissance formatterLieuNaissance(String lieuNaissance) {

        if (!lieuNaissance.isBlank()) {

            lieuNaissance = lieuNaissance.trim();
            String ville, region, pays, nouvelleVille, nouvelleRegion, nouveauPays;

            // Cas 1 : Ville, Région, Pays [now nouvelle ville, nouveau pays]
            Pattern patternCas1 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^,\\[\\]]+?),\\s*([^]]+)]\\s*$");
            Matcher matcherCas1 = patternCas1.matcher(lieuNaissance);
            if (matcherCas1.matches()) {
                ville = matcherCas1.group(1).trim();
                region = matcherCas1.group(2).trim();
                pays = matcherCas1.group(3).trim();
                nouvelleVille = matcherCas1.group(4).trim();
                nouveauPays = matcherCas1.group(5).trim();
                return new LieuNaissance(ville + " [ maintenant " + nouvelleVille + " ]", region, pays + " [ maintenant " + nouveauPays + " ]");
            }

            // Cas 2 : Ville, Région, Ancien Pays [now Nouveau Pays]
            Pattern patternCas2 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^]]+)]\\s*$");
            Matcher matcherCas2 = patternCas2.matcher(lieuNaissance);
            if (matcherCas2.matches()) {
                ville = matcherCas2.group(1).trim();
                region = matcherCas2.group(2).trim();
                pays = matcherCas2.group(3).trim();
                nouveauPays = matcherCas2.group(4).trim();
                return new LieuNaissance(ville, region, pays + " [ maintenant " + nouveauPays + " ]");
            }

            // Cas 3 : Ville, Région [now nouvelle région], Pays
            Pattern patternCas3 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^]]+)]\\s*,\\s*([^,\\[\\]]+?)\\s*$");
            Matcher matcherCas3 = patternCas3.matcher(lieuNaissance);
            if (matcherCas3.matches()) {
                ville = matcherCas3.group(1).trim();
                region = matcherCas3.group(2).trim();
                nouvelleRegion = matcherCas3.group(3).trim();
                pays = matcherCas3.group(4).trim();
                return new LieuNaissance(ville, region + " [ maintenant " + nouvelleRegion + " ]", pays);
            }

            // Cas 4 : Ville, Région, Sous-pays, Pays
            Pattern patternCas4 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*$");
            Matcher matcherCas4 = patternCas4.matcher(lieuNaissance);
            if (matcherCas4.matches()) {
                ville = matcherCas4.group(1).trim();
                region = matcherCas4.group(2).trim();
                pays = matcherCas4.group(4).trim();
                return new LieuNaissance(ville, region, pays);
            }

            // Cas 5 : Sous-ville, Région [now nouvelle région], Ville, Sous-pays, Pays
            Pattern patternCas5 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^]]+)]\\s*,\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*$");
            Matcher matcherCas5 = patternCas5.matcher(lieuNaissance);
            if (matcherCas5.matches()) {
                region = matcherCas5.group(2).trim();
                nouvelleRegion = matcherCas5.group(3).trim();
                ville = matcherCas5.group(4).trim();
                pays = matcherCas5.group(6).trim();
                return new LieuNaissance(ville, region + " [ maintenant " + nouvelleRegion + " ]", pays);
            }

            // Cas 6 : Ville, Ancien Pays [now Nouvelle Ville, Nouveau Pays]
            Pattern patternCas6 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^,\\[\\]]+?),\\s*([^]]+)]\\s*$");
            Matcher matcherCas6 = patternCas6.matcher(lieuNaissance);
            if (matcherCas6.matches()) {
                ville = matcherCas6.group(1).trim();
                pays = matcherCas6.group(2).trim();
                nouvelleVille = matcherCas6.group(3).trim();
                nouveauPays = matcherCas6.group(4).trim();
                return new LieuNaissance(ville + " [ maintenant " + nouvelleVille + " ]", "", pays + " [ maintenant " + nouveauPays + " ]");
            }

            // Cas 7 : Ville, Ancien Pays [now Nouveau Pays]
            Pattern patternCas7 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^]]+)]\\s*$");
            Matcher matcherCas7 = patternCas7.matcher(lieuNaissance);
            if (matcherCas7.matches()) {
                ville = matcherCas7.group(1).trim();
                pays = matcherCas7.group(2).trim();
                nouveauPays = matcherCas7.group(3).trim();
                return new LieuNaissance(ville, "", pays + " [ maintenant " + nouveauPays + " ]");
            }

            // Cas 8 : Ville, Ancien Pays [now Nouvelle Ville, Nouvelle Région, Nouveau Pays]
            Pattern patternCas8 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^]]+)]\\s*$");
            Matcher matcherCas8 = patternCas8.matcher(lieuNaissance);
            if (matcherCas8.matches()) {
                ville = matcherCas8.group(1).trim();
                pays = matcherCas8.group(2).trim();
                nouvelleVille = matcherCas8.group(3).trim();
                nouvelleRegion = matcherCas8.group(4).trim();
                nouveauPays = matcherCas8.group(5).trim();
                return new LieuNaissance(ville + " [ maintenant " + nouvelleVille + " ]", nouvelleRegion, pays + " [ maintenant " + nouveauPays + " ]");
            }

            // Cas 9 : Ville, Pays (present-day Nouvelle Ville, Nouveau Pays)
            Pattern patternCas9 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^(]+?)\\s*\\(present-day\\s+([^,)]+?),\\s*([^)]+)\\)\\s*$");
            Matcher matcherCas9 = patternCas9.matcher(lieuNaissance);
            if (matcherCas9.matches()) {
                ville = matcherCas9.group(1).trim();
                pays = matcherCas9.group(2).trim();
                nouvelleVille = matcherCas9.group(3).trim();
                nouveauPays = matcherCas9.group(4).trim();
                return new LieuNaissance(ville + " [ maintenant " + nouvelleVille + " ]", "", pays + " [ maintenant " + nouveauPays + " ]");
            }

            // Cas 10 : Ville, Région [now Nouvelle Région], Sous-pays, Pays
            Pattern patternCas10 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*\\[now\\s+([^]]+)]\\s*,\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*$");
            Matcher matcherCas10 = patternCas10.matcher(lieuNaissance);
            if (matcherCas10.matches()) {
                ville = matcherCas10.group(1).trim();
                region = matcherCas10.group(2).trim();
                nouvelleRegion = matcherCas10.group(3).trim();
                pays = matcherCas10.group(5).trim();
                return new LieuNaissance(ville, region + " [ maintenant " + nouvelleRegion + " ]", pays);
            }

            // Cas 11 : Ville, Région, Pays
            Pattern patternCas11 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*$");
            Matcher matcherCas11 = patternCas11.matcher(lieuNaissance);
            if (matcherCas11.matches()) {
                ville = matcherCas11.group(1).trim();
                region = matcherCas11.group(2).trim();
                pays = matcherCas11.group(3).trim();
                return new LieuNaissance(ville, region, pays);
            }

            // Cas 12 : Ville, Pays
            Pattern patternCas12 = Pattern.compile("^([^,\\[\\]]+?),\\s*([^,\\[\\]]+?)\\s*$");
            Matcher matcherCas12 = patternCas12.matcher(lieuNaissance);
            if (matcherCas12.matches()) {
                ville = matcherCas12.group(1).trim();
                pays = matcherCas12.group(2).trim();
                return new LieuNaissance(ville, "", pays);
            }

            // Si aucun pattern ne match
            LOGGER_FORMAT.warn("Aucun format valide pour le Lieu de Naissance \"{}\" n'a été trouvé.", lieuNaissance);

        }
        return null;

    }

}
