package fr.orsys.fx.calendrier_gif.service;

import java.util.List;

import fr.orsys.fx.calendrier_gif.business.Theme;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.util.NbInscrits;

public interface UtilisateurService {

	Utilisateur enregistrerUtilisateur(Utilisateur utilisateur);
	
	Utilisateur ajouterUtilisateur(String nom, String prenom, String email, String motDePasse, Theme recupererTheme);

	Utilisateur recupererUtilisateur(String email, String motDePasse);

	List<Utilisateur> recupererUtilisateursAyantReagiAuMoinsCinqFois();

	Utilisateur recupererUtilisateur(Long id);

	Utilisateur ajouterUtilisateurAleatoire();
	
	List<NbInscrits> recupererNbInscrits();

	long compterUtilisateurs();

	Utilisateur recupererUtilisateur(String username);
}
