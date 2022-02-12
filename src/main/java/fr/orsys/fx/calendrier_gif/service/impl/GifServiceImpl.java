package fr.orsys.fx.calendrier_gif.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.orsys.fx.calendrier_gif.business.Gif;
import fr.orsys.fx.calendrier_gif.business.GifDistant;
import fr.orsys.fx.calendrier_gif.business.GifTeleverse;
import fr.orsys.fx.calendrier_gif.business.Jour;
import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.dao.GifDao;
import fr.orsys.fx.calendrier_gif.dao.GifDistantDao;
import fr.orsys.fx.calendrier_gif.dao.GifTeleverseDao;
import fr.orsys.fx.calendrier_gif.dao.UtilisateurDao;
import fr.orsys.fx.calendrier_gif.service.GifService;
import fr.orsys.fx.calendrier_gif.service.JourService;

@Service
public class GifServiceImpl implements GifService {

	@Autowired
	private JourService jourService;

	@Autowired
	private GifDao gifDao;

	@Autowired
	private GifDistantDao gifDistantDao;

	@Autowired
	private GifTeleverseDao gifTeleverseDao;

	@Autowired
	private UtilisateurDao utilisateurDao;
	
	@Override
	public GifDistant ajouterGifDistant(LocalDate idJour, String url, Utilisateur utilisateur) {
		GifDistant gifDistant = new GifDistant();
		Jour jour = jourService.recupererJour(idJour);
		gifDistant.setJour(jour);
		gifDistant.setUrl(url);
		gifDistant.setUtilisateur(utilisateur);
		gifDistant = gifDistantDao.save(gifDistant);
		jour.setGif(gifDistant);
		jourService.enregistrerJour(jour);
		// Met à jour le solde de l'utilisateur
		utilisateur.setNbPoints(utilisateur.getNbPoints()-jour.getNbPoints());
		utilisateurDao.save(utilisateur);
		return gifDistant;
	}

	@Override
	public GifDistant ajouterGifDistant(GifDistant gifDistant, Utilisateur utilisateur) {
		gifDistant = gifDistantDao.save(gifDistant);
		gifDistant.setUtilisateur(utilisateur);
		Jour jour = jourService.recupererJour(gifDistant.getJour().getDate());
		jour.setGif(gifDistant);
		jourService.enregistrerJour(jour);
		// Met à jour le solde de l'utilisateur
		utilisateur.setNbPoints(utilisateur.getNbPoints()-jour.getNbPoints());
		utilisateurDao.save(utilisateur);
		return gifDistant;
	}

	@Override
	public Gif recupererGif(Long idGif) {
		if (gifDistantDao.findById(idGif)!=null) {
			return gifDistantDao.findById(idGif).orElse(null);
		}
		if (gifTeleverseDao.findById(idGif)!=null) {
			return gifTeleverseDao.findById(idGif).orElse(null);
		}
		return null;
	}

	@Override
	public Gif mettreAJourLegende(Gif gif, String nouvelleLegende) {
		gif.setLegende(nouvelleLegende);
		gifDao.save(gif);
		return gif;
	}

	@Override
	public Gif recupererGifParJour(Jour jour) {
		return gifDao.findLast1ByJour(jour);
	}

	@Override
	public List<Gif> recupererGifsParLegende(String legende) {
		return gifDao.findByLegendeContaining(legende);
	}

	@Override
	public List<Gif> gifParNbReactions() {
		// TODO Auto-generated method stub
		return gifDao.findTopByReactions();
	}

//	@Override
//	public GifTeleverse ajouterGifTeleverse(String legende, String fileName, Utilisateur utilisateur) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	@Override
	public GifTeleverse ajouterGifTeleverse(GifTeleverse gifTeleverse, Utilisateur utilisateur) {
		gifTeleverse = gifTeleverseDao.save(gifTeleverse);
		gifTeleverse.setUtilisateur(utilisateur);
		Jour jour = jourService.recupererJour(gifTeleverse.getJour().getDate());
		jour.setGif(gifTeleverse);
		jourService.enregistrerJour(jour);
		// Met à jour le solde de l'utilisateur
		utilisateur.setNbPoints(utilisateur.getNbPoints()-jour.getNbPoints());
		utilisateurDao.save(utilisateur);
		return gifTeleverse;
	}

	 @Override
	    public List<Gif> recupererGifsParNbReactions() {
	        return gifDao.findTopByReactions();
	    }


}