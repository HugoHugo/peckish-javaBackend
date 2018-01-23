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
	public recipeReturn[] searchPotentialRecipes(int[] ingredientlist){
		recipeReturn[] myResults = null;
		try{
			//We make a command to look for the number of ingredients missing for each recipe, and select those that have less than 4 missing
			Statement st = con.createStatement();
			String myCommand = "FROM (SELECT r.R_id, MAX(r.numIngredients) - COUNT(*) AS missing FROM IinR ir, recipes r WHERE r.R_id=ir.R_id AND ir.I_id in (";
			myCommand = myCommand + ingredientlist[0];
			for(int i=1;i<ingredientlist.length;i++){
				myCommand = myCommand + "," + ingredientlist[i];
			}
			myCommand = myCommand + ") GROUP BY r.R_id ORDER BY missing) sub WHERE sub.missing <= 4;";
			//We first ask for the number of recipes in question
			ResultSet rs = st.executeQuery("SELECT COUNT(*)" + myCommand);
			rs.next();
			int resLength = rs.getInt("count");
			//Then we ask for the recpes themselves
			rs = st.executeQuery("SELECT sub.R_id, sub.missing "+myCommand);
			//We make a list of the recipes to be returned
			myResults = new recipeReturn[resLength];
			int j=0;
			while(rs.next()){
				myResults[j] = new recipeReturn(rs.getInt("R_id"),rs.getInt("missing"));
				j=j+1;
			}
		} catch (SQLException e){
			System.out.println("NO }:(");
		}
		return myResults;
	}

	/** A helper function that finds the ID of an ingredient
	@param ingred the exact name of the ingredient in question
	@return the id of the ingredient in question. Returns -1 if no ingredients are found*/
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

	class recipeReturn{
		int Rid;
		int missing;
		public recipeReturn(int setRid, int setmissing){
			Rid = setRid;
			missing = setmissing;
		}
	}
}