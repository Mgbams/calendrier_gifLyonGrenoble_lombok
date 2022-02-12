package fr.orsys.fx.calendrier_gif.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.orsys.fx.calendrier_gif.business.Gif;
import fr.orsys.fx.calendrier_gif.business.Jour;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;

public interface GifDao extends JpaRepository<Gif, Long> {

	int countByUtilisateur(Utilisateur utilisateur);

	List<Gif> findByLegendeContaining(String legende);

	Gif findByJour(Jour jour);

	@Query(value = "SELECT r.gif FROM Reaction r GROUP BY r.gif ORDER BY count(r) DESC, r.dateHeure DESC")
	List<Gif> findTopByReactions();

	Gif findLast1ByJour(Jour jour);
	
	@Query(value = "SELECT r.gif FROM Reaction r GROUP BY r.gif ORDER BY count(r) DESC, r.dateHeure DESC")
	List<Gif> recupererGifsParNbReactions();
}
