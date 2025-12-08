package fr.diginamic.utils;

import fr.diginamic.entites.CastingPrincipal;
import fr.diginamic.entites.Film;
import fr.diginamic.entites.Role;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * La classe RolesPrincipauxUtils est une classe utilitaire pour l'attribution en base de données des rôles principaux au sein des films.
 */
public class RolesPrincipauxUtils {

    /**
     * Méthode permettant à partir d'un film et de ses données de castings, de renseigner au sein de ses rôles lesquels sont principaux.
     * @param film Film dont il faut assigner les rôles principaux.
     * @param artistesPrincipaux Liste d'artistes possèdant un rôle principal dans ce film.
     */
    public static void assigner(Film film, List<CastingPrincipal> artistesPrincipaux) {

        Set<Role> roles = film.getRoles();
        for (Role role : roles) {
            for (CastingPrincipal artiste : artistesPrincipaux) {
                String artisteId = artiste.getId().trim();
                if (Objects.equals(role.getActeur().getId(), artisteId)) {
                    role.setRolePrincipal(true);
                }
            }
        }
        film.setRoles(roles);

    }

}
