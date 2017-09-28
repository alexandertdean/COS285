package student;

import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;

/* testing code for finished Addendum List
 * There is a default test case, but
 * you can also specify an argument on the command line that will be
 * processed as a sequence of characters to insert into the list.
 * 
 * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
 */
public class AddendumListTesterProg5 {
  public static void main(String[] args) throws FileNotFoundException {
    System.out.println("testing routine for Addendum List");
    String order = "qwertyuiopasdfghjklzxcvbnmamz@~";  // default test
    if (args.length > 0)
      order = args[0];   // use user specified test case instead

    // insert them character by character into the list
    System.out.println("insertion order: "+order);
    Comparator<String> comp = new StringCmp();
    ((CmpCnt)comp).resetCmpCnt();            // reset the counter inside the comparator
    AddendumList<String> addList = new AddendumList<String>(comp);
    for (int i = 0; i < order.length(); i++){
      String s = order.substring(i, i+1);
      addList.add(s);
    }
    System.out.println("The number of comparison to build the Addendum List = "+
        ((CmpCnt)comp).getCmpCnt());

    System.out.println("TEST: after adds - data structure dump");
    dump(addList);

    stats(addList, comp);     

    System.out.println("TEST: toArray");
    String[] a = new String[addList.size()];
    addList.toArray(a);
    for (int i=0; i<a.length; i++)
      System.out.print("["+a[i]+"]");
    System.out.println();

    System.out.println("TEST: sublist(e,o)");
    AddendumList<String> sublist = addList.subList("e", "o");
    dump(sublist); 

    System.out.println("TEST: sublist(emu,owl)");
    AddendumList<String> sublist2 = addList.subList("emu", "owl");
    dump(sublist2); 

    System.out.println("TEST: iterator");
    Iterator<String> itr = addList.iterator();
    while (itr.hasNext())
      System.out.print("["+itr.next()+"]");
    System.out.println();
  }


  /**
   * string comparator with cmpCnt for testing
   */
  public static class StringCmp extends CmpCnt implements Comparator<String> {
    public int compare(String s1, String s2) {
      cmpCnt++;
      return s1.compareTo(s2);
    }
  }

  /**
   * print out an organized display of the list
   * intended for testing purposes on small examples
   * it looks nice for the test case where the objects are characters
   *
   * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
   */
  public static void dump(AddendumList<String> addList){
    for (int i1 = 0; i1 < addList.l1array.length; i1++) {
      AddendumList<String>.L2Array l2array = 
          (AddendumList<String>.L2Array)(addList.l1array[i1]);
      System.out.print("[" + i1 + "] -> ");
      if (l2array == null)
        System.out.println("null");
      else {
        Object[] items = (Object[])(l2array.items); // can't seem to cast to array of strings
        int len = items.length;
        for (int i2 = 0; i2 < len; i2++) {
          String item = (String)(items[i2]);
          if (item == null)
            System.out.print("[ ]");
          else
            System.out.print("["+item+"]");
        }
        //System.out.println("  ("+l2array.numUsed+" of "+l2array.items.length+") used");
        System.out.println();
      }
    }
  }

  /**
   * calculate and display statistics
   * 
   * It use a comparator that implements the given CmpCnt interface.
   * It then runs through the list searching for every item and calculating
   * search statistics.
   * 
   * DO NOT MODIFY I WILL BE USING THIS FOR MY TESTING
   */
  public static void stats(AddendumList<String> addList, Comparator<String> comp){
    System.out.println("STATS:");
    int size = addList.size();
    System.out.println("list size N = "+ size);

    // level 1 array
    int l1numUsed = addList.l1numUsed;
    System.out.println("level 1 array " + l1numUsed + " of " + addList.l1array.length + " used.");

    // level 2 arrays
    System.out.print("level 2 sizes: ");
    for (int i1 = 0; i1 < addList.l1numUsed; i1++) {
      AddendumList<String>.L2Array l2array = 
          (AddendumList<String>.L2Array)(addList.l1array[i1]);
      System.out.print(l2array.numUsed + " ");
    }
    System.out.println();

    // search stats, search for every item
    int totalCmps = 0, minCmps = Integer.MAX_VALUE, maxCmps = 0;

    for (int i1 = 0; i1 < addList.l1numUsed; i1++) {
      AddendumList<String>.L2Array l2array = 
          (AddendumList<String>.L2Array)(addList.l1array[i1]);
      Object[] items = (Object[])(l2array.items); // can't seem to cast to array of strings
      for (int i2 = 0; i2 < l2array.numUsed; i2++) {
        String item = (String)(items[i2]);
        ((CmpCnt)comp).resetCmpCnt();
        if (!addList.contains(item))
          System.err.println("Did not expect an unsuccesful search in stats");
        int cnt = ((CmpCnt)comp).getCmpCnt();
        totalCmps += cnt;
        if (cnt > maxCmps)
          maxCmps = cnt;
        if (cnt < minCmps)
          minCmps = cnt;
      }
    }

    System.out.printf("Successful search: min cmps = %d, avg cmps = %.1f, max cmps %d\n",
        minCmps, (double)totalCmps/size, maxCmps);
  }

}
