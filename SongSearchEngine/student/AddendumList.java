package student;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/*
 * AddendumList.java
 * PUT YOUR NAME HERE
 * 
 * Initial starting code by Prof. Boothe Aug. 2017
 *
 * To an external user an AddendumList appears as a single sorted list ordered by the comparator.
 * Duplicates are allowed, and new items with duplicate keys are added after any matching items.
 * 
 * Internally, at its simplest, an AddendumList is one big sorted array, but additions are added to a 
 * small secondary (addendum) array. Searching first checks the big array, and if a match is not found
 * it then checks the addendum array. Searching is fast because it can use binary search, and adding
 * is fast because adds are added into the small addendum array.
 * 
 * In fact there can be multiple levels of addendum arrays of exponentially decreasing sizes.
 * Searching works it's way through all of them. 
 * 
 * All additions are to the last array. When the last array becomes full, it is merged with the preceding
 * array if it is of equal or greater size. This merging might then continue up the chain to the top.
 * 
 * Invariant: The last array is never allowed to become full. When add causes it to become full, it is
 * possibly merged, and then a new empty minimum size array is added for future additions.
 *  
 * The implementation the AddendumList is stored internally as an array of arrays.
 * 
 * The top level array (called level 1) contains references to the 2nd level arrays.
 * 
 *
 * NOTE: normally fields, internal nested classes and non API methods should all be private,
 *       however some have been made public so that the tester code can set them
 */
@SuppressWarnings("unchecked")  // added to suppress warnings about all the type casting of Object arrays
public class AddendumList<E> implements Iterable<E> {
	private static final int L1_STARTING_SIZE = 4;
	private static final int L2_MINIMUM_SIZE = 4;   
	public int size;             // total number of elements stored
	public Object[] l1array;     // really is an array of L2Array, but compiler won't let me cast to that
	public int l1numUsed;
	private Comparator<E> comp;

	// create an empty list
	// always have at least 1 second level array even if empty, makes code easier 
	// (DONE)
	public AddendumList(Comparator<E> c){
		size = 0;
		l1array = new Object[L1_STARTING_SIZE];        // you can't create an array of a generic type
		l1array[0] = new L2Array(L2_MINIMUM_SIZE);    // first 2nd level array
		l1numUsed = 1;
		comp = c;
	}

	// nested class for 2nd level arrays
	// (DONE)
	public class L2Array {
		public E[] items;  
		public int numUsed;

		public L2Array(int capacity) {
			items = (E[])new Object[capacity];  // you can't create an array of a generic type
			numUsed = 0;
		}
	}

	//total size (number of entries) in the entire data structure
	// (DONE)
	public int size(){
		return size;
	}

	// null out all references so garbage collector can grab them
	// but keep the now empty l1array 
	// (DONE)
	public void clear(){
		size = 0;
		Arrays.fill(l1array, null);  // clear l1 array
		l1array[0] = new L2Array(L2_MINIMUM_SIZE);    // first 2nd level array
		l1numUsed = 1;
	}


	// Find the index of the 1st matching entry in the specified array.
	// If the item is NOT found, it returns a negative value (-(insertion point) - 1). 
	//   The insertion point is defined as the point at which the key would be inserted into the array,
	//   (which may be the index after the last item)
	public int findFirstInArray(E item, L2Array a){
		int cmpResult = 0;										//stores result of comparator
		for (int i = 0; i < a.numUsed; i++) {					//loops through all elements in a
			cmpResult = comp.compare(item, a.items[i]);
			if (cmpResult == 0) {								//item found
				return i;
			}
			else if (cmpResult < 0) {
				return -i - 1;									//passed where item should go
			}
		}
		return -a.numUsed - 1;									//item should go at end of array
	}


	/**
	 * check if list contains a match
	 * use the findFirstInArray() helper method
	 */
	public boolean contains(E item){
		for (int i = 0; i < l1numUsed; i++) {
			if (findFirstInArray(item, (L2Array)l1array[i]) >= 0) return true;
		}
		return false;  // never found a match
	}

	
	// find the index of the insertion point of this item, which is
	// the index after any smaller or matching entries
	// this might be an unused slot at the end of the array
	// note: this will only be used on the last (very small) addendum array
	public int findIndexAfter(E item, L2Array a){
		// TO DO

		return 0;
	}

	/** 
	 * add object after any other matching values
	 * 
	 * adds to the last addendum array
	 * findIndexAfter() will give the insertion position
	 * if that array becomes full, you may need to invoke merging
	 * and then create a new empty final addendum array
	 * 
	 * remember to increment numUsed for the L2Array inserted into, and increment size for the whole data structure
	 */
	public boolean add(E item){
		// TO DO
		
		return true;
	}
	
	// merge the last two levels
	// if there are matching items, those from the earlier array go first in the merged array
	// note: this method does not add a new empty addendum array to the end, that will need to be done elsewhere 
	public void merge1Level() {
		// TO DO

	}
	
	// merge all levels
	// this is used by iterator(), toArray() and subList()
	// this makes these easy to implement. and the O(N) full merge time would likely be required for these operations anyway
	// Note: after merging, all items will be in the first l2array, but there may still be a second empty level2 addendum array
	// at the end.
	// The result of the merging should still be a valid addendum array with room to add in the last addendum array.
	// The merging will likely cause the size of the array to no longer be a power of two.
	private void mergeAllLevels() {
		// TO DO
	
	}

	/**
	 * copy the contents of the AddendumList into the specified array
	 * @param a - an array of the actual type and of the correct size
	 * @return the filled in array
	 */
	public E[] toArray(E[] a){
		// TO DO
		
		return a;
	}

	/**
	 * returns a new independent AddendumList 
	 * whose elements range from fromElemnt, inclusive, to toElement, exclusive
	 * The original list is unaffected.
	 * findFirstIndexOf() will be useful.
	 * @param fromElement
	 * @param toElement
	 * @return the sublist
	 */
	public AddendumList<E> subList(E fromElement, E toElement){
		// TO DO
		
		return null;
	}

	/**
	 * returns an iterator for this list
	 * this method just merges the items into a single array and creates an instance of the inner Itr() class
	 * (DONE)   
	 */
	public Iterator<E> iterator() {
		mergeAllLevels();
		return new Itr();
	}

	/**
	 * Iterator 
	 */
	private class Itr implements Iterator<E> {
		int index;

		/*
		 * create iterator at start of list
		 */
		Itr(){
			// TO DO

		}

		/**
		 * check if more items
		 */
		public boolean hasNext() {
			// TO DO
			
			return false;
		}

		/**
		 * return item and move to next
		 * throws NoSuchElementException if off end of list
		 */
		public E next() {
			// TO DO

			return null;
		}

		/**
		 * Remove is not implemented. Just use this code.
		 * (DONE)
		 */
		public void remove() {
			throw new UnsupportedOperationException();	
		}
	}
}
