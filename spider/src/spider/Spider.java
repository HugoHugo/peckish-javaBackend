package spider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.lang.*;
import java.io.*;

/**
 * Downloads web page content starting with a starting url.
 * If the spider encounters links in the content, it downloads
 * those as well.
 * 
 * Steps:
 * 1. Complete the processPage method.  One TestSpider unit tests should pass.
 * 2. Complete the crawl() method.  Both TestSpider unit tests should pass.
 *  
 * @author shilad
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
     * Keep count of recipes made.
     */
    private static int recipecounter = 0;

    /**
     * Base recipe webpage of beginning URL.
     */
    public String base = null;

    /**
     * Beginning url.
     */
    private String beginURL = null;

    /**
     * List of recipes.
     */
    private List<Recipe> recipelist = new ArrayList<Recipe>();

	
	/**
	 * Creates a new spider that will crawl at most maxUrls.
	 * @param maxUrls Maximum number of URLs to crawl.
	 */
	public Spider(int maxUrls) {
		this.maxUrls = maxUrls;
	}
	
	/**
	 * Crawls at most maxUrls starting with beginningUrl.
	 * @param beginningUrl Starting URL, indicating a web page that 
	   potentially contains other URLs.
	 */
	public void crawl(String beginningUrl) {
	    beginURL = beginningUrl;
	    urlCounter.countWord(beginningUrl);
	    work.add(beginningUrl);
	    base = beginningUrl.substring(0, beginningUrl.indexOf(".com")); 
	    while (work.peek() != null) {
		// instead of checking finished.size()...
		if (recipecounter != maxUrls) {
		    processPage(work.peek());
		    finished.add(work.poll());
		}
		else
		    break;
	    }
	}
		
		// TODO: While there is remaining work and we haven't
		// reach the maximum # of finished urls, process
		// the next unfinshed url.  After processing, mark
		// it as finished.
	
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


    /** Create a new Recipe object and add info to it */
    public Recipe recipewriter (String url, String html) {
	Recipe yummy = new Recipe();
	String baseurl = null;
	String keyphrase = null;
	String keyphrase2 = null;
	int beginhere = 0;
	int start = 0;
	int end = 0;

	/* Find base URL for recipe page. */
	keyphrase = "<link id=\"canonicalUrl\" rel=\"canonical\" href=\"";
	keyphrase2 = "\"";    
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	baseurl = html.substring(start,end);
	
	/* Adding to ingredient list */
	keyphrase = "itemprop=\"ingredients\">";
	keyphrase2 = "</span>";
	beginhere = 0;
	while (html.indexOf(keyphrase, beginhere) != -1) {
	    Ingredient ing = new Ingredient();
	    start = html.indexOf(keyphrase, beginhere);
	    start = start + keyphrase.length();
	    end = html.indexOf(keyphrase2, start);
	    ing.iname = html.substring(start,end);
	    yummy.inglist.add(ing);
	    beginhere = end;
	}
	
	/* Finding rating */
	keyphrase = "<meta property=\"og:rating\" content=\"";
	keyphrase2 = "\"";
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	yummy.rating = Double.parseDouble(html.substring(start,end));
	
	/* Adding to step list */
	// keyphrase = "<span class=\"recipe-directions__list--item\">";
	// keyphrase2 = "</span>";
	// beginhere = 0;
	// while (html.indexOf(keyphrase, beginhere) != -1) {
	//     start = html.indexOf(keyphrase);
	//     start = start + keyphrase.length();
	//     end = html.indexOf(keyphrase2, start);
	//     String thestep = html.substring(start,end);
	//     yummy.steps.add(thestep);
	//     beginhere = end;
	// }
	
	/* Finding recipe name */
	keyphrase = "<meta property=\"og:title\" content=\"";
	keyphrase2 = "\"";
	start = html.indexOf(keyphrase);
	start = start + keyphrase.length();
	end = html.indexOf(keyphrase2, start);
	yummy.rname = html.substring(start,end);
	
	/* Finding a picture */
	List<String> weblinks = helper.extractLinks(url,html);
	String potentialURL = null;
	for (int i = 0; i < weblinks.size(); i++) {
	    potentialURL = weblinks.get(i);
	    if (helper.isImage(potentialURL)) {
		keyphrase = baseurl + "photos/";
		if (potentialURL.indexOf(keyphrase) != -1)
		    yummy.imageurl = potentialURL;
	    }
	} 
	recipecounter++;
	System.out.println("Added recipe: " + yummy.rname);
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
}

