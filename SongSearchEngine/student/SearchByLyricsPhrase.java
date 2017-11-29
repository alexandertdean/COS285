package student;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SearchByLyricsPhrase {
	private SongCollection sc;
	
	public SearchByLyricsPhrase(SongCollection songCollection) {
		sc = songCollection;
	}
	
	public static void main(String[] args) 
	{
		if (args.length == 0) {
			System.out.println("Invalid number of arguments.");
			return;
		}
		
		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsPhrase search = new SearchByLyricsPhrase(sc);
		if (args.length > 1) {
			System.out.println("Search for: " + args[1]);
			Song[] result = search.search(args[1]);
			System.out.println("Result size: " + result.length);
		}
	}
	
	public Song[] search(String phrase)
	{
		SearchByLyricsWords searchFn = new SearchByLyricsWords(sc);
		Song[] unranked = searchFn.search(phrase);
		ArrayList<RankedItem> ranked = new ArrayList<RankedItem>();
		for(Song s : unranked)
		{
			int rank = PhraseRanking.rankPhrase(s.getLyrics(), phrase);
			if (rank > 0) 
			{
				ranked.add(new RankedItem(s, rank));
			}
			
		}
		RankedItem[] rankedArray = new RankedItem[ranked.size()];
		ranked.toArray(rankedArray);
		CompareRank cmp = new CompareRank();
		Arrays.sort(rankedArray, cmp);
		Song[] result = new Song[rankedArray.length];
		System.out.println("rank artist title");
		for (int i = 0; i < rankedArray.length; i++)
		{
			if (i < Math.min(result.length,10)) {
				System.out.println(rankedArray[i].count + " " + rankedArray[i].name.toString());
			}
			result[i] = (Song)(rankedArray[i].name);	
		}
		return result;
	}
	
	public class CompareRank implements Comparator<RankedItem> {

		@Override
		public int compare(RankedItem o1, RankedItem o2) {
			if (o1.count < o2.count) return -1;
			else if (o1.count > o2.count) return 1;
			else return 0;
		}
	}
	
}
