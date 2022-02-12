package fr.orsys.fx.calendrier_gif.service;

import java.util.List;

import fr.orsys.fx.calendrier_gif.business.Theme;

public interface ThemeService {

	Theme ajouterTheme(String nom);

	List<Theme> recupererThemes();
	
	Theme recupererTheme(Long id);

	Theme recupererTheme(String string);
}
