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
		Ingredient testIng = mylib.getIngredient("Cheese");
		System.out.println("Ingredient " + testIng.name + " has ID: " +testIng.I_id);
		int[] ingArray = {1,2,5};
		Recipe[] resReceps = mylib.searchPotentialRecipes(ingArray);
		System.out.println(resReceps.length);
		for(int i=0;i<resReceps.length;i++){
			System.out.println(resReceps[i].name +" is missing " + resReceps[i].missing + " ingredients.");
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
	public Recipe[] searchPotentialRecipes(int[] ingredientlist){
		Recipe[] myResults = null;
		try{
			System.out.println("Starting search for ingredients");
			//We make a command to look for the number of ingredients missing for each recipe, and select those that have less than 4 missing
			Statement st = con.createStatement();
			String myCommand = "FROM (SELECT r.R_id, MAX(r.numIngredients) - COUNT(*) AS thing FROM IinR ir, recipes r WHERE r.R_id=ir.R_id AND ir.I_id in (";
			myCommand = myCommand + ingredientlist[0];
			for(int i=1;i<ingredientlist.length;i++){
				myCommand = myCommand + "," + ingredientlist[i];
			}
			myCommand = myCommand + ") GROUP BY r.R_id) AS sub, recipes r WHERE sub.thing <= 4 AND r.R_id=sub.R_id";
			//We first ask for the number of recipes in question
			ResultSet rs = st.executeQuery("SELECT COUNT(*) " + myCommand + ";");
			rs.next();
			int resLength = rs.getInt("count");
			System.out.println(resLength);
			//Then we ask for the recpes themselves
			rs = st.executeQuery("SELECT sub.thing AS missing, sub.R_id, r.name, r.numIngredients "+myCommand+ " AND r.R_id=sub.R_id ORDER BY missing;");
			//We make a list of the recipes to be returned
			myResults = new Recipe[resLength];
			int j=0;
			while(rs.next()){
				myResults[j] = new Recipe(rs.getInt("R_id"), rs.getString("name"), getIngredientsinRecipe(rs.getInt("R_id")),rs.getInt("missing"),rs.getInt("numIngredients"));
				j=j+1;
			}
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResults;
	}

	/** A helper function that finds the ID of an ingredient
	@param ingred the exact name of the ingredient in question
	@return the appropriate Ingredient object*/
	public Ingredient getIngredient(String ingred){
		Ingredient myResult=null;
		try{
			String myCmd = "SELECT I_id, name FROM ingredients WHERE name = ?;";
			PreparedStatement ps1 = con.prepareStatement(myCmd);
			ps1.setString(1, ingred);
			ResultSet rs = ps1.executeQuery();
			rs.next();
			myResult = new Ingredient(rs.getInt("I_id"), rs.getString("name"));
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResult;
	}

	/** A helper function that finds the ID of an ingredient
	@param Iid the ID of the ingredient in question
	@return the appropriate Ingredient object*/
	public Ingredient getIngredient(int Iid){
		Ingredient myResult=null;
		try{
			String myCmd = "SELECT I_id, name FROM ingredients WHERE I_id = ?;";
			PreparedStatement ps1 = con.prepareStatement(myCmd);
			ps1.setInt(1, Iid);
			ResultSet rs = ps1.executeQuery();
			rs.next();
			myResult = new Ingredient(rs.getInt("I_id"), rs.getString("name"));
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return myResult;
	}

	public Ingredient[] getIngredientsinRecipe(int Rid){
		try{
			String myCmd1 = "SELECT COUNT(*) FROM IinR WHERE R_id = ?";
			String myCmd2 = "SELECT i.I_id, i.name, ir.amount FROM ingredients i, IinR ir WHERE ir.R_id = ? AND i.I_id = ir.I_id;";
			PreparedStatement ps = con.prepareStatement(myCmd1);
			ps.setInt(1, Rid);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int resLength = rs.getInt("count");
			Ingredient[] listIng = new Ingredient[resLength];
			ps = con.prepareStatement(myCmd2);
			ps.setInt(1, Rid);
			rs = ps.executeQuery();
			for(int i=0;i<resLength-1;i++){
				rs.next();
				listIng[i]= new Ingredient(rs.getInt("I_id"), rs.getString("name"), rs.getString("amount"));
			}
			return listIng;
		} catch (SQLException e){
			System.out.println(e.getMessage());
			return null;
		}
	}

	class recipeReturn{
		int Rid;
		int missing;
		public recipeReturn(int setRid, int setmissing){
			Rid = setRid;
			missing = setmissing;
		}
	}
}