package fr.orsys.fx.calendrier_gif.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fr.orsys.fx.calendrier_gif.business.Jour;

@RepositoryRestResource(exported = false)
public interface JourDao extends JpaRepository<Jour, LocalDate> {

}
