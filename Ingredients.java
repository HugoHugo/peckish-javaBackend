import java.util.*;
import java.lang.*;
import java.io.*;

public class Ingredients {
    public static int iidtracker;

    public int iid;

    public List<String> ingredientnames = new ArrayList<String>();
    //public String iname;

    public Ingredients() {
	this.iid = this.iidtracker;
	this.iidtracker++;

    }
