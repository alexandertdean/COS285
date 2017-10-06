package student;

import java.util.Arrays;
import java.util.Comparator;

public class BreakAL {
	public static void main(String[] args) {
		Comparator<Integer> comp = new myCmp();
		AddendumList<Integer> list = new AddendumList<Integer>(comp);
		for (int i = 0; i < 1000; i++) {
			list.add(i % 23);
		}
		for (int i = 0; i < list.l1numUsed; i++) {
			System.out.println(Arrays.toString(((AddendumList<Integer>.L2Array)list.l1array[i]).items));
		}
	}
	
	public static class myCmp implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
		
	}
}
