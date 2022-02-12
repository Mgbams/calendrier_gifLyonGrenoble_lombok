package fr.orsys.fx.calendrier_gif.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import fr.orsys.fx.calendrier_gif.service.UtilisateurService;

@Controller
public class AutomatismeController {

	private final UtilisateurService utilisateurService;
	
	public AutomatismeController(UtilisateurService utilisateurService) {
		super();
		this.utilisateurService = utilisateurService;
	}


	@Scheduled(cron="* * * * * *")
	private void ajouterUtilisateurAleatoirement() {
		utilisateurService.ajouterUtilisateurAleatoire();
	}
}
