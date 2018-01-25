import java.util.*;
import java.lang.*;
import java.io.*;

public class Recipe {
    public int rid;//id in react native JSON
    
    public Ingredients ingredients = new Ingredients();

    public double rating=-1;

    public String steps = "Directions: ";

    public String rname;//title in react native JSON

    public String imageurl;

    public String url;

    public String cooktime;

    public String serving;

    public int missing;

    public Recipe(){
    }

    public Recipe(int initRid, String initRname, Ingredients initingredients, int initmissing, String initsource, String initimageurl){
    	rid = initRid;
    	rname = initRname;
    	ingredients = initingredients;
    	missing = initmissing;
        url = initsource;
        imageurl = initimageurl;
    }
}
