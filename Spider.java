import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.lang.*;
import java.io.*;

/**
 * Downloads web page content starting with a starting url, then moves 
 *  on to the next webpage for another recipe, until "maxUrls" recipes
 * have been found. Note that this Spider is specifically designed to
 * scrape recipes from allrecipes.com.
 * @author shilad, Brenden Ireland
 *
 */
public class Spider {
    /**
     * Urls waiting to be scraped.  The "work" left to do.
     */
    private Queue<String> work = new LinkedList<String>();
    
    /**
     * Keeps track of counts for each url.
     */
    private AllWordsCounter urlCounter = new AllWordsCounter();
    
    /**
     * Maximum number of urls that should be scraped.
     */
    private int maxUrls = 100;
    
    /**
     * URLs that have already been retrieved.
     */
    private List<String> finished = new ArrayList<String>();
    
    /**
     * Helps download and parse the web pages.
     */
    private HttpHelper helper = new HttpHelper();
    
    /** 
     * Keep count of recipes delivered.
     */
    private static int recipecounter = 0;

    /**
     * List of recipes.
     */
    public List<Recipe> recipelist = new ArrayList<Recipe>();

	
    /**
     * Creates a new spider that will crawl at most maxUrls.
     * @param maxUrls Maximum number of URLs to crawl.
     */
    public Spider(int maxUrls) {
	this.maxUrls = maxUrls;
    }
    
    /**
     * Crawls at most maxUrls starting with beginningUrl.
     * @param beginningUrl Starting URL.
    */
    public void crawl(String beginningUrl) {
	urlCounter.countWord(beginningUrl);
	work.add(beginningUrl); 
	while (work.peek() != null) {
	    if (recipecounter != maxUrls) {
		processPage(work.peek());
		finished.add(work.poll());
	    }
	    else {
		break;
	    }
	}
    }
    
    
    /**
     * Retrieves content from a url and processes that content. 
     * @param url A URL to process.
     */
    
    public void processPage(String url) {
	/* Retrieve HTML content of webpage and put in string */
	String html = helper.retrieve(url);
	
	/* If recipe has x or more reviews, we want it */
	String uniprop = recipefinder(url);
	int unipropint = Integer.parseInt(uniprop);
	String specialstring = "<meta itemprop=\"reviewCount\" content=\"";
	int reviewcount = 0;
	if (html.indexOf(specialstring) != -1) {
	    int specialstart = html.indexOf(specialstring);
	    specialstart += specialstring.length();
	    int specialend = html.indexOf("\"",specialstart);
	    reviewcount = Integer.parseInt(html.substring(specialstart,specialend));
	}
	if (reviewcount > 0) {
	    Recipe recipe = recipewriter(url, html);
	    recipelist.add(recipe);
	}
	unipropint += 1;
	String unipropnew = Integer.toString(unipropint);
	work.add("http://allrecipes.com/recipe/" + unipropnew + "/");
    }


    
    /** 
     * Creates a new Recipe object and adds info to it.
     * @param url A URL that contains recipe information.
     * @param html The html content of the given URL.
     */
    public Recipe recipewriter (String url, String html) {
	/* Defining variables that will be useful in the search for recipe info. */
	Recipe yummy = new Recipe();
	String keyphrase = null;
	String keyphrase2 = null;
	String keyphrase3 = null;
	int beginhere = 0;
	int start = 0;
	int end = 0;
	List<String> units = new ArrayList<String>();
	units.add("teaspoon");
	units.add("teaspoons");
	units.add("tablespoon");
	units.add("tablespoons");
	units.add("fluid");
	units.add("ounce");
	units.add("ounces");
	units.add("gill");
	units.add("gills");
	units.add("cup");
	units.add("cups");
	units.add("pint");
	units.add("pints");
	units.add("quart");
	units.add("quarts");
	units.add("clove");
	units.add("cloves");
	units.add("gallon");
	units.add("gallons");
	units.add("milliliter");
	units.add("milliliters");
	units.add("millilitre");
	units.add("millilitres");
	units.add("liter");
	units.add("liters");
	units.add("litre");
	units.add("litres");
	units.add("pound");
	units.add("pounds");
	units.add("milligram");
	units.add("milligrams");
	units.add("milligramme");
	units.add("milligrammes");
	units.add("gram");
	units.add("grams");
	units.add("gramme");
	units.add("grammes");
	units.add("millimeter");
	units.add("millimeters");
	units.add("millimetre");
	units.add("millimetres");
	units.add("centimeter");
	units.add("centimeters");
	units.add("centimetre");
	units.add("centimetres");
	units.add("cans");
	units.add("can");
	units.add("inch");
	units.add("inches");
	units.add("package");
	units.add("packages");
		
	/* Record URL of recipe. */
	yummy.url = url;

	/* Adding to ingredient list */
	String amount;
	String ingred;
	keyphrase = "itemprop=\"ingredients\">";
	keyphrase2 = "</span>";
	beginhere = 0;
	while (html.indexOf(keyphrase, beginhere) != -1) {
	    start = html.indexOf(keyphrase, beginhere);
	    start = start + keyphrase.length();
	    end = html.indexOf(keyphrase2, start);
	    String ai = html.substring(start,end);
	    String[] los = ai.split(" ");
	    int loscount = 0;
	    for (int i = 0; i < los.length; i++) {
		if (units.contains(los[i]))
		    loscount = i;
	    }
	    if (loscount == 0) {
		amount = ai.replaceAll("[^0-9]+"," ");
		ingred = ai.substring(ai.indexOf(amount.charAt(amount.length()-1)) + 1, ai.length());
	    }
	    else {
		amount = ai.substring(0, ai.indexOf(los[loscount]) + los[loscount].length());
		ingred = ai.substring(ai.indexOf(los[loscount]) + los[loscount].length(), ai.length());
	    }
	    amount = amount.trim();
	    ingred = ingred.trim();
	    yummy.ingredients.amounts.add(amount);
	    yummy.ingredients.ingredientnames.add(ingred);
	    beginhere = end;
	}
	
	/* Finding rating */
	keyphrase = "<meta property=\"og:rating\" content=\"";
	keyphrase2 = "\"";
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	String ratingstring = html.substring(start,end);

	keyphrase = ".";
	start = ratingstring.indexOf(keyphrase);
	start = start - 1;
	end = start + 4;
	yummy.rating = Double.parseDouble(ratingstring.substring(start,end));
	
	/* Adding to steps */
	keyphrase = "<span class=\"recipe-directions__list--item\">";
	keyphrase2 = "</span>";
	beginhere = 0;
	while (html.indexOf(keyphrase, beginhere) != -1) {
	    start = html.indexOf(keyphrase, beginhere);
	    start = start + keyphrase.length();
	    end = html.indexOf(keyphrase2, start);
	    String thestep = html.substring(start,end);
	    thestep = thestep.replace("&#39;", "'");
	    yummy.steps = yummy.steps.concat(thestep);
	    beginhere = end;
	}
	
	/* Finding recipe name */
	keyphrase = "<meta property=\"og:title\" content=\"";
	keyphrase2 = "\"";
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	yummy.rname = html.substring(start,end).replace("&#39;","'");
	
	/* Finding a picture */
	keyphrase = "rec-photo";
	keyphrase2 = "src=\"";
	keyphrase3 = "\"";
	start = html.indexOf(keyphrase2, html.indexOf(keyphrase));
	start = start + keyphrase2.length();
	end = html.indexOf(keyphrase3, start);
	yummy.imageurl = html.substring(start, end);

	/* Finding cooking time */
	keyphrase = "<span class=\"ready-in-time\">";
	keyphrase2 = "</span>";
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	yummy.cooktime = html.substring(start,end);

	/* Finding serving size */
	keyphrase = "itemprop=\"recipeYield\" content=\"";
	keyphrase2 = "\"";
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	yummy.serving = html.substring(start,end);

	/* Printing out info - used for testing and bug searching */
	// System.out.println("rname: " + "\t" + yummy.rname);
	// System.out.println("url: " + "\t" + yummy.url);
	// System.out.println("imageurl: " + "\t" + yummy.imageurl);
	// for (int i = 0; i < yummy.ingredients.ingredientnames.size(); i++)
	//     System.out.println(yummy.ingredients.amounts.get(i) + "\t" + "\t" + yummy.ingredients.ingredientnames.get(i));
	// System.out.println("rating: " + "\t" + yummy.rating);
	// System.out.println(yummy.steps);
	// System.out.println(yummy.cooktime);
	// System.out.println(yummy.serving);
	// System.out.println();
	// System.out.println();
	
	/* Wrapping it up */
	recipecounter++;
	return yummy;
    }

    public String recipefinder(String myurl) {
	String findme = "/recipe/";
	String recipeno = null;
	int helpstart = myurl.indexOf(findme);
	helpstart = findme.length() + helpstart;
	int endstart = myurl.indexOf("/",helpstart);
	recipeno = myurl.substring(helpstart,endstart);
	return recipeno;
    }

    /**
	 * Returns the number of times the spider encountered
	 * links to each url.  The url are returned in increasing
	 * frequency order.
	 * 
	 * @return Number of URLs encountered.
	 */
    public WordCount[] getUrlCounts() {
	return urlCounter.getCounts();
    }
    
    /** Getter only to be used for testing.
	@return The state variable <code>work</code>
    */
    Queue<String> getWork() { return work; }
    /** Getter only to be used for testing.
	@return The state variable <code>finished</code>
    */
    List<String> getFinished() { return finished; }
}
