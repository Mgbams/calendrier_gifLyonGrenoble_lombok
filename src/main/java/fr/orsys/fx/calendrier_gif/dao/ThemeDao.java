package fr.orsys.fx.calendrier_gif.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fr.orsys.fx.calendrier_gif.business.Theme;

@RepositoryRestResource(exported = true)
public interface ThemeDao extends JpaRepository<Theme, Long> {

	Theme findByNom(String nom);

}
