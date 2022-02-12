package fr.orsys.fx.calendrier_gif.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import fr.orsys.fx.calendrier_gif.business.Utilisateur;
import fr.orsys.fx.calendrier_gif.util.NbInscrits;

public interface UtilisateurDao extends JpaRepository<Utilisateur, Long> {

	// Spring Data : requête par dérivation
	Utilisateur findLastByEmailAndMotDePasse(String email, String motDePasse);

	Utilisateur findByIdAndEmail(Long id, String email);
	
	Utilisateur findByEmail(String email);
	
	Utilisateur findByNom(String nom);

	// ajout d'une méthode annotée @Query
	@Query("""
			SELECT u
			FROM Utilisateur u, Reaction r
			WHERE r.utilisateur=u
			GROUP BY r.utilisateur
			HAVING COUNT(r.utilisateur)>=5
			""")
	List<Utilisateur> findUtilisateursHavingAtLeastFiveReactions();
	
	@Query("""
			FROM Utilisateur
			WHERE theme.nom = 'Dark'
			""")
	List<Utilisateur> findUtilisateursUsingDarkTheme();
	
	/**
	 * Cette méthode renvoie le nombre d'inscrits par année et par mois
	 * Elle utilise la classe util NbInscrits
	 */
	// Big up Oleg qui nous a présentés une nouveauté de Java 15 : text block
	@Query(value="""
			SELECT new fr.orsys.fx.calendrier_gif.util.NbInscrits(year(utilisateur.dateHeureInscription), 			month(utilisateur.dateHeureInscription), count(utilisateur))  
			FROM Utilisateur utilisateur 
			GROUP BY year(utilisateur.dateHeureInscription), month(utilisateur.dateHeureInscription)
			ORDER BY year(utilisateur.dateHeureInscription) desc, month(utilisateur.dateHeureInscription)
			""")
	List<NbInscrits> findNbInscrits();
}
