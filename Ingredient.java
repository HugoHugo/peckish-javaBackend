/** A barebones class to store store the properties of ingredients */
public class Ingredient{
	/** ID of ingredient */
	int I_id;
	/** name of ingredient */
	String name;
	/** placeholder for amount of ingredient */
	String amount = "a pinch";

	/** Basic constructor
	@param initID is the value I_id will be set to
	@param initName is the value name will be set to */
	public Ingredient(int initID, String initName){
		I_id = initID;
		name = initName;
	}

	/** Basic constructor
	@param initID is the value I_id will be set to
	@param initName is the value name will be set to
	@param initamount is the amount of that ingredient required in tha recipe*/
	public Ingredient(int initID, String initName, String initamount){
		I_id = initID;
		name = initName;
		amount = initamount;
	}
}