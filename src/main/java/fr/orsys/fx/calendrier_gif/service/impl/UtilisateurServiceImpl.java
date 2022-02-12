package fr.orsys.fx.calendrier_gif.service.impl;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import fr.orsys.fx.calendrier_gif.business.Theme;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.dao.UtilisateurDao;
import fr.orsys.fx.calendrier_gif.service.ThemeService;
import fr.orsys.fx.calendrier_gif.service.UtilisateurService;
import fr.orsys.fx.calendrier_gif.util.NbInscrits;

@Service
public class UtilisateurServiceImpl implements UtilisateurService, UserDetailsService {

	private final UtilisateurDao utilisateurDao;
	private final ThemeService themeService;
	private final PasswordEncoder passwordEncoder;

	public UtilisateurServiceImpl(UtilisateurDao utilisateurDao, ThemeService themeService,
			PasswordEncoder passwordEncoder) {
		super();
		this.utilisateurDao = utilisateurDao;
		this.themeService = themeService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Utilisateur enregistrerUtilisateur(Utilisateur utilisateur) {
		utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
		return utilisateurDao.save(utilisateur);
	}

	@Override
	public Utilisateur ajouterUtilisateur(String nom, String prenom, String email, String motDePasse, Theme theme) {
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom(nom);
		utilisateur.setPrenom(prenom);
		utilisateur.setEmail(email);
		utilisateur.setMotDePasse(passwordEncoder.encode(motDePasse));
		utilisateur.setTheme(theme);
		return utilisateurDao.save(utilisateur);
	}

	@Override
	public Utilisateur recupererUtilisateur(String email, String motDePasse) {
		return utilisateurDao.findLastByEmailAndMotDePasse(email, motDePasse);
	}

	@Override
	public List<Utilisateur> recupererUtilisateursAyantReagiAuMoinsCinqFois() {
		return utilisateurDao.findUtilisateursHavingAtLeastFiveReactions();
	}

	@Override
	public Utilisateur recupererUtilisateur(Long id) {
		return utilisateurDao.findById(id).orElse(null);
	}

	@Override
	public Utilisateur ajouterUtilisateurAleatoire() {
		FakeValuesService fakeValuesService = new FakeValuesService(new Locale("fr-FR"), new RandomService());

		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNom(fakeValuesService.letterify("?????"));
		utilisateur.setPrenom(fakeValuesService.letterify("?????"));
		utilisateur.setEmail(fakeValuesService.letterify("?????@orsys.fr"));
		utilisateur.setMotDePasse(passwordEncoder.encode(fakeValuesService.letterify("?????")));
		Faker faker = new Faker(new Locale("fr-FR"));
		Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 1, 1);
		Date dateDebut = calendar.getTime();
		calendar = Calendar.getInstance();
		Date dateFin = calendar.getTime();
		Date dateAleatoire = faker.date().between(dateDebut, dateFin);
		calendar.setTime(dateAleatoire);
		// Big up Moulaye !
		utilisateur.setDateHeureInscription(dateAleatoire.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		Random random = new Random();
		utilisateur.setTheme(themeService.recupererTheme(Long.valueOf(1 + random.nextInt(2))));
		utilisateurDao.save(utilisateur);
		return utilisateur;
	}

	@Override
	public List<NbInscrits> recupererNbInscrits() {
		return utilisateurDao.findNbInscrits();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		if (username.trim().isEmpty()) {
			throw new UsernameNotFoundException("username is empty");
		}

		Utilisateur utilisateur = utilisateurDao.findByEmail(username);
		if (utilisateur == null) {
			throw new UsernameNotFoundException("user " + username + " not found");
		}
		List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(utilisateur);
		User user = new User(utilisateur.getEmail(), utilisateur.getMotDePasse(), grantedAuthorities);
		System.out.println(user);
		return user;
	}

	private List<GrantedAuthority> getGrantedAuthorities(Utilisateur utilisateur) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		return authorities;
	}

	@Override
	public long compterUtilisateurs() {
		return utilisateurDao.count();
	}

	@Override
	public Utilisateur recupererUtilisateur(String username) {
		return utilisateurDao.findByEmail(username);
	}

}