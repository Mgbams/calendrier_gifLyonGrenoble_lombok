package fr.orsys.fx.calendrier_gif.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.fx.calendrier_gif.business.GifDistant;

public interface GifDistantDao extends JpaRepository<GifDistant, Long> {

	long deleteByUrlLike(String source);
}
