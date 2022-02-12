package fr.orsys.fx.calendrier_gif.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.orsys.fx.calendrier_gif.business.GifTeleverse;

public interface GifTeleverseDao extends JpaRepository<GifTeleverse, Long> {

}
