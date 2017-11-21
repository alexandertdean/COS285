package student;

import java.util.Comparator;

public class RankedItem extends Object {
	public Object name;
	public int count;
	
	public RankedItem(Object myName, int myCount) {
		name = myName;
		count = myCount;
	}
	
	public String toString() {
		return name.toString() + "(" + count + ")";
	}
	
	
}
