package spider;


/**
 * Downloads web pages by following http links located
 * in the html of BEGINNING_URL.  Recursively repeats
 * the process.
 * 
 * @author shilad
 *
 */
public class RunSpider {
  private static final String BEGINNING_URL = "http://allrecipes.com/recipe/23600/worlds-best-lasagna/?internalSource=streams&referringId=1&referringContentType=recipe%20hub&clickId=st_recipes_mades";
  
  /**
   * Run the spider program.
   * @param args Command-line arguments (unused).
   */
  public static void main(String [] args) {
    Spider spider = new Spider(5);
    spider.crawl(BEGINNING_URL);
    //for (WordCount urlCount : spider.getUrlCounts()) {
    //System.out.println("url " + urlCount.getWord() + " is " + 
    //urlCount.getCount());
    //}
  }
}
