package fr.orsys.fx.calendrier_gif.business;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
//import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
//import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Utilisateur {

	private static final int NB_POINTS_INITIAL = 500;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nom;
	
	@NotBlank(message="Merci de préciser votre prénom")
	private String prenom;
	
	@Email(message="Merci de préciser une adresse email au bon format")
	@NotBlank(message="Merci de préciser une adresse email")
	@Column(unique=true)
	// Big up Oleg pour la regex !
	@Pattern(regexp="^([A-Za-z0-9-])+(.[A-Za-z0-9-]+)*@orsys.fr$", message="Votre adresse doit faire partie du nom de domaine orsys.fr")
	private String email;
	
	@JsonIgnore
	@Size(min=3, message="Le mot de passe doit comporter au moins trois caractères")
	private String motDePasse;
	
	@ManyToOne
	@NotNull(message="Merci de choisir un thème")
	private Theme theme;
	
	private int nbPoints;

	private LocalDateTime dateHeureInscription;
	
	@JsonIgnore
	@OneToMany(mappedBy="utilisateur")
	private List<Gif> gifs;
	
	public Utilisateur() {
		nbPoints = NB_POINTS_INITIAL;
		dateHeureInscription = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "Utilisateur [id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", motDePasse="
				+ motDePasse + ", theme=" + theme + ", nbPoints=" + nbPoints + "]";
	}
	
}