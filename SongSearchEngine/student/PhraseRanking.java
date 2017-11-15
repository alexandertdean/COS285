package student;

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
		String[] words = lyricsPhrase.split("[^a-zA-Z]", 0);					//divides phrase into array of words
		while(true) 										
		{
			//GO FORWARDS THROUGH SONG AND FIND ANY PHRASE THAT HAS A RANK
			for (int i = 0; i < words.length; i++)
			{
				Matcher match = Pattern.compile("\\b" + words[i] + "\\b").matcher(lyrics.substring(prevIndex));
				if (!match.find()) {											//checks if next word is found in remaining song
					if (ranks.isEmpty()) return 0;								//no matches found
					else return ranks.first();									//returns lowest rank
				}
				else 
				{
					index = match.start() + words[i].length() + prevIndex;		//sets index to end of the matched word
				}
				prevIndex = index;												//increments index
			}
			
			lastIndex = index;													//last index solidified
			
			//GO BACKWARDS AND MAKE SURE RANK IS AS SMALL AS POSSIBLE
			for (int i = words.length; i > 0; i--)
			{
				index = lyrics.substring(0, prevIndex + 1).lastIndexOf(words[i-1]);		//finds closest match of word to next word
				Matcher p = Pattern.compile("(\\b" + words[i - 1] + "\\b)").matcher(lyrics.substring(index, prevIndex));
				if (!p.find())													//checks if word is actually whole word, or just part of a larger word
				{
					i++;														//repeat this word with new starting point
				}
				prevIndex = index;
			}
			ranks.add(lastIndex - index);										//adds ranking of this phrase to set of ranks
			prevIndex = lastIndex;												//check rest of the song after already found matches
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
			if (rank > 0) 
			{
				System.out.println(rank + " " + s.toString());
				count++;
			}
			
		}
		System.out.println(count + " matches.");
	}
}
