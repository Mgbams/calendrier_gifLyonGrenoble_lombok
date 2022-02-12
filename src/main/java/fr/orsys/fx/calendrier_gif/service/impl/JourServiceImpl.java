package fr.orsys.fx.calendrier_gif.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.orsys.fx.calendrier_gif.business.Jour;
import fr.orsys.fx.calendrier_gif.dao.JourDao;
import fr.orsys.fx.calendrier_gif.exception.JourDejaPresentException;
import fr.orsys.fx.calendrier_gif.service.JourService;

@Service
public class JourServiceImpl implements JourService {

	private JourDao jourDao;

	private static Random random;

	public JourServiceImpl(JourDao jourDao) {
		this.jourDao = jourDao;
		random = new Random();
	}

//	@Override
//	public Jour ajouterJour(LocalDate date) {
//		return jourDao.save(new Jour(date, 20 + random.nextInt(31)));
//	}

	@Override
	public Jour enregistrerJour(Jour jour) {
		return jourDao.save(jour);
	}

	@Override
	public Page<Jour> recupererJours(Pageable pageable) {
		return jourDao.findAll(pageable);
	}

	@Override
	public List<Jour> recupererJours() {
		return jourDao.findAll();
	}

	@Override
	public Jour recupererJour(LocalDate id) {
		return jourDao.findById(id).orElse(null);
	}

	@Override
	public boolean supprimerJour(LocalDate date) {
		Jour jour = recupererJour(date);
		if (jour == null) {
			return false;
		}
		jourDao.delete(jour);
		return true;
	}

	@Override
	public long compterJours() {
		return jourDao.count();
	}

	@Override
	public Jour ajouterJour(LocalDate date) throws JourDejaPresentException {
		if (recupererJour(date) != null) {
			throw new JourDejaPresentException();
		}
		return jourDao.save(new Jour(date, 20 + random.nextInt(31)));
	}

}