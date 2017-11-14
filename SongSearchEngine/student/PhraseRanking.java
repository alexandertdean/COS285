package student;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhraseRanking
{
	static int rankPhrase(String lyrics, String lyricsPhrase) 
	{
		TreeSet<Integer> ranks = new TreeSet<Integer>();
		lyrics = lyrics.toLowerCase();
		lyricsPhrase = lyricsPhrase.toLowerCase();
		int lastIndex = 0;
		int index = 0;
		int prevIndex = 0;
		String[] words = lyricsPhrase.split("[^a-zA-Z]", 0);
		while(true) 
		{
			for (int i = 0; i < words.length; i++)
			{
				Matcher m = Pattern.compile("\\b" + words[i] + "\\b").matcher(lyrics.substring(prevIndex));
				if (!m.find()) {
					if (ranks.isEmpty()) return 0;
					else return ranks.first();
				}
				index = lyrics.substring(prevIndex).indexOf(words[i]);
				if (index < 0)
				{
					if (ranks.isEmpty()) return 0;
					else return ranks.first();
				}
				index = index + words[i].length() + prevIndex;
				
				Matcher p = Pattern.compile("(\\b" + words[i] + "\\b)").matcher(lyrics.substring(prevIndex, index + 1));
				if (!p.find()) {
					i--;
				}
				
				prevIndex = index;
			}
			lastIndex = index;
			for (int i = words.length; i > 0; i--)
			{
				index = lyrics.substring(0, prevIndex).lastIndexOf(words[i-1]);
				Matcher p = Pattern.compile("(\\b" + words[i - 1] + "\\b)").matcher(lyrics.substring(index, prevIndex));
				if (!p.find())
				{
					i++;
				}
				prevIndex = index;
			}
			ranks.add(lastIndex - index);
			prevIndex = lastIndex;
		}
		
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
			if (rank > 0) {
				System.out.println(rank + " " + s.toString());
				count++;
			}
			
		}
		System.out.println(count + " matches.");
	}
}
