package fr.orsys.fx.calendrier_gif.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import fr.orsys.fx.calendrier_gif.business.Emotion;
import fr.orsys.fx.calendrier_gif.dao.EmotionDao;
import fr.orsys.fx.calendrier_gif.service.EmotionService;

@Component // Spring va déduire que cette classe est un service
@Primary
public class EmotionServiceImpl implements EmotionService {

	@Autowired //: magnifique mais plus vraiment au goût du jour
	//@Inject equivalent du autowired dans Jakarta EE (CDI)
	private EmotionDao emotionDao;
	
	@Override
	public Emotion ajouterEmotion(String nom, String code) {
		return emotionDao.save(new Emotion(nom, code));
	}

	@Override
	public List<Emotion> recupererEmotions() {
		return emotionDao.findAll();
	}

	@Override
	public Emotion recupererEmotion(Long id) {
		return emotionDao.findById(id).orElse(null);
	}

	@Override
	public Emotion recupererEmotion(String nom) {
		return emotionDao.findByNom(nom);
	}

	@Override
	public Emotion ajouterEmotion(Emotion emotion) {
		return emotionDao.save(emotion);
	}

}
