package fr.orsys.fx.calendrier_gif.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fr.orsys.fx.calendrier_gif.business.Emotion;

@RepositoryRestResource(exported = true)
public interface EmotionDao extends JpaRepository<Emotion, Long> {

	Emotion findByNom(String nom);

}
