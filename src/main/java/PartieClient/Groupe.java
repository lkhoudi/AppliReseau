/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PartieClient;

/**
 *
 * @author khoudi
 */
public class Groupe {
     private String label;
     private String theme;
     private String level;
     public Groupe(String label, String theme){
         this.label=label;
         this.theme=theme;
     }
     
     private void setAttribut(String label, String theme){
         this.label=label;
         this.theme=theme;
     }

     public void setTheme(String theme) {
         this.theme = theme;
     }

     public void setLevel(String level) {
         this.level = level;
     }

     public void setLabel(String label) {
         this.label = label;
     }

     public String getTheme() {
         return theme;
     }

     public String getLevel() {
         return level;
     }

     public String getLabel() {
         return label;
     }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((theme == null) ? 0 : theme.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		Groupe other = (Groupe) obj;
		
		return label.equals(other.getLabel())&&theme.equals(other.getTheme());
	}
    
	
     
}
