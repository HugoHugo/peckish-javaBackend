package spider;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Ingredient {
    public static int iidtracker;

    public int iid;

    public String iname;

    public Ingredient() {
	this.iid = this.iidtracker;
	this.iidtracker++;
    }
}
