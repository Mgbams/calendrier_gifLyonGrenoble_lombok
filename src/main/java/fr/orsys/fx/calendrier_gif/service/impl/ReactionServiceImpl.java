package fr.orsys.fx.calendrier_gif.service.impl;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.orsys.fx.calendrier_gif.business.Emotion;
import fr.orsys.fx.calendrier_gif.business.Gif;
import fr.orsys.fx.calendrier_gif.business.Reaction;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.dao.EmotionDao;
import fr.orsys.fx.calendrier_gif.dao.ReactionDao;
import fr.orsys.fx.calendrier_gif.dao.UtilisateurDao;
import fr.orsys.fx.calendrier_gif.service.GifService;
import fr.orsys.fx.calendrier_gif.service.ReactionService;

@Service
public class ReactionServiceImpl implements ReactionService {

	@Autowired
	private ReactionDao reactionDao;

	@Autowired
	private EmotionDao emotionDao;

	@Autowired
	private UtilisateurDao utilisateurDao;

	@Autowired
	private GifService gifService;

	@Override
	public Reaction ajouterReaction(Long idGif, Long idEmotion, Utilisateur utilisateur) {
		Emotion emotion = emotionDao.findById(idEmotion).orElse(null);
		Gif gif = gifService.recupererGif(idGif);
		Reaction reaction = new Reaction();
		reaction.setGif(gif);
		reaction.setUtilisateur(utilisateur);
		reaction.setEmotion(emotion);
		reactionDao.save(reaction);
		return reaction;
	}

	@Override
	public List<Reaction> recupererReactions(Gif gif) {
	
		return reactionDao.findByGif(gif);
	}

	@Override
	public List<Reaction> recupererReaction(Long id, Date date, String email) {
		Calendar calendar = Calendar.getInstance();
		System.out.println(date);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date dateSuivante = calendar.getTime();
		System.out.println(dateSuivante);
		System.out.println(email);
		System.out.println(id);
		//System.out.println(reactionDao.findFirstByIdAndDateAndUtilisateurEmailLike(id, date, dateSuivante, email));
		//return reactionDao.findFirstByIdAndDateAndUtilisateurEmailLike(id, date, dateSuivante, email);
		// TODO à reprendre
		return null;
	}

	@Override
	public boolean supprimerReaction(Long id, Date date, String email) {
		List<Reaction> reactions = recupererReaction(id, date, email);
		if (!reactions.isEmpty()) {
			reactionDao.delete(reactions.get(0));
			return true;
		}
		return false;
	}

	@Override
	public boolean supprimerReaction(Long idGif, Long idEmotion, Long idUtilisateur, String email) {
		Reaction reaction = reactionDao.findLastByGifAndUtilisateurAndEmotion(gifService.recupererGif(idGif),
				utilisateurDao.findByIdAndEmail(idUtilisateur, email), emotionDao.findById(idEmotion).orElse(null));
		if (reaction == null) {
			return false;
		}
		reactionDao.delete(reaction);
		return true;
	}

	@Override
	public List<Reaction> recupererReactionParDate(LocalDate dateAjout) {
		return reactionDao.getReactionByDate(dateAjout);
		
	}

}