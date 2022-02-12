package fr.orsys.fx.calendrier_gif.service;

import java.util.List;

import fr.orsys.fx.calendrier_gif.business.Emotion;

public interface EmotionService {

	Emotion ajouterEmotion(String nom, String code);

	List<Emotion> recupererEmotions();
	
	Emotion recupererEmotion(Long id);
	
	Emotion recupererEmotion(String nom);

	Emotion ajouterEmotion(Emotion emotion);
}
