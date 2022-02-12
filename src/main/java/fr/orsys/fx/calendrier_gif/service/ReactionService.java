package fr.orsys.fx.calendrier_gif.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import fr.orsys.fx.calendrier_gif.business.Gif;
import fr.orsys.fx.calendrier_gif.business.Reaction;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;

public interface ReactionService {

	Reaction ajouterReaction(Long idGif, Long idEmotion, Utilisateur utilisateur);
	
	List<Reaction> recupererReactions(Gif gif);
	
	boolean supprimerReaction(Long idGif, Long idEmotion, Long idUtilisateur, String email);

	boolean supprimerReaction(Long id, Date date, String email);

	List<Reaction> recupererReaction(Long id, Date date, String email);

	 List<Reaction> recupererReactionParDate(LocalDate dateAjout);

	
}