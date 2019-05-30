package entities;

public class EtatGame {

	private String etat;
	
	public EtatGame() {
		etat="repos";
	}
	
	public boolean estRepos() {
		return etat.equals("repos");
	}
	
	public boolean estPret() {
		return etat.equals("pret");
	}
	public boolean estFini() {
		return etat.equals("fini");
	}
	public boolean estEnCours() {
		return etat.equals("encours");
	}
	public void setEtat(String value) {
		etat=value;
	}
	
	@Override 
	public String toString() {
		return etat;
	}
}
