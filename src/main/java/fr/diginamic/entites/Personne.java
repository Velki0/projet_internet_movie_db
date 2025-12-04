package fr.diginamic.entites;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Personne {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private int id;
    @Column(name = "IDENTITE", nullable = false)
    private String identite;
    @Column(name = "DATE_NAISSANCE")
    private LocalDate dateNaissance;
    @Column(name = "URL_IMDB", nullable = false, unique = true)
    private String urlIMDB;

    @ManyToOne
    @JoinColumn(name = "LIEU_NAISSANCE_ID")
    private LieuNaissance lieuNaissance;

    public Personne() {}

    @JsonCreator
    protected Personne(@JsonProperty("id") final String id,
                       @JsonProperty("identite") final String identite,
                       @JsonProperty("url") final String urlIMDB,
                       @JsonProperty("naissance") Map<String, String> naissance) {

        this.id = Integer.parseInt(id.substring(2));
        this.identite = identite;
        this.urlIMDB = urlIMDB;

        try {
            String date = naissance.get("dateNaissance").trim();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
            dateNaissance = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            try {
                String date = naissance.get("dateNaissance").trim();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d", Locale.ENGLISH);
                dateNaissance = LocalDate.parse(date, formatter);
            } catch (DateTimeParseException ex2) {
                dateNaissance = null;
            }
        }

        try {
            List<String> lieu = List.of(naissance.get("lieuNaissance").trim().split(","));
            String ville = lieu.getFirst().trim();
            String region = lieu.get(1).trim();
            String pays = lieu.getLast().trim();
            lieuNaissance = new LieuNaissance(ville, region, pays);
        } catch (Exception ex) {
            lieuNaissance = null;
        }

    }

    public int getId() { return id; }

    public void assignerLieuNaissanceBdD(EntityManager em) {

        if (lieuNaissance != null) {
            EntityTransaction transaction = em.getTransaction();
            try {
                lieuNaissance = em.createQuery("FROM LieuNaissance l WHERE l.region = :region AND l.pays = :pays AND l.ville = :ville", LieuNaissance.class)
                        .setParameter("region", lieuNaissance.getRegion())
                        .setParameter("pays", lieuNaissance.getPays())
                        .setParameter("ville", lieuNaissance.getVille())
                        .getSingleResult();
            } catch (Exception e) {
                lieuNaissance.assignerPaysBdD(em);
                transaction.begin();
                em.persist(lieuNaissance);
                transaction.commit();
            }
        }

    }

}
