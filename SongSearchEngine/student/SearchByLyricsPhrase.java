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
		
	}
	
	public Song[] search(String phrase)
	{
		SearchByLyricsWords searchFn = new SearchByLyricsWords(sc);
		Song[] unranked = searchFn.search(phrase);
		int count = 0;
		ArrayList<RankedItem> ranked = new ArrayList<RankedItem>();
		for(Song s : unranked)
		{
			int rank = PhraseRanking.rankPhrase(s.getLyrics(), phrase);
			if (rank > 0) 
			{
				ranked.add(new RankedItem(s, count));
			}
			
		}
		RankedItem[] rankedArray = new RankedItem[ranked.size()];
		ranked.toArray(rankedArray);
		CompareRank cmp = new CompareRank();
		Arrays.sort(rankedArray, cmp);
		Song[] result = new Song[rankedArray.length];
		for (int i = 0; i < rankedArray.length; i++)
		{
			result[i] = (Song)(rankedArray[i].name);	
		}
		return new Song[0];
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
