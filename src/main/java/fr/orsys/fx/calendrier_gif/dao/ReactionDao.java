package fr.orsys.fx.calendrier_gif.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.orsys.fx.calendrier_gif.business.Emotion;
import fr.orsys.fx.calendrier_gif.business.Gif;
import fr.orsys.fx.calendrier_gif.business.Reaction;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;

public interface ReactionDao extends JpaRepository<Reaction, Long> {

	/**
	 * Méthode qui renvoie la liste des toutes les réactions du Gif donné en paramètre
	 * @param gif
	 * @return
	 */
	List<Reaction> findByGif(Gif gif);

	Reaction findLastByGifAndUtilisateurAndEmotion(Gif gif, Utilisateur utilisateur, Emotion emotion);

	List<Reaction> findByDateHeureBetween(LocalDateTime dateDebut, LocalDateTime dateFin);

	@Query("select r from Reaction r where r.id = :id and r.gif.jour.date >= :dateDebut and r.gif.jour.date < :dateFin and r.utilisateur.email like :email")
	List<Reaction> findFirstByIdAndDateAndUtilisateurEmailLike(@Param("id") Long id, @Param("dateDebut") LocalDateTime dateDebut, @Param("dateFin") LocalDateTime dateFin, @Param("email") String email);
	
	List<Reaction> findLast5ByGif(Gif gif);
	
	@Query("select r FROM Reaction r where CONVERT(DATE, r.dateHeure) = :dateAjout")
	List<Reaction> getReactionByDate(@Param("dateAjout")LocalDate dateAjout);  
}