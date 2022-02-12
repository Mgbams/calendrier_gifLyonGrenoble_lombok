package fr.orsys.fx.calendrier_gif.business;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Reaction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Gif gif;
	
	@ManyToOne
	private Utilisateur utilisateur;

	@ManyToOne
	private Emotion emotion;
	
	private LocalDateTime dateHeure;
	
	public Reaction() {
		dateHeure = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return "Reaction [id=" + id + ", gif=" + gif + ", utilisateur=" + utilisateur + ", emotion=" + emotion
				+ ", dateHeure=" + dateHeure + "]";
	}
	
}