package fr.orsys.fx.calendrier_gif.util;

public class NbInscrits {

	private int annee;
	private int mois;
	private long nbInscrits;
	
	public NbInscrits() {
		// TODO Auto-generated constructor stub
	}

	public NbInscrits(int annee, int mois, long nbInscrits) {
		super();
		this.annee = annee;
		this.mois = mois;
		this.nbInscrits = nbInscrits;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getMois() {
		return mois;
	}

	public void setMois(int mois) {
		this.mois = mois;
	}

	public long getNbInscrits() {
		return nbInscrits;
	}

	public void setNbInscrits(long nbInscrits) {
		this.nbInscrits = nbInscrits;
	}

	@Override
	public String toString() {
		return "NbInscrits [annee=" + annee + ", mois=" + mois + ", nbInscrits=" + nbInscrits + "]";
	}
	
}