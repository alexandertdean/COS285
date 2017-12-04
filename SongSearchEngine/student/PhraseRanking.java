package student;

import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseRanking
{
	static int rankPhrase(String lyrics, String lyricsPhrase) 
	{
		
		int firstIndex = 0;
		int prevIndex;
		int index = 0;
		int startingPoint = 0;
		TreeSet<Integer> ranks = new TreeSet<Integer>();
		lyrics = lyrics.toLowerCase();
		lyricsPhrase = lyricsPhrase.toLowerCase();
		String[] words = lyricsPhrase.split("[^a-zA-Z]", 0);					//divides phrase into array of words
		while(true)									//continues until code returns
		{
			int i = 0;
			prevIndex = startingPoint;
			while (i < words.length)
			{
				index = lyrics.substring(prevIndex).indexOf(words[i]) + prevIndex;
				if (index < prevIndex) return ranks.isEmpty() ? 0 : ranks.first();
				prevIndex = index + words[i].length();
				if (isValidMatch(index, words[i], lyrics))
				{
					if (i == 0) 
					{
						firstIndex = index;
						startingPoint = prevIndex;
					}
					i++;
				}
			}
			if ((prevIndex - firstIndex) == lyricsPhrase.length()) return prevIndex - firstIndex;
			ranks.add(prevIndex - firstIndex);
		}
		
	}
	
	private static boolean isValidMatch(int index, String word, String subLyrics)
	{
		if (index == 0 || !Character.isAlphabetic(subLyrics.charAt(index - 1)))
		{
			if (((index + word.length()) >= subLyrics.length()) || !Character.isAlphabetic(subLyrics.charAt(index + word.length())))
			{
				return true;
			}
			else return false;
		}
		else return false;
	}
	
	public static void main(String args[])
	{
		if (args.length < 2)
		{
			System.out.println("Usage: java PhraseRanking [song file] [lyrics phrase]");
			return;
		}
		
		SongCollection sc = new SongCollection(args[0]);
		SearchByLyricsWords searchFn = new SearchByLyricsWords(sc);
		Song[] unranked = searchFn.search(args[1]);
		int count = 0;
		
		for(Song s : unranked)
		{
			int rank = rankPhrase(s.getLyrics(), args[1]);
			if (rank > 0) 
			{
				System.out.println(rank + " " + s.toString());
				count++;
			}
			
		}
		System.out.println(count + " matches.");
	}
}
