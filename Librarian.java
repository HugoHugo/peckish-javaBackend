import java.io.*;
import java.util.*;
import java.sql.*;

/** Class to communicate with the Database. */

public class Librarian{
	/** username to be used in the database */
	private static String user;
	/** the password to go with the username */
	private static String password;
	/** the url where the database can be found */
	private static String url;
	/** The driver used to connect to the database */
	private static String driver;
	/** The variable referencing the open connection to the database */
	public Connection con;
	/** list of IIDs already in use */
	static private List<Integer> usedIIDs = new ArrayList<Integer>();
	/** list of RIDs already in use */
	static private List<Integer> usedRIDs = new ArrayList<Integer>();
	/** A String containing the beginning of a list of the I_ids of the default ingredients
	It contains water, salt, pepper by default */
	static private String defaultIngredients = "(0,1,2";

	/** In case of parallelization*/
	static private boolean stashing = false;
	/** In case of parallelization*/
	static private boolean enableStashing = true;

	public static void main(String[] args){
		ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
		Librarian mylib = new Librarian(bundle);
		//mylib.UpdateUsedIDs();
		//mylib.updateDefaultIngredients();
		List<String> ingList2 = new ArrayList<String>();
		ingList2.add("macaroni");ingList2.add("eggs");ingList2.add("milk");
		Ingredients testIng = new Ingredients();
		testIng.ingredientnames = ingList2;
		List<Recipe> resReceps = mylib.searchPotentialRecipes(testIng);
		System.out.println("Recipes found: " + resReceps.size());
		for(Recipe r : resReceps){
			System.out.println(r.rname +" is missing " + r.missing + " ingredients.");
		}
	}

	/** Constructor that stores connection info and initializes the connection to the database */
	public Librarian(ResourceBundle bundle){
		user = bundle.getString("jdbc.user");
		password = bundle.getString("jdbc.password");
		url = bundle.getString("jdbc.url") + bundle.getString("jdbc.dbname");
		driver = bundle.getString("jdbc.driver");
		try{
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();
			st.executeUpdate("set search_path to mca_i18_pantry;");
			System.out.println("Connected with no exceptions");
			UpdateUsedIDs();
			updateDefaultIngredients();
		} catch (ClassNotFoundException e){
			System.out.println("Error Connecting to Database:");
			System.out.println(e.getMessage());
		} catch (SQLException e){
			System.out.println("Error Connecting to Database:");
			System.out.println(e.getMessage());
		}
	}

	/** method that searches for recipes that can be made with the given ingredients
	@param ingredientlist is an array of ints that represents the IDs of the ingredients
	@return A list of recipes that have at most 4 ingredients missing, with the number of ingredients missing*/
	public List<Recipe> searchPotentialRecipes(Ingredients ingredientlist){
		List<Recipe> myResults = null;
		fillIID(ingredientlist);
		try{
			System.out.println("Starting search for ingredients");
			//We make a command to look for the number of ingredients missing for each recipe, and select those that have less than 4 missing
			Statement st = con.createStatement();
			String myCommand = "SELECT sub.thing AS missing, sub.R_id, r.rname, r.numIngredients, r.url, r.imageURL, r.rating, r.steps, r.cooktime, r.serving, r.source FROM (SELECT r.R_id, MAX(r.numIngredients) - COUNT(*) AS thing FROM IinR ir, recipes r WHERE r.R_id=ir.R_id AND ir.I_id in "+defaultIngredients;
			for(int i=0;i<ingredientlist.ingredientIDs.size();i++){
				myCommand = myCommand + "," + ingredientlist.ingredientIDs.get(i);
			}
			myCommand = myCommand + ") GROUP BY r.R_id) AS sub, recipes r WHERE sub.thing <= 15 AND r.R_id=sub.R_id ORDER BY missing;";
			//We ask for the recipes
			ResultSet rs = st.executeQuery(myCommand);
			//We make a list of the recipes to be returned
			myResults = new ArrayList<Recipe>();
			while(rs.next()){
				Recipe tempr = new Recipe();
				tempr.rid = rs.getInt("R_id");
				tempr.ingredients =getIngredientsinRecipe(rs.getInt("R_id"));
				tempr.rating = rs.getDouble("rating");
				tempr.steps = rs.getString("steps");
				tempr.rname = rs.getString("rname");
				tempr.imageurl = rs.getString("imageURL");
				tempr.url = rs.getString("url");
				tempr.cooktime = rs.getString("cooktime");
				tempr.serving = rs.getString("serving");
				tempr.missing = rs.getInt("missing");
				tempr.source = rs.getString("source");
				myResults.add(tempr);
			}
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResults;
	}

	/** Searches the database for the recipe corresponding to the ID, and returns it
	@param Rid the ID of the recipe in question
	@return the appropriate Recipe object*/
	public Recipe getRecipe(int Rid){
		Recipe tempr = new Recipe();
		try{
			String myCmd = "SELECT * FROM recipes WHERE R_id = ?;";
			PreparedStatement ps1 = con.prepareStatement(myCmd);
			ps1.setInt(1, Rid);
			ResultSet rs = ps1.executeQuery();
			rs.next();
			tempr.rid = rs.getInt("R_id");
			tempr.ingredients =getIngredientsinRecipe(rs.getInt("R_id"));
			tempr.rating = rs.getDouble("rating");
			tempr.steps = rs.getString("steps");
			tempr.rname = rs.getString("rname");
			tempr.imageurl = rs.getString("imageURL");
			tempr.url = rs.getString("url");
			tempr.cooktime = rs.getString("cooktime");
			tempr.serving = rs.getString("serving");
			tempr.source = rs.getString("source");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return tempr;
	}

	/** A helper function that finds the ID of a recipe
	@param title the exact name of the recipe in question
	@return Rid*/
	public int getRecipeID(String title){
		int myResult=-1;
		try{
			String myCmd = "SELECT R_id, rname FROM recipes WHERE rname = ?;";
			PreparedStatement ps1 = con.prepareStatement(myCmd);
			ps1.setString(1, title);
			ResultSet rs = ps1.executeQuery();
			rs.next();
			myResult = rs.getInt("R_id");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResult;
	}

	/** A helper function that finds the ID of an ingredient
	@param ingred the exact name of the ingredient in question
	@return the appropriate Ingredient ID*/
	public int getIngredientID(String ingred){
		int myResult = -1;
		try{
			String myCmd = "SELECT I_id, name FROM ingredients WHERE name = ?;";
			PreparedStatement ps1 = con.prepareStatement(myCmd);
			ps1.setString(1, ingred.toLowerCase());
			ResultSet rs = ps1.executeQuery();
			rs.next();
			myResult = rs.getInt("I_id");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResult;
	}

	/** A helper function that finds the ID of an ingredient
	@param Iid the ID of the ingredient in question
	@return the appropriate Ingredient name*/
	public String getIngredientName(int Iid){
		String myResult=null;
		try{
			String myCmd = "SELECT I_id, name FROM ingredients WHERE I_id = ?;";
			PreparedStatement ps1 = con.prepareStatement(myCmd);
			ps1.setInt(1, Iid);
			ResultSet rs = ps1.executeQuery();
			rs.next();
			myResult = rs.getString("name");
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResult;
	}

	/** Helper function to match IDs to ingredient names  */
	public void fillIID(Ingredients mylist){
		mylist.ingredientIDs = null;
		mylist.ingredientIDs = new ArrayList<Integer>();
		for(String name : mylist.ingredientnames){
			int temp = getIngredientID(name.toLowerCase());
			System.out.println("Get ID for \t" + name + "\t" + temp);
			mylist.ingredientIDs.add(temp);
		}
	}

	/** Helper function to match ingredient names to IDs  */
	public void fillIname(Ingredients mylist){
		mylist.ingredientnames = null;
		mylist.ingredientnames = new ArrayList<String>();
		for(Integer ID : mylist.ingredientIDs){
			mylist.ingredientnames.add(getIngredientName(ID));
		}
	}

	/** This function returns an ingredients object containing the ingredients of a recipe identified by Rid
	@param Rid is the ID of the recipe in question
	@return Ingredients is the ingredients object of that recipe*/
	public Ingredients getIngredientsinRecipe(int Rid){
		try{
			String myCmd = "SELECT i.I_id, i.name, ir.amount, i.type FROM ingredients i, IinR ir WHERE ir.R_id = ? AND i.I_id = ir.I_id;";
			Ingredients listIng = new Ingredients();
			PreparedStatement ps = con.prepareStatement(myCmd);
			ps.setInt(1, Rid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				listIng.ingredientnames.add(rs.getString("name"));
				listIng.ingredientIDs.add(rs.getInt("I_id"));
				listIng.amounts.add(rs.getString("amount"));
				listIng.types.add(rs.getString("type"));
				//(rs.getInt("I_id"), rs.getString("name"), rs.getString("amount"));
			}
			return listIng;
		} catch (SQLException e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	/** This function returns an ingredients object containing all ingredients currently in the database
	@return Ingredients is the ingredients object of that recipe*/
	public Ingredients getAllIngredients(){
		try{
			String myCmd = "SELECT I_id, name, type FROM ingredients;";
			Ingredients listIng = new Ingredients();
			PreparedStatement ps = con.prepareStatement(myCmd);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				listIng.ingredientnames.add(rs.getString("name"));
				listIng.ingredientIDs.add(rs.getInt("I_id"));
				listIng.amounts.add("n/a");
				listIng.types.add(rs.getString("type"));
				//(rs.getInt("I_id"), rs.getString("name"), rs.getString("amount"));
			}
			return listIng;
		} catch (SQLException e){
			System.out.println(e.getMessage());
			return null;
		}
	}
	/** Checks the database and updates the list of IDs already in the Database */
	public void UpdateUsedIDs(){
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT I_id FROM ingredients;");
			while(rs.next()) usedIIDs.add(rs.getInt("I_id"));
		} catch (SQLException e){
			System.out.println("UpdateUsedIDs " + e.getMessage());
		}
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT R_id FROM recipes;");
			while(rs.next()) usedRIDs.add(rs.getInt("R_id"));
		} catch (SQLException e){
			System.out.println("UpdateUsedIDs " + e.getMessage());
		}
	}

	/** Helper function that returns the lowest unused I_id
	@return An unused I_id*/
	static private int getUnusedIID(){
		int i=0;
		while(usedIIDs.contains(i)) i=i+1;
		return i;
	}

	/** Helper function that returns the lowest unused R_id
	@return An unused R_id*/
	static private int getUnusedRID(){
		int i=0;
		while(usedRIDs.contains(i)) i=i+1;
		return i;
	}

	/**Function that stores a recipe in the database if it isn't there already
	@param r is the recipe object to be stored in the database
	@return a boolean representing whether or not the operation succeeded*/
	public boolean stashRecipe(Recipe r){
		if(!enableStashing) return false;
		stashIngredients(r.ingredients);
		try{
			PreparedStatement ps = con.prepareStatement("INSERT INTO recipes (R_id, rname, url, imageURL, numIngredients, steps, rating, cooktime, serving) VALUES (?,?,?,?,?,?,?,?,?)");
			if(getRecipeID(r.rname)==-1){
				int temp = getUnusedRID();
				System.out.println(temp);
				ps.setInt(1, temp);
				ps.setString(2,r.rname);
				ps.setString(3,r.url);
				ps.setString(4,r.imageurl);
				ps.setInt(5, r.ingredients.ingredientIDs.size());
				ps.setString(6, r.steps);
				ps.setDouble(7,r.rating);
				ps.setString(8,r.cooktime);
				ps.setString(9,r.serving);
				ps.executeUpdate();
				usedRIDs.add(temp);
				ps = con.prepareStatement("INSERT INTO IinR (R_id, I_id, amount) VALUES (?,?,?)");
				int length = r.ingredients.ingredientIDs.size();
				for(int i =0; i < length; i++){
					System.out.println(temp + " " + i);
					ps.setInt(1, temp);
					ps.setInt(2, r.ingredients.ingredientIDs.get(i));
					ps.setString(3, r.ingredients.amounts.get(i));
					ps.executeUpdate();
				}
			}
		} catch (SQLException e){
			System.out.println("stashRecipe " + e.getMessage());
			return false;
		}
		return true;
	}

	/**Function that goes through all ingredient names in an Ingredients
	object and stores them in the database if they aren't there already.
	In the process, it refills the I_ids of each ingredient name to the
	values that match the database
	@param ings is the Ingredients object to be stored in the database
	@return a boolean representing whether or not the operation succeeded*/
	public boolean stashIngredients(Ingredients ings){
		if(!enableStashing) return false;
		System.out.println("boop");
		fillIID(ings);
		try{
			PreparedStatement ps = con.prepareStatement("INSERT INTO ingredients (I_id, name) VALUES (?,?)");
			for(int i = 0; i<ings.ingredientIDs.size(); i++){
				if(ings.ingredientIDs.get(i) == -1){
					int temp = getUnusedIID();
					System.out.println("Adding Ingredient:" + ings.ingredientnames.get(i));
					ps.setInt(1, temp);
					ps.setString(2,ings.ingredientnames.get(i).toLowerCase());
					ps.executeUpdate();
					usedIIDs.add(temp);
				}
			}
		} catch (SQLException e){
			System.out.println("stashIngredients " + e.getMessage());
			return false;
		}
		fillIID(ings);
		return true;
	}

	/** Helper function that makes sure all the
	'default ingredients' (like 'add pepper to taste') are kept track of*/
	public void updateDefaultIngredients(){
		try{
			String myCmd = "SELECT I_id FROM ingredients WHERE name ~* '.*(salt ).*' OR name ~* '.*(black pepper).*' OR (name ~* '.*(pepper).*' AND name ~* '.*(to taste).*');";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(myCmd);
			defaultIngredients = "(0,1,2";
			while(rs.next()){
				defaultIngredients = defaultIngredients + "," + rs.getInt("I_id");
			}
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		System.out.println(defaultIngredients);
	}
}