package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * La classe LieuTournage représente un lieu où un film a été produit.
 */
@Entity
public class LieuTournage extends Lieu {

    /** Précision de l'adresse du lieu. Peut éventuellement être une ville, un studio de tournage ou une adresse complète. */
    @Column(name = "PRECISION_ADRESSE")
    private String precisionAdresse;

    /** Collections de films où ce lieu de tournage a été utilisé. */
    @OneToMany(mappedBy = "lieuDeTournage")
    private Set<Film> films;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public LieuTournage() {}

    /**
     * Constructeur pour la création d'un nouveau lieu de tournage à partir de ses données de région, pays et d'une précision de l'adresse.
     * @param region Région où le lieu se trouve.
     * @param nomPays Nom du pays dans lequel ce lieu se trouve.
     * @param precisionAdresse Précision de l'adresse où ce lieu de tournage est situé.
     */
    @JsonCreator
    private LieuTournage(@JsonProperty("etatDept") final String region,
                         @JsonProperty("pays") final String nomPays,
                         @JsonProperty("ville") final String precisionAdresse) {

        super(region.trim(), nomPays.trim());
        this.precisionAdresse = precisionAdresse.trim();

    }

    /**
     * Getter pour le complément d'adresse du lieu de tournage.
     * @return La précision d'adresse du lieu de tournage.
     */
    public String getPrecisionAdresse() { return precisionAdresse; }

}
