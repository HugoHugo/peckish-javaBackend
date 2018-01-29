import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * Downloads web pages by following http links located
 * in the html of BEGINNING_URL.  Recursively repeats
 * the process.
 * 
 * @author shilad
 *
 */
public class RunSpider {
    // private static final String BEGINNING_URL = "http://allrecipes.com/recipe/23600/worlds-best-lasagna/?internalSource=streams&referringId=1&referringContentType=recipe%20hub&clickId=st_recipes_mades";

    private static String BEGINNING_URL = "http://allrecipes.com/recipe/";

    
  /**
   * Run the spider program.
   * @param args Command-line arguments (unused).
   */

  public static void main(String [] args) {
      Spider spider = new Spider(Integer.parseInt(args[1]));
      BEGINNING_URL = BEGINNING_URL + args[0] + "/";
      spider.crawl(BEGINNING_URL);
      List<Recipe> recipelist = spider.recipelist;
      ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
      Librarian mylib = new Librarian(bundle,0);
      for(Recipe r : recipelist){
	      mylib.stashRecipe(r);
      }
      mylib.updateDefaultIngredients();
  }
}
