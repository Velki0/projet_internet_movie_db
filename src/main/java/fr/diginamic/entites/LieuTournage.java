package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class LieuTournage extends Lieu {

    @Column(name = "PRECISION_ADRESSE")
    private String precisionAdresse;

    @OneToMany(mappedBy = "lieuTournage")
    private Set<Film> films;

    public LieuTournage() {}

    @JsonCreator
    private LieuTournage(@JsonProperty("etatDept") final String region,
                         @JsonProperty("pays") final String nomPays,
                         @JsonProperty("ville") final String precisionAdresse) {

        super(region.trim(), nomPays.trim());
        this.precisionAdresse = precisionAdresse.trim();

    }

    public String getPrecisionAdresse() { return precisionAdresse; }

}
