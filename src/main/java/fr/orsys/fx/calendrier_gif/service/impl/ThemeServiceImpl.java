package fr.orsys.fx.calendrier_gif.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.orsys.fx.calendrier_gif.business.Theme;
import fr.orsys.fx.calendrier_gif.dao.ThemeDao;
import fr.orsys.fx.calendrier_gif.service.ThemeService;

@Service
public class ThemeServiceImpl implements ThemeService {

	@Autowired
	private ThemeDao themeDao;
	
	@Override
	public Theme ajouterTheme(String nom) {
		return themeDao.save(new Theme(nom));
	}

	@Override
	public List<Theme> recupererThemes() {
		return themeDao.findAll();
	}

	@Override
	public Theme recupererTheme(Long id) {
		return themeDao.findById(id).orElse(null);
	}

	@Override
	public Theme recupererTheme(String nom) {
		return themeDao.findByNom(nom);
	}

}
