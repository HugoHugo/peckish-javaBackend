import java.util.*;
import java.lang.*;
import java.io.*;

public class Recipe {
    public int rid;
    
    public Ingredients ingredients = new Ingredients();
    //public List<Ingredient> inglist = new ArrayList<Ingredient>();

    public double rating;

    public List<String> steps = new ArrayList<String>();

    public String rname;

    public String imageurl;

    //Number of ingredients missing. Only used if creating a JSON to be returned to the user
    public int NoIngMiss;

    public Recipe(int initRid, String initRname, Ingredients initingredients, int initmissing){
    	rid = initRid;
    	rname = initRname;
    	ingredients = initingredients;
    	NoIngMiss = initmissing;
    }
}
