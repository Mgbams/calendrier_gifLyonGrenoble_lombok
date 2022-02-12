package fr.orsys.fx.calendrier_gif.business;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GifTeleverse extends Gif{
	
	private String nomFichierOriginal;
		
}