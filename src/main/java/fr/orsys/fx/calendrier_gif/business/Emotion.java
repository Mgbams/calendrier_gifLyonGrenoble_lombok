package fr.orsys.fx.calendrier_gif.business;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "nom", "code"})
public class Emotion {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nom;
	
	private String code;
	
	public Emotion(String nom, String code) {
		this.nom = nom;
		this.code = code;
	}

}