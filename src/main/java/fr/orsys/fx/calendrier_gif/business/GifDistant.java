package fr.orsys.fx.calendrier_gif.business;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class GifDistant extends Gif{
	
	//@Pattern(regexp = "^https?://(?:[a-z0-9\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:Gif|gif|GIF)$", message="L''URL doit se terminer par .gif")
	@NotNull(message="Merci de saisir une URL")
	@NotBlank(message="Merci de saisir une URL")
	@URL(message="Merci de saisir une URL valide, elle doit se terminer par .gif",
		 regexp = "^https?://(?:[a-z0-9\\-]+\\.)+[a-z]{2,6}(?:/[^/#?]+)+\\.(?:Gif|gif|GIF)$")
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}