package org.wh.tv_fcn;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by W3B on 22/09/2018.
 */
public class Linguist {
    private ResourceBundle bundle;
    private String path;

    public Linguist(String path, Locale locale){
        this.path = path;
        this.bundle = ResourceBundle.getBundle(path,locale);
    }

    public String tr(String key){
        String chaine;
        try{
            chaine = bundle.getString(key);
        }
        catch (MissingResourceException e){
            chaine = key;
            System.out.println("Exception dans la méthode tr de la classe Linguist : \n"+e.getMessage());
        }
        return chaine;
    }

    public void setLocale(Locale locale){
        bundle = ResourceBundle.getBundle(path,locale);
    }
}
