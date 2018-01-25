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

    //Number of ingredients missing. Only used if creating a JSON to be returned to the user
    public int NoIngMiss;
    public Recipe(){
    }

    public Recipe(int initRid, String initRname, Ingredients initingredients, int initmissing, String initsource, String initimageurl){
    	rid = initRid;
    	rname = initRname;
    	ingredients = initingredients;
    	NoIngMiss = initmissing;
        url = initsource;
        imageurl = initimageurl;
    }
}
