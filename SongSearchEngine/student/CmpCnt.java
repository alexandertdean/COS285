package student;

import java.util.Comparator;

/**
 * CmpCnt.java
 * Bob Boothe 2016
 * 
 * A class for adding comparison counting to a comparator in a clean fashion.
 * 	
 * This is used as a super class when declaring you comparator class, e.g.:
 *    public static class CmpArtist extends CmpCnt implements Comparator<Song> {
 * 
 * This class has a protected variable: cmpCnt
 * It is initialized to 0 when your comparator is constructed.
 * In your comparator's compare() method you add "cmpCnt++;" which increments
 * the counter each time it is called.
 * 
 * Later you can retrieve the accumulated value of the counter with something like:
 *    ((CmpCnt)cmp).getCmpCnt()
 * where cmp would be the instance name of your comparator. The casting is needed to 
 * tell the compiler that you want to use the CmpCnt methods of your Comparator object.
 * 
 * There is also a resetCmpCnt() method in case you want to count another operation
 * using the same comparator. 
 */
public class CmpCnt {
	protected int cmpCnt;

	// constructor initializes the counter to zero
	CmpCnt(){
		cmpCnt=0;
	}

	// return the value of the embedded counter
	public int getCmpCnt() {
		return cmpCnt;
	}

	// reset the counter to 0
	public void resetCmpCnt() {
		cmpCnt = 0;
	}
}
