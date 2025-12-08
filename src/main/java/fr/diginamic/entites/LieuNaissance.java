package fr.diginamic.entites;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * La classe LieuNaissance représente un lieu où une personne est née.
 */
@Entity
public class LieuNaissance extends Lieu {

    /** Ville de naissance. */
    @Column(name = "VILLE")
    private String ville;

    /** Liste de personnes qui sont nées à ce lieu de naissance. */
    @OneToMany(mappedBy = "lieuNaissance")
    private Set<Personne> personnes;

    /**
     * Constructeur vide pour la mise en place JPA / Hibernate.
     */
    public LieuNaissance() {}

    /**
     * Constructeur pour la création d'une nouvelle instance de lieu de naissance à partir de trois chaines de caractères sur la ville, la région et le pays.
     * @param ville Ville de naissance.
     * @param region Région du pays où ce lieu se trouve.
     * @param nomPays Nom du pays où ce lieu se trouve.
     */
    public LieuNaissance(String ville, String region, String nomPays) {

        super(region, nomPays);
        this.ville = ville;

    }

    /**
     * Getter pour la ville du lieu de naissance.
     * @return Ville du lieu de naissance.
     */
    public String getVille() { return ville; }

}
