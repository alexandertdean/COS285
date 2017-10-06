package student;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
		int index = binaryFindFirst2(0, a.numUsed - 1, item, a);
		return index;
	}
	
	//This search continues unless item is first in array
	//This is slower than binaryFindFirst2(), but maybe not for larger arrays
	private int binaryFindFirst1(int first, int last, E item, L2Array a) {
		int mid = (last + first) / 2;
		int cmpResult = comp.compare(a.items[mid], item);
		if (cmpResult == 0) {
			if (mid == 0 || comp.compare(a.items[mid], a.items[mid - 1]) != 0) return mid;
			else return binaryFindFirst1(first, mid - 1, item, a);
		}
		else if (last < first) return -first - 1;
		else if (cmpResult > 0) return binaryFindFirst1(first, mid - 1, item, a);
		else if (cmpResult < 0) return binaryFindFirst1(mid + 1, last, item, a);
		else return -404;
	}
	
	//This search iterates backwards after any match is found
	private int binaryFindFirst2(int first, int last, E item, L2Array a) {
		int mid = (last + first) / 2;
		if (a.items[mid] == null) {
			return binaryFindFirst2(first, mid - 1, item, a);
		}
		int cmpResult = comp.compare(a.items[mid], item);
		if (last < first) return -first - 1;
		else if (cmpResult == 0) {
			while (mid != 0 && comp.compare(a.items[mid], a.items[mid - 1]) == 0) {
				mid--;
			}
			return mid;
		} else if (cmpResult > 0) {
			return binaryFindFirst2(first, mid - 1, item, a);
		}
		else if (cmpResult < 0) {
			return binaryFindFirst2(mid + 1, last, item, a);
		}
		return -mid - 1;
	}
	
	//This search iterates forwards after any match is found
	private int binaryFindAfter2(int first, int last, E item, L2Array a) {
		int mid = (last + first) / 2;
		int cmpResult = comp.compare(a.items[mid], item);
		if (last < first) return first;
		else if (cmpResult == 0) {
			while (mid != a.numUsed - 1 && comp.compare(a.items[mid + 1], a.items[mid]) == 0) {
				mid++;
			}
			return mid + 1;
		} else if (cmpResult > 0) return binaryFindAfter2(first, mid - 1, item, a);
		else if (cmpResult < 0) return binaryFindAfter2(mid + 1, last, item, a);
		return first;
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
		int index = binaryFindAfter2(0, a.numUsed - 1, item, a);
		return index;
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
		L2Array insertionArray = (L2Array)l1array[l1numUsed - 1];
		if (insertionArray.numUsed == 0) {
			//ARRAY IS EMPTY! JUST STICK IT IN
			insertionArray.items[0] = item;
			insertionArray.numUsed++;
			size++;
		} else if (insertionArray.numUsed == insertionArray.items.length) {
			//MAKE NEW ARRAY
			if (l1numUsed == l1array.length) {
				//L1array full, resize by 150% and copy over
				Object[] tempArray = new Object[(int) Math.ceil((l1numUsed * 1.5))];	//resize l1array by 150%. Java does it for ArrayLists, so it must be good
				System.arraycopy(l1array, 0, tempArray, 0, l1numUsed);
				l1array = tempArray;
			}
			l1array[l1numUsed] = new L2Array(L2_MINIMUM_SIZE);
			l1numUsed++;													//added new l2array
			insertionArray = (L2Array)l1array[l1numUsed - 1];				//reassigns insertionArray to new array
			insertionArray.items[0] = item;									//L2Array is empty, first index will mean array is still sorted
			insertionArray.numUsed++;										//added new element to L2array
			size++;															//added new element to AddendumList
		} else {
			int mergeIndex = findIndexAfter(item, insertionArray);
			for (int i = insertionArray.numUsed; i > mergeIndex; i--) {
				//shift items over to make room for new element
				insertionArray.items[i] = insertionArray.items[i - 1];
			}
			insertionArray.items[mergeIndex] = item;						//insert item into array
			insertionArray.numUsed++;										//added new element to L2Array
			size++;															//added new element to AddenumList
			//CHECK TO SEE IF WE NEED TO MERGE L2ARRAYS
			boolean mergeCheck = l1numUsed > 1 && ((L2Array)(l1array[l1numUsed - 1])).numUsed >= ((L2Array)(l1array[l1numUsed - 2])).numUsed;
			if (mergeCheck) {
				while (mergeCheck) {
					merge1Level();
					mergeCheck = l1numUsed > 1 && ((L2Array)(l1array[l1numUsed - 1])).numUsed >= ((L2Array)(l1array[l1numUsed - 2])).numUsed;
				}
			}
			if (((L2Array)(l1array[l1numUsed - 1])).numUsed == ((L2Array)(l1array[l1numUsed - 1])).items.length) {
				if (l1numUsed == l1array.length) {
					//L1array full, resize by 150% and copy over
					Object[] tempArray = new Object[(int) Math.ceil((l1numUsed * 1.5))];	//resize l1array by 150%. Java does it for ArrayLists, so it must be good
					System.arraycopy(l1array, 0, tempArray, 0, l1numUsed);
					l1array = tempArray;
				}
				l1array[l1numUsed] = new L2Array(L2_MINIMUM_SIZE);
				l1numUsed++;
			}
		}
		return true;
	}
	
	// merge the last two levels
	// if there are matching items, those from the earlier array go first in the merged array
	// note: this method does not add a new empty addendum array to the end, that will need to be done elsewhere 
	//todo: make this stable. I believe it is unstable if both arrays have duplicates of the same value
	public void merge1Level() {
		L2Array secondLastArray = (AddendumList<E>.L2Array) l1array[l1numUsed - 2];
		L2Array lastArray = (AddendumList<E>.L2Array) l1array[l1numUsed - 1];
		L2Array newArray = new L2Array(lastArray.numUsed + secondLastArray.numUsed);
		int lastIndex = 0;
		int secondLastIndex = 0;
		while (lastIndex < lastArray.numUsed && secondLastIndex < secondLastArray.numUsed) {
			int cmpResult = comp.compare(lastArray.items[lastIndex], secondLastArray.items[secondLastIndex]);
			if (cmpResult < 0) 
				newArray.items[lastIndex + secondLastIndex] = lastArray.items[lastIndex++];
			else newArray.items[lastIndex + secondLastIndex] = secondLastArray.items[secondLastIndex++];
			newArray.numUsed++;
		}
		while (lastIndex < lastArray.numUsed) {
			newArray.items[lastIndex + secondLastIndex] = lastArray.items[lastIndex++];
			newArray.numUsed++;
		}
		while (secondLastIndex < secondLastArray.numUsed) {
			newArray.items[lastIndex + secondLastIndex] = secondLastArray.items[secondLastIndex++];
			newArray.numUsed++;
		}
		l1array[l1numUsed - 2] = newArray;
		l1array[l1numUsed - 1] = null;
		l1numUsed--;
	}
	
	// merge all levels
	// this is used by iterator(), toArray() and subList()
	// this makes these easy to implement. and the O(N) full merge time would likely be required for these operations anyway
	// Note: after merging, all items will be in the first l2array, but there may still be a second empty level2 addendum array
	// at the end.
	// The result of the merging should still be a valid addendum array with room to add in the last addendum array.
	// The merging will likely cause the size of the array to no longer be a power of two.
	private void mergeAllLevels() {
		while (l1numUsed > 1) {
			merge1Level();
		}
		if  (((L2Array)l1array[0]).items.length == ((L2Array)l1array[0]).numUsed) {
			l1numUsed++;
			l1array[1] = new L2Array(L2_MINIMUM_SIZE);
		}
	}

	/**
	 * copy the contents of the AddendumList into the specified array
	 * @param a - an array of the actual type and of the correct size
	 * @return the filled in array
	 */
	public E[] toArray(E[] a){
		AddendumList<E> newAL = new AddendumList<E>(comp);
		newAL.l1numUsed = 0;
		//copy elements from previous l2arrays into new l2arrys in newAL
		for (int i = 0; i < this.l1numUsed; i++) {
			L2Array originalArray = (L2Array)l1array[i];
			newAL.l1array[i] = new L2Array(originalArray.numUsed);
			System.arraycopy(originalArray.items, 0, ((L2Array)newAL.l1array[i]).items, 0, originalArray.numUsed);
			((L2Array)newAL.l1array[i]).numUsed = originalArray.numUsed;
			newAL.l1numUsed++;
		}
		newAL.mergeAllLevels();
		System.arraycopy(((L2Array)newAL.l1array[0]).items, 0, a, 0, ((L2Array)newAL.l1array[0]).numUsed);
		return a;
	}

	/**
	 * @param fromElement
	 * @param toElement
	 * @return the sublist
	 */
	public AddendumList<E> subList(E fromElement, E toElement){
		//POST: returns an AddendumList with elements inclusively between fromElement and toElement
		AddendumList<E> newAL = new AddendumList<E>(comp);
		newAL.l1numUsed = 0;
		//copy elements from previous l2arrays into new l2arrys in newAL
		for (int i = 0; i < this.l1numUsed; i++) {
			L2Array originalArray = (L2Array)l1array[i];
			newAL.l1array[i] = new L2Array(originalArray.numUsed);
			System.arraycopy(originalArray.items, 0, ((L2Array)newAL.l1array[i]).items, 0, originalArray.numUsed);
			((L2Array)newAL.l1array[i]).numUsed = originalArray.numUsed;
			newAL.l1numUsed++;
		}
		newAL.mergeAllLevels();
		
		//Find proper start and end indexes
		int startIndex = findFirstInArray(fromElement, (L2Array)newAL.l1array[0]);
		if (startIndex < 0) startIndex = -startIndex - 1;
		int sizeToCopy = findIndexAfter(toElement, (L2Array)newAL.l1array[0]) - startIndex;
		
		//Create new array of proper size for sublist and copy elements over
		E[] tempArray = (E[])new Object[sizeToCopy];
		System.arraycopy(((L2Array)(newAL.l1array[0])).items, startIndex, tempArray, 0, sizeToCopy);
		((L2Array)(newAL.l1array[0])).items = tempArray;																//assigns new array to AddendumList
		((L2Array)(newAL.l1array[0])).numUsed = sizeToCopy;
		return newAL;
	}

	public Iterator<E> iterator() {
		//POST: returns iterator for AddendumList
		mergeAllLevels();							//merges all l2 arrays into a single array
		return new Itr();
	}

	//Iterator
	private class Itr implements Iterator<E> {
		//POST: creates iterator at start of AddendumList
		int index;
		
		Itr(){
			index = 0;
		}

		public boolean hasNext() {
			//POST: returns true if index has not reached end of array
			if ((index < ((L2Array)l1array[0]).numUsed)) return true;
			return false;
		}

		public E next() {
			//POST: returns item at index or throws NoSuchElementException if index is greater than size of array
			if (hasNext()) {
				index++;
				return ((L2Array)l1array[0]).items[index - 1];
			}
			else throw new NoSuchElementException();
		}

		//NOT YET IMPLEMENTED
		public void remove() {
			throw new UnsupportedOperationException();	
		}
		
		
	}
}
