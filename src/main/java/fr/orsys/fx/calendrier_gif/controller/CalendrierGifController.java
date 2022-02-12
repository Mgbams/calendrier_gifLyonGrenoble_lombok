package fr.orsys.fx.calendrier_gif.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import fr.orsys.fx.calendrier_gif.business.GifDistant;
import fr.orsys.fx.calendrier_gif.business.GifTeleverse;
import fr.orsys.fx.calendrier_gif.business.Jour;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.service.EmotionService;
import fr.orsys.fx.calendrier_gif.service.GifService;
import fr.orsys.fx.calendrier_gif.service.JourService;
import fr.orsys.fx.calendrier_gif.service.ReactionService;
import fr.orsys.fx.calendrier_gif.service.ThemeService;
import fr.orsys.fx.calendrier_gif.service.UtilisateurService;

@Controller
public class CalendrierGifController {

	private static final int NB_JOURS_PAR_PAGE = 7;

	private final JourService jourService;
	private final EmotionService emotionService;
	private final UtilisateurService utilisateurService;
	private final ThemeService themeService;
	private final GifService gifService;
	private final ReactionService reactionService;
	private final HttpSession httpSession;
	private static final String DOSSIER_IMAGES = "src/main/webapp/images/";

	// Constructeur avec tous les services que Spring doit injecter
	public CalendrierGifController(JourService jourService, EmotionService emotionService,
			UtilisateurService utilisateurService, ThemeService themeService, GifService gifService,
			ReactionService reactionService, HttpSession httpSession) {
		super();
		this.jourService = jourService;
		this.emotionService = emotionService;
		this.utilisateurService = utilisateurService;
		this.themeService = themeService;
		this.gifService = gifService;
		this.reactionService = reactionService;
		this.httpSession = httpSession;
	}

	@RequestMapping(value = { "/index", "/" })
	public ModelAndView accueil() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index");
		mav.addObject("utilisateurs", utilisateurService.recupererUtilisateursAyantReagiAuMoinsCinqFois());

		// deux écritures équivalentes :
		// mav.getModel().put("nbInscrits", utilisateurService.recupererNbInscrits());
		mav.addObject("nbInscrits", utilisateurService.recupererNbInscrits());
		return mav;
	}

	// This method is now handled by spring security when we configure spring
	// security in our project
//	@PostMapping("/connexion")
//	public ModelAndView connexion(@RequestParam("EMAIL") String email, @RequestParam("MOT_DE_PASSE") String motDePasse) {
//		Utilisateur utilisateur = utilisateurService.recupererUtilisateur(email, motDePasse);
//		if (utilisateur==null) {
//			ModelAndView mav = accueil();
//			mav.addObject("notification", "Mot de passe et/ou email incorrect");
//			return mav;
//		}
//		else {
//			// On ajoute en session HTTP l'objet utilisateur
//			httpSession.setAttribute("utilisateur", utilisateur);
//			ModelAndView mav = new ModelAndView("redirect:calendrier");
//			return mav;
//		}
//	}

	@GetMapping("/calendrier")
	public ModelAndView calendrier(@PageableDefault(size = NB_JOURS_PAR_PAGE, sort = "date") Pageable pageable) {
//		if (httpSession.getAttribute("utilisateur")==null) {
//			return accueil();
//		}

		// Récuperer l'utilisateur
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utilisateur utilisateur = utilisateurService.recupererUtilisateur(user.getUsername());

		ModelAndView mav = new ModelAndView("calendrier");
		mav.addObject("utilisateur", utilisateur);
		mav.addObject("pageDeJours", jourService.recupererJours(pageable));
		// Met en session la page choisie
		if (pageable != null) {
			httpSession.setAttribute("numeroDePage", pageable.getPageNumber());
		}
		return mav;
	}

	@GetMapping("/placerGifDistant")
	public ModelAndView placerGifDistantGet(
			@RequestParam("ID_JOUR") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate idJour,
			@ModelAttribute GifDistant gifDistant) {
		ModelAndView mav = new ModelAndView("placerGifDistant");
		Jour jour = jourService.recupererJour(idJour);
		gifDistant.setJour(jour);
		mav.addObject("gifDistant", gifDistant);

		// Récuperer l'utilisateur
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utilisateur utilisateur = utilisateurService.recupererUtilisateur(user.getUsername());
		mav.addObject("utilisateur", utilisateur);
		return mav;
	}

	@PostMapping("/placerGifDistant")
	public ModelAndView placerGifDistantPost(@Valid @ModelAttribute GifDistant gifDistant, BindingResult result) {
		if (result.hasErrors()) {
			ModelAndView mav = placerGifDistantGet(gifDistant.getJour().getDate(), gifDistant);
			mav.addObject("gifDistant", gifDistant);
			return mav;
		} else {
			// On vérifie qu'il n'y a pas déja un gif sur ce jour
			if (gifDistant.getJour().getGif() == null) {
				gifService.ajouterGifDistant(gifDistant, (Utilisateur) httpSession.getAttribute("utilisateur"));
			}
			// return new ModelAndView("redirect:calendrier");
			if (httpSession.getAttribute("numeroDePage") != null) {
				return new ModelAndView("redirect:calendrier?page=" + httpSession.getAttribute("numeroDePage"));
			} else {
				return new ModelAndView("redirect:calendrier");
			}
		}
	}

	@GetMapping("/reagir")
	public ModelAndView reagirGet(@RequestParam("ID_GIF") Long idGif) {
		// Récuperer l'utilisateur
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utilisateur utilisateur = utilisateurService.recupererUtilisateur(user.getUsername());

		ModelAndView mav = new ModelAndView("reagir");
		mav.addObject("gif", gifService.recupererGif(idGif));
		mav.addObject("emotions", emotionService.recupererEmotions());
		mav.addObject("utilisateur", utilisateur);
		return mav;
	}

	@PostMapping("/reagir")
	public ModelAndView reagirPost(@RequestParam("ID_GIF") Long idGif, @RequestParam("ID_EMOTION") Long idEmotion) {
		reactionService.ajouterReaction(idGif, idEmotion, (Utilisateur) httpSession.getAttribute("utilisateur"));
		if (httpSession.getAttribute("numeroDePage") != null) {
			return new ModelAndView("redirect:calendrier?page=" + httpSession.getAttribute("numeroDePage"));
		} else {
			return new ModelAndView("redirect:calendrier");
		}
	}

	// This method is now handled by spring security when we configure spring
	// security in our project
//	@GetMapping("/deconnexion")
//	public ModelAndView deconnexion() {
//		httpSession.invalidate();
//		ModelAndView mav = new ModelAndView("redirect:index");
//		mav.addObject("notification", "Au revoir");
//		return mav;
//	}

	@GetMapping("/inscription")
	public ModelAndView inscriptionGet(@ModelAttribute Utilisateur utilisateur) {
		ModelAndView mav = new ModelAndView("inscription");
		// On envoie à la vue la liste exhaustive des thèmes
		mav.addObject("themes", themeService.recupererThemes());
		return mav;
	}

	@GetMapping("/placerGifTeleverser")
	public ModelAndView placerGifTeleverserGet(
			@RequestParam("ID_JOUR") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate idJour) {
		ModelAndView mav = new ModelAndView("placerGifTeleverse");
		Jour jour = jourService.recupererJour(idJour);

		// Récuperer l'utilisateur
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utilisateur utilisateur = utilisateurService.recupererUtilisateur(user.getUsername());

		mav.addObject("jour", jour);
		mav.addObject("utilisateur", utilisateur);
		return mav;
	}

	@PostMapping("/placerGifTeleverser")
	public ModelAndView placerGifTeleverserPost(@RequestParam("LEGENDE") String legende,
			@RequestParam("ID_JOUR") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("FICHIER") MultipartFile multipartFile) throws IOException {

		// Récuperer l'utilisateur
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utilisateur utilisateur = utilisateurService.recupererUtilisateur(user.getUsername());

		GifTeleverse gifTeleverse = new GifTeleverse();
		Jour jour = jourService.recupererJour(date);

		gifTeleverse.setJour(jour);
		gifTeleverse.setLegende(legende);
		gifTeleverse.setDateHeureAjout(LocalDateTime.now());

		gifTeleverse.setNomFichierOriginal(multipartFile.getOriginalFilename());

		enregisterFichier(multipartFile.getOriginalFilename(), multipartFile);

		gifService.ajouterGifTeleverse(gifTeleverse, utilisateur);

		return new ModelAndView("redirect:calendrier");
	}

	private static void enregisterFichier(String nom, MultipartFile multipartFile) throws IOException {
		Path chemin = Paths.get(DOSSIER_IMAGES);

		if (!Files.exists(chemin)) {
			Files.createDirectories(chemin);
		}

		try (InputStream inputStream = multipartFile.getInputStream()) {
			Path cheminFichier = chemin.resolve(nom);
			Files.copy(inputStream, cheminFichier, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Erreur d'écriture : " + nom, ioe);
		}

	}

	/**
	 * Cette méthode traite une requête HTTP dont la méthode est POST
	 * 
	 * @param utilisateur
	 * @param result
	 * @return
	 */
	@PostMapping("/inscription")
	public ModelAndView inscriptionPost(@Valid @ModelAttribute Utilisateur utilisateur, BindingResult result) {
		// Est-ce que Spring a trouvé des erreurs en validant l'objet utilisateur ?
		if (result.hasErrors()) {
			return inscriptionGet(utilisateur);
		} else {
			System.out.println("Ajout utilisateur");
			utilisateurService.enregistrerUtilisateur(utilisateur);
			// ModelAndView mav = accueil();
			ModelAndView mav = new ModelAndView("redirect:index");
			mav.addObject("notification", "Utilisateur ajouté");

			// Pour que l'utilisateur soit connecté automatiquement et redirigé vers le
			// calendrier
			// httpSession.setAttribute("utilisateur", utilisateur);
			// ModelAndView mav = new ModelAndView("redirect:calendrier");

			return mav;
		}
	}

	@GetMapping(value = "exportWord", produces = "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
	public void getSerieAuFormatWord(HttpServletResponse response) throws IOException {
		response.addHeader("Content-Disposition", "attachment; filename=Calendrier" + ".docx");
		response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

		List<Jour> jours = jourService.recupererJours();
		// Création du document Word
		XWPFDocument document = new XWPFDocument();

		// Entête
		XWPFHeader headerHeader = document.createHeader(HeaderFooterType.DEFAULT);
		XWPFParagraph headerPara = headerHeader.createParagraph();
		headerPara.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun header = headerPara.createRun();
		URL url = new URL("https://www.clelia.fr/images/Logo-ORSYS.png");
		try {
			header.addPicture(url.openStream(), Document.PICTURE_TYPE_PNG, "Logo.png", Units.toEMU(300),
					Units.toEMU(70));
		} catch (InvalidFormatException | IOException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Jours list " + jours);

		header.addBreak();
		header.setText("Nombre de points : " + "hello");
		header.setColor("0000ff");
		header.setFontSize(24);
		header.setBold(true);

		// Téléchargement du document généré
		OutputStream outStream = response.getOutputStream();
		document.write(outStream);
		outStream.close();
		document.close();

	}

}