/** A barebones class to store store the properties of recipes */
public class Recipe{
	/** ID of recipe */
	int R_id;
	/** name of recipe */
	String name;
	/** list of ingredients */
	Ingredient[] myIngredients;
	/** number of ingredients missing */
	int missing=0;
	/** number of ingredients in the recipe */
	int numingredients=0;

	/** Basic constructor
	@param initID is the value R_id will be set to
	@param initName is the value name will be set to
	@param initIngredients is the value myIngredients will be set to */
	public Recipe(int initID, String initName, Ingredient[] initIngredients){
		R_id = initID;
		name = initName;
		myIngredients = initIngredients;
	}
	
	public Recipe(int initID, String initName, Ingredient[] initIngredients, int initmissing, int initnuming){
		R_id = initID;
		name = initName;
		myIngredients = initIngredients;
		missing = initmissing;
		numingredients = initnuming;
	}
}