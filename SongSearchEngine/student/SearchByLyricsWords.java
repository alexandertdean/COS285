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
		commonWords = new TreeSet<String>(Arrays.asList("the", "of", "and", "a", "to", "in", "is", "you", "that", "it", "he", "for", "was", "on", "are", "as", "with", "his", "they", "at", "be", "this", "from", "I", "have", "or", "by", "one", "had", "not", "but", "what", "all", "were", "when", "we", "there", "can", "an", "your", "which", "their", "if", "do", "will", "each", "how", "them", "then", "she", "many", "some", "so", "these", "would", "into", "has", "more", "her", "two", "him", "see", "could", "no", "make", "than", "been", "its", "now", "my", "made", "did", "get", "our", "me", "too"));
		map = new TreeMap<String,TreeSet<Song>>();
		for(Song song : sc.getAllSongs()) {
			lyrics = song.getLyrics().split("[^a-zA-Z]", 0);
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
		if (args.length == 0) {
			System.out.println("Invalid number of arguments.");
			return;
		}
		
		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsWords search = new SearchByLyricsWords(sc);
		if (args.length > 1) {
			System.out.println("Search for: " + args[1]);
			Song[] result = search.search(args[1]);
			System.out.println("Result size: " + result.length);
			for (int i = 0; i < Math.min(result.length,10); i++) {
				System.out.println(result[i]);
			}
		}
		
		if (args.length > 2) {
			if (args[2].equals("-top10words")) {
				search.top10words();
			}
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
	
	public Song[] search(String lyricsWords) {
		TreeSet<Song> result = new TreeSet<Song>();
		String[] searchWords = lyricsWords.split("[^a-zA-Z]");
		for (String word : searchWords) {
			word = word.toLowerCase();
			if (commonWords.contains(word) || word.length() < 2);
			else if (result.isEmpty()) {
				if (map.containsKey(word)) {
					result.addAll(this.map.get(word));
				}
			} else {
				result.retainAll(this.map.get(word));
			}
		}
		return result.toArray(new Song[result.size()]);
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
