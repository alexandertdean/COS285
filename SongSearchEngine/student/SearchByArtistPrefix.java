package student;
import java.io.*;
import java.util.*;

/*
 * SearchByArtistPrefix.java
 * starting code
 * Boothe 2016
 */
public class SearchByArtistPrefix {

	private Song[] songs;  // The constructor fetches and saves a reference to the song array here

	public SearchByArtistPrefix(SongCollection sc) {
		songs = sc.getAllSongs();
	}

	/**
	 * find all songs matching artist prefix
	 * uses binary search
	 * should operate in time log n + k (# matches)
	 */
	public Song[] search(String artistPrefix) {
		Song tempSong = new Song(artistPrefix, "", "");
		Song.CmpArtist cmp = new Song.CmpArtist();
		ArrayList<Song> tempResult = new ArrayList<Song>();
		Song[] result;
		int loopCount = 0;
		int i = Arrays.binarySearch(songs, tempSong, cmp);
		if (i < 0) {
			i = (i * -1) - 1;
		} else {
			while((i != 0) && (cmp.compare(songs[i - 1], tempSong) == 0)) {
				i--;
			}
		}
		while(i < songs.length && songs[i].getArtist().toLowerCase().startsWith(artistPrefix.toLowerCase())) {
			loopCount++;
			tempResult.add(songs[i]);
			i++;
		}
		System.out.println("Number of Matches = " + loopCount + "\nNumber of compares in binary search = " + cmp.getCmpCnt());
		result = tempResult.toArray(new Song[tempResult.size()]);
		return result;
	}


	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByArtistPrefix sbap = new SearchByArtistPrefix(sc);

		if (args.length > 1){
			System.out.println("searching for: " + args[1]);
			Song[] byArtistResult = sbap.search(args[1]);

			// to do: show first 10 matches
			for (int i = 0; i < 10 && i < byArtistResult.length; i++) {
				System.out.println(byArtistResult[i].toString());
			}
		}
	}
}
