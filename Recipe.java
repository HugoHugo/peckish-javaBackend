import java.util.*;
import java.lang.*;
import java.io.*;

public class Recipe {
    public int rid;//id in react native JSON
    
    public Ingredients ingredients = new Ingredients();
    //public List<Ingredient> inglist = new ArrayList<Ingredient>();

    public double rating;

    public List<String> steps = new ArrayList<String>();

    public String rname;//title in react native JSON

    public String imageurl;//imagePath in react native JSON

    public String source;//source in react native JSON

    //Number of ingredients missing. Only used if creating a JSON to be returned to the user
    public int NoIngMiss; //missing  in react native JSON

    public Recipe(int initRid, String initRname, Ingredients initingredients, int initmissing){
    	rid = initRid;
    	rname = initRname;
    	ingredients = initingredients;
    	NoIngMiss = initmissing;
    }
}
