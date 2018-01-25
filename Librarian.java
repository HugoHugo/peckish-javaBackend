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
	/** list of IDs already in use */
	static private List<Integer> usedIIDs = new ArrayList<Integer>();
	static private List<Integer> usedRIDs = new ArrayList<Integer>();

	/** In case of parallelization*/
	static private boolean stashing = false;
	static private boolean enableStashing = true;

	public static void main(String[] args){
		ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
		Librarian mylib = new Librarian(bundle);
		try{
			Statement st = mylib.con.createStatement();
			st.executeUpdate("set search_path to belezn1;");
		}catch(SQLException e){;}
		mylib.UpdateUsedIDs();
		int testIngint = mylib.getIngredientID("Cheese");
		String testIngString = mylib.getIngredientName(3);
		System.out.println("Cheese has ID: " +testIngint);
		System.out.println("The ingredient with ID 3 is " +testIngString);
		List<Integer> ingList = new ArrayList<Integer>();
		ingList.add(1);ingList.add(2);ingList.add(5);
		Ingredients testIng = new Ingredients();
		testIng.ingredientIDs = ingList;
		mylib.fillIname(testIng);
		List<Recipe> resReceps = mylib.searchPotentialRecipes(testIng);
		System.out.println("Recipes found: " + resReceps.size());
		for(Recipe r : resReceps){
			System.out.println(r.rname +" is missing " + r.missing + " ingredients.");
		}
		System.out.println("\n About to test adding stuff to the database");
		testIng = new Ingredients();
		testIng.ingredientnames.add("salt");
		testIng.amounts.add("1/2 tsp");
		testIng.ingredientnames.add("elbow macaroni");
		testIng.amounts.add("2 cups");
		testIng.ingredientnames.add("butter");
		testIng.amounts.add("2 Tbsp");
		testIng.ingredientnames.add("dijon mustard");
		testIng.amounts.add("1/2 tsps");
		testIng.ingredientnames.add("cayenne pepper");
		testIng.amounts.add("1 pinch");
		testIng.ingredientnames.add("sharp cheddar");
		testIng.amounts.add("1.5 cups shredded");
		Recipe myR = new Recipe();
		myR.rname = "Macaroni and Cheese";
		myR.ingredients = testIng;
		myR.url = "BudgetBytes";
		enableStashing = true;
		mylib.stashRecipe(myR);
		ingList = new ArrayList<Integer>();
		ingList.add(30);ingList.add(31);ingList.add(32);
		testIng = new Ingredients();
		testIng.ingredientIDs = ingList;
		mylib.fillIname(testIng);
		resReceps = mylib.searchPotentialRecipes(testIng);
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
			String myCommand = "SELECT sub.thing AS missing, sub.R_id, r.rname, r.numIngredients, r.url, r.imageURL, r.rating, r.steps, r.cooktime, r.serving FROM (SELECT r.R_id, MAX(r.numIngredients) - COUNT(*) AS thing FROM IinR ir, recipes r WHERE r.R_id=ir.R_id AND ir.I_id in (";
			myCommand = myCommand + ingredientlist.ingredientIDs.get(0);
			for(int i=1;i<ingredientlist.ingredientIDs.size();i++){
				myCommand = myCommand + "," + ingredientlist.ingredientIDs.get(i);
			}
			myCommand = myCommand + ") GROUP BY r.R_id) AS sub, recipes r WHERE sub.thing <= 4 AND r.R_id=sub.R_id ORDER BY missing;";
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
				myResults.add(tempr);
			}
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResults;
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
			ps1.setString(1, ingred);
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
			mylist.ingredientIDs.add(getIngredientID(name));
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
			String myCmd = "SELECT i.I_id, i.name, ir.amount FROM ingredients i, IinR ir WHERE ir.R_id = ? AND i.I_id = ir.I_id;";
			Ingredients listIng = new Ingredients();
			PreparedStatement ps = con.prepareStatement(myCmd);
			ps.setInt(1, Rid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				listIng.ingredientnames.add(rs.getString("name"));
				listIng.ingredientIDs.add(rs.getInt("I_id"));
				listIng.amounts.add(rs.getString("amount"));
				//(rs.getInt("I_id"), rs.getString("name"), rs.getString("amount"));
			}
			return listIng;
		} catch (SQLException e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	public void UpdateUsedIDs(){
		try{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("SELECT I_id FROM ingredients;");
			while(rs.next()) usedIIDs.add(rs.getInt("I_id"));
		} catch (SQLException e){
			System.out.println("UpdateUsedIDs " + e.getMessage());
		}
	}

	static private int getUnusedIID(){
		int i=0;
		while(usedIIDs.contains(i)) i=i+1;
		return i;
	}

	static private int getUnusedRID(){
		int i=0;
		while(usedRIDs.contains(i)) i=i+1;
		return i;
	}

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

	public boolean stashIngredients(Ingredients ings){
		if(!enableStashing) return false;
		fillIID(ings);
		try{
			PreparedStatement ps = con.prepareStatement("INSERT INTO ingredients (I_id, name) VALUES (?,?)");
			for(int i = 0; i<ings.ingredientIDs.size(); i++){
				if(ings.ingredientIDs.get(i) == -1){
					int temp = getUnusedIID();
					ps.setInt(1, temp);
					ps.setString(2,ings.ingredientnames.get(i));
					ps.executeUpdate();
					usedIIDs.add(temp);
				}
			}
			fillIID(ings);
		} catch (SQLException e){
			System.out.println("stashIngredients " + e.getMessage());
			return false;
		}
		return true;
	}
}