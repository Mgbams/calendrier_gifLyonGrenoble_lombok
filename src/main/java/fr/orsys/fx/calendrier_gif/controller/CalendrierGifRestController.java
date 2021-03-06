package fr.orsys.fx.calendrier_gif.controller;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.orsys.fx.calendrier_gif.business.Emotion;
import fr.orsys.fx.calendrier_gif.business.Gif;
import fr.orsys.fx.calendrier_gif.business.Jour;
import fr.orsys.fx.calendrier_gif.business.Reaction;
import fr.orsys.fx.calendrier_gif.business.Theme;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.exception.JourDejaPresentException;
import fr.orsys.fx.calendrier_gif.service.EmotionService;
import fr.orsys.fx.calendrier_gif.service.GifService;
import fr.orsys.fx.calendrier_gif.service.JourService;
import fr.orsys.fx.calendrier_gif.service.ReactionService;
import fr.orsys.fx.calendrier_gif.service.ThemeService;
import fr.orsys.fx.calendrier_gif.service.UtilisateurService;

@RestController
@RequestMapping("/api")
public class CalendrierGifRestController {
	private final JourService jourService;
	private final UtilisateurService utilisateurService;
	private final ThemeService themeService;
	private final GifService gifService;
	private final EmotionService emotionService;
	private final ReactionService reactionService;

	public CalendrierGifRestController(JourService jourService, UtilisateurService utilisateurService,
			ThemeService themeService, GifService gifService, EmotionService emotionService,
			ReactionService reactionService) {
		super();
		this.jourService = jourService;
		this.utilisateurService = utilisateurService;
		this.themeService = themeService;
		this.gifService = gifService;
		this.emotionService = emotionService;
		this.reactionService = reactionService;
	}

	// A) une m??thode pour ajouter un jour au calendrier (en pr??cisant la date au
	// format yyyy-MM-dd)
	@PostMapping(value = "/jours/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Jour ajouterJour(@DateTimeFormat(pattern = "yyyy-MM-dd") @PathVariable LocalDate date)
			throws JourDejaPresentException {
		return jourService.ajouterJour(date);
	}

	// @ResponseBody
	@ExceptionHandler(JourDejaPresentException.class)
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public String traiterJourDejaPresentException() {
		return "le jour est d??j?? pr??sent";
	}

	/*
	 * B) une m??thode pour ajouter un utilisateur (en pr??cisant dans le corps de la
	 * requ??te son nom, son pr??nom, son email, son mot de passe et le nom du th??me)
	 */
	// B) une m??thode pour ajouter un utilisateur
	@PostMapping(value = "utilisateurs/")
	public Utilisateur ajouterUtilisateur(@RequestBody @Valid @ModelAttribute Utilisateur utilisateur,
			BindingResult result) {

		if (result.hasErrors()) {
			return null;
		} else {
			return utilisateurService.enregistrerUtilisateur(utilisateur);
		}
	}

	/**
	 * Annotation d???une m??thode ???montrant??? ?? Spring comment obtenir un objet m??tier
	 * ?? partir de son id ou comment transformer un objet en un autre objet
	 * 
	 */
	// Apprend ?? Spring ?? convertir un nom de theme en objet de type Theme
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Theme.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String nom) throws IllegalArgumentException {
				setValue((nom.equals("")) ? null : themeService.recupererTheme(nom));
			}
		});
	}

	/*
	 * une m??thode pour r??cup??rer le gif d'un jour (en pr??cisant la date au format
	 * yyyy-MM-dd)
	 */
	// http://localhost:8080/RestAPI/jours/2022-01-01/gif
	@GetMapping("jours/{dateId}/gif")
	public Gif recupererGifJour(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateId) {
		return jourService.recupererJour(dateId).getGif();
	}

	// D) une m??thode pour mettre ?? jour la l??gende d'un gif
	@PatchMapping("/gif/{id}/{legende}")
	public Gif updateGif(@PathVariable Long id, @PathVariable String legende) {
		Gif gif = gifService.recupererGif(id);
		return gifService.mettreAJourLegende(gif, legende);
	}

	/*
	 * E) une m??thode pour ajouter une ??motion (en pr??cisant dans le corps de la
	 * requ??te le nom et l'unicode)
	 */
	@PostMapping("/emotion")
	public Emotion postEmotion(@RequestBody Emotion emotion) {
		Emotion emotionSaved = this.emotionService.ajouterEmotion(emotion);
		return emotionSaved;
	}

	/*
	 * F) une m??thode pour r??cup??rer les r??actions d???un Gif (en pr??cisant la date au
	 * format yyyy-MM-dd)
	 */

	@GetMapping("/jour/{date}/reactions")
	public List<Reaction> recupererReactionEnFonctionDate(
			@PathVariable @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
		return jourService.recupererJour(date).getGif().getReactions();
	}
	
	/* G) une m??thode pour supprimer une r??action (en pr??cisant son id, son email et la
			date) */
	
	@DeleteMapping("/reaction/{id}/{email}/{date}")
	public void supprimerReaction(
			@PathVariable @DateTimeFormat(iso = ISO.DATE) Date date, @PathVariable String email, @PathVariable Long id) {
		reactionService.supprimerReaction(id, date, email);
	}
	
	/*
	* G) une m??thode pour supprimer une r??action (en pr??cisant son id, son email et
	* la date)
	*/
//	@DeleteMapping("/reaction")
//	public void supprimerReaction(@RequestBody Reaction reaction) {
//	  reactionService.supprimerReaction(reaction.getId(), reaction.getGif().getJour().getDate(),     reaction.getUtilisateur().getEmail());
//	}
	
	// H) une m??thode pour r??cup??rer tous les Gif d???un utilisateur
	@GetMapping("/utilisateurs/{id}/gifs")
	public List<Gif> recupererGifsParUtilisateur(
			@PathVariable Long id) { 
		return utilisateurService.recupererUtilisateur(id).getGifs();
	}
	
	 /*
     * H) une m??thode pour r??cup??rer tous les Gif d???un utilisateur
     */
//    @GetMapping("/utilisateurs/{id}/gifs")
//    public Page<Gif> recupererGifsUtilisateur(@PathVariable Long id, @PageableDefault(page=0, size=10) Pageable pageable) {
//        Utilisateur utilisateur = utilisateurService.recupererUtilisateur(id);
//        return gifService.recupererGifsParUtilisateur(utilisateur, pageable);
//    }
	
	/* I) une m??thode pour r??cup??rer les Gif dont la l??gende contient le mot pr??cis?? dans
	l???URL */
	@GetMapping("/gifs")
	public List<Gif> recupererGifsParLegende(
			@RequestParam String legende) { 
		return gifService.recupererGifsParLegende(legende);
	}
	
	/* J) une m??thode permettant d???obtenir le Gif ayant eu le plus grand nombre de r??actions. S???il
	y a plusieurs Gif avec le plus grand nombre de r??actions, envoyer le plus r??cent. */
	  // J) une m??thode permettant d???obtenir le Gif ayant eu le plus grand nombre de
    // r??actions. S???il y a plusieurs Gif avec le plus grand nombre de r??actions,
    // envoyer le plus r??cent
    @GetMapping("topGif")
    public Gif recupererTopGif() {
        if (!gifService.recupererGifsParNbReactions().isEmpty()) {
            Gif gif = gifService.gifParNbReactions().get(0);
            return gif;
        }
        return null;
    }
	
	

}
