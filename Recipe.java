package spider;

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
}
