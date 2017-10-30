package student;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SearchByLyricsWords {
	public TreeSet<String> commonWords;
	public TreeMap<String,TreeSet<Song>> map;
	
	public SearchByLyricsWords(SongCollection sc) throws FileNotFoundException {
		String[] lyrics;
		Scanner fileInput = new Scanner(new FileReader("commonWords.txt"));
		commonWords = new TreeSet<String>();
		map = new TreeMap<String,TreeSet<Song>>();
		while (fileInput.hasNext()) {
			commonWords.add(fileInput.next());
		}
		fileInput.close();
		for(Song song : sc.getAllSongs()) {
			lyrics = song.getLyrics().split("[^a-zA-z]|`|\\[|\\]|(\\\\)|_", 0);
			for (String word : lyrics) {
				word = word.toLowerCase();
				if (!commonWords.contains(word) && (word.length() > 1) && !map.containsKey(word)) {
					map.put(word, new TreeSet<Song>());
					map.get(word).add(song);
				} else if (map.containsKey(word)) {
					map.get(word).add(song);
				}
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		SongCollection sc = new SongCollection("allSongs.txt");
		SearchByLyricsWords search = new SearchByLyricsWords(sc);
		Scanner debugger = new Scanner(new FileReader("debugAllKeysInMap.txt"));
		while (debugger.hasNext()) {
			String temp = debugger.next();
			if (!search.map.containsKey(temp)) System.out.println("No value for " + temp);
		}
		debugger = new Scanner(new FileReader("debugAllKeysAndCounts.txt"));
		while (debugger.hasNext()) {
			String temp = debugger.next();
			int tempCnt = debugger.nextInt();
			if (search.map.get(temp).size() != tempCnt) System.out.println("File says " + tempCnt + " for " + temp + ". Found " + search.map.get(temp).size());
		}
		search.statistics();
		if (args[0].equals("-top10words")) {
			search.top10words();
		}
	}
	
	public void top10words() {
		TreeSet<RankedItem> topWords = new TreeSet<RankedItem>(new compareRank());
		Set<String> set = map.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			if (!topWords.isEmpty()) {
				if ((map.get(temp).size() > topWords.first().count) && (topWords.size() < 10)) {
					topWords.add(new RankedItem(temp, map.get(temp).size()));
				} else if (topWords.size() >= 10) {
					if (map.get(temp).size() > topWords.first().count) {
						topWords.remove(topWords.first());
						topWords.add(new RankedItem(temp, map.get(temp).size()));
					}
				}
			} else if (topWords.isEmpty()) topWords.add(new RankedItem(temp, map.get(temp).size()));
		}
		RankedItem[] topArray = new RankedItem[10];
		Arrays.toString(topWords.toArray(topArray));
		System.out.println();
		System.out.println("TOP WORDS:");
		System.out.printf("%-10s %s\n", "Word", "Count");
		System.out.println("_________________");
		for (RankedItem item : topArray) {
			System.out.printf("%-10s %d\n", item.name, item.count);
		}
		
	}
	
	public void statistics() {
		int songCount = 0;
		int mapSize = map.size();
		
		System.out.println("Number of keys in map: " + mapSize);
		Set<String> set = map.keySet();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String temp = iter.next();
			songCount +=  map.get(temp).size();
		}
		System.out.println("N: " + songCount);
		System.out.println("Size of map: " + mapSize * 6);
		System.out.println("Size of all Tree Sets: " + songCount * 6);
		System.out.println("Size of Data Structure: " + ((mapSize + songCount) * 6));
		System.out.println("Size usage: " + (((mapSize + songCount) * 6) / songCount) + "N");
	}
	
	public class compareRank implements Comparator<RankedItem> {

		@Override
		public int compare(RankedItem o1, RankedItem o2) {
			if (o1.count < o2.count) return -1;
			else if (o1.count > o2.count) return 1;
			else return 0;
		}
	}
}
