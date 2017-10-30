package student;

import java.util.Comparator;

public class RankedItem extends Object {
	public String name;
	public int count;
	
	public RankedItem(String myName, int myCount) {
		name = myName;
		count = myCount;
	}
	
	public String toString() {
		return name + "(" + count + ")";
	}
	
	
}
