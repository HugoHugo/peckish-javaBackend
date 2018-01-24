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
	private Connection con;

	public static void main(String[] args){
		ResourceBundle bundle = ResourceBundle.getBundle("javaconfig");
		Librarian mylib = new Librarian(bundle);
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
			System.out.println(r.rname +" is missing " + r.NoIngMiss + " ingredients.");
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
			System.out.println("Connected with no exceptions");
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
			String myCommand = "SELECT sub.thing AS missing, sub.R_id, r.name, r.numIngredients FROM (SELECT r.R_id, MAX(r.numIngredients) - COUNT(*) AS thing FROM IinR ir, recipes r WHERE r.R_id=ir.R_id AND ir.I_id in (";
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
				myResults.add(new Recipe(rs.getInt("R_id"), rs.getString("name"), getIngredientsinRecipe(rs.getInt("R_id")),rs.getInt("missing")));
			}
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResults;
	}

	/** A helper function that finds the ID of an ingredient
	@param ingred the exact name of the ingredient in question
	@return the appropriate Ingredient object*/
	public int getIngredientID(String ingred){
		int myResult=-1;
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
	@return the appropriate Ingredient object*/
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
				//(rs.getInt("I_id"), rs.getString("name"), rs.getString("amount"));
			}
			return listIng;
		} catch (SQLException e){
			System.out.println(e.getMessage());
			return null;
		}
	}
}