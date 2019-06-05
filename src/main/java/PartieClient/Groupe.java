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
     public Groupe(String label, String theme){
         this.label=label;
         this.theme=theme;
     }
     
     public String getTheme(){
         return theme;
     }
     
     public String getLabel(){
         return label;
     }
}
