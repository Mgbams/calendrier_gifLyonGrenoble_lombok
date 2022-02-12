package fr.orsys.fx.calendrier_gif.controller;

import java.time.LocalDate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Controller;

import fr.orsys.fx.calendrier_gif.exception.JourDejaPresentException;
import fr.orsys.fx.calendrier_gif.service.EmotionService;
import fr.orsys.fx.calendrier_gif.service.JourService;
import fr.orsys.fx.calendrier_gif.service.ThemeService;
import fr.orsys.fx.calendrier_gif.service.UtilisateurService;

@Controller
public class InitController {

	private final JourService jourService;
	private final EmotionService emotionService;
	private final UtilisateurService utilisateurService;
	private final ThemeService themeService;
	
	public InitController(JourService jourService, EmotionService emotionService, UtilisateurService utilisateurService,
			ThemeService themeService) {
		super();
		this.jourService = jourService;
		this.emotionService = emotionService;
		this.utilisateurService = utilisateurService;
		this.themeService = themeService;
	}

	@PostConstruct
	private void init() throws JourDejaPresentException {

		if (themeService.recupererThemes().isEmpty()) {
			themeService.ajouterTheme("Bachata");
			themeService.ajouterTheme("Dark");
		}
		
		if (emotionService.recupererEmotions().isEmpty()) {			
			emotionService.ajouterEmotion("Souriant", "&#x1F600;");
			emotionService.ajouterEmotion("Monocle", "&#x1F9D0;");
			emotionService.ajouterEmotion("Bisous", "&#x1F618;");
			emotionService.ajouterEmotion("Coeur", "&#x1F60D;");
			emotionService.ajouterEmotion("PTDR", "&#x1F923;");
		}

		if (jourService.compterJours()==0) {
			int anneeEnCours = LocalDate.now().getYear();
	        int moisEnCours = LocalDate.now().getMonthValue();
			LocalDate localDate = LocalDate.of(anneeEnCours, moisEnCours, 1);
			int nbJoursDuMoisEnCours = localDate.lengthOfMonth();
			for (int i = 1; i <= nbJoursDuMoisEnCours; i++) {
				jourService.ajouterJour(localDate);
				localDate = localDate.plusDays(1);
			}
		}
				
		if (utilisateurService.compterUtilisateurs() == 0) {
			utilisateurService.ajouterUtilisateur("DURAND", "Delphine", "d@orsys.fr", "123", themeService.recupererTheme("Dark"));
		}

	}
	
	@PreDestroy
	private void goodbye() {
		
	}
	
}
