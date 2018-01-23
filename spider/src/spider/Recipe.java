package spider;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Recipe {
    public static int ridtracker = 1;

    public int rid;
    
    public List<Ingredient> inglist = new ArrayList<Ingredient>();

    public double rating;

    public List<String> steps = new ArrayList<String>();

    public String rname;

    public String imageurl;

    public Recipe() {
	this.rid = this.ridtracker;
	this.ridtracker += 1;
    }
}
