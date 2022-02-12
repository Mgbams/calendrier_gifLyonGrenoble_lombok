package fr.orsys.fx.calendrier_gif.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import fr.orsys.fx.calendrier_gif.business.Emotion;


@SpringBootTest
public class EmotionServiceImplTest {

	@Autowired
	private EmotionService emotionService;

	@Test
	public void testerAjout() {
		String nomEmotion = "Test";
		String codeEmotion = "&#x1F123;";
		Emotion emotion = emotionService.ajouterEmotion(nomEmotion, codeEmotion);
		assertNotNull(emotion);
		assertNotNull(emotion.getId());
		assertTrue(emotion.getId()>0);
		assertNotNull(emotion.getNom());
		assertEquals(emotion.getNom(), nomEmotion);
	}

}