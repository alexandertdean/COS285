package student;

import java.util.Comparator;

public class SearchByTitlePrefix {
	AddendumList<Song> songs;
	Song[] songArray;
	Song.CmpTitle comp = new Song.CmpTitle();
	
	public SearchByTitlePrefix(SongCollection sc) {
		songs = new AddendumList<Song>(comp);
		songArray = sc.getAllSongs();
		for (int i = 0; i < songArray.length; i++) {
			songs.add(songArray[i]);
		}
		System.out.println("Number of comparisons to create song array: " + comp.getCmpCnt());
	}
	
	public Song[] search(String titlePrefix) {
		comp.resetCmpCnt();
		StringBuilder last = new StringBuilder();
		String lastElement;
		for (int i = 0; i < (titlePrefix.length() - 1); i++) {
			last.append(titlePrefix.charAt(i));
		}
		last.append((char)(titlePrefix.charAt(titlePrefix.length() - 1) + 1));
		lastElement = last.toString();
		AddendumList<Song> result = songs.subList(new Song("", titlePrefix, ""), new Song("", lastElement, ""));
		Song[] arrayResult = new Song[result.size()];
		System.out.println("Search used: " + comp.getCmpCnt() + " comparisons");
		return result.toArray(arrayResult);
	}
	
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile [search string]");
			return;
		}

		SongCollection sc = new SongCollection(args[0]);
		SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);

		if (args.length > 1){
			Song[] byTitleResult = sbtp.search(args[1]);
			System.out.println("\nSearch for " + args[1] + " found " + byTitleResult.length + " results.");
			// to do: show first 10 matches
			for (int i = 0; i < 10 && i < byTitleResult.length; i++) {
				System.out.println(byTitleResult[i].toString());
			}
		}
	}
}
