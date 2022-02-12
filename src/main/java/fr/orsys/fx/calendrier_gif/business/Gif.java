package fr.orsys.fx.calendrier_gif.business;

import java.time.LocalDateTime;
import java.util.List;

//import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "dtype")
public abstract class Gif {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	
	protected LocalDateTime dateHeureAjout;
	
	protected String legende;
	
	@JsonIgnore
	@OneToOne
	protected Jour jour;
	
	// A venir ...
	@JsonIgnore
	@ManyToOne
	private Utilisateur utilisateur;

	@JsonIgnore
	@OneToMany(mappedBy="gif", fetch=FetchType.EAGER)
	private List<Reaction> reactions;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDateHeureAjout() {
		return dateHeureAjout;
	}

	public void setDateHeureAjout(LocalDateTime dateHeureAjout) {
		this.dateHeureAjout = dateHeureAjout;
	}

	public String getLegende() {
		return legende;
	}

	public void setLegende(String legende) {
		this.legende = legende;
	}

	public Jour getJour() {
		return jour;
	}

	public void setJour(Jour jour) {
		this.jour = jour;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public List<Reaction> getReactions() {
		return reactions;
	}

	public void setReactions(List<Reaction> reactions) {
		this.reactions = reactions;
	}

	@Override
	@JsonProperty(value = "utilisateur")
	public String toString() {
		return "utilisateur.getNom() + ' ' + utilisateur.getPrenom()";
	}

}