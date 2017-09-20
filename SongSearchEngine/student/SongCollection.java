package student;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/*
 * SongCollection.java
 * Read the specified data file and build an array of songs.
 * 
 * Starting code by Prof. Boothe 2016
 * Code completed by Alexander Dean, 2017
 */
public class SongCollection {

	private Song[] songs;

	public SongCollection(String filename) {

		// read in the song file and build the songs array
		BufferedReader fileInput;										//scans input file for songs
		String artist;													//temporary location of artist during song creation
		String title;													//temporary location of title during song creation
		String line;													//temporary location of line of file
		ArrayList<Song> tempList = new ArrayList<Song>();				//temporary storage of songs
		StringBuilder lyricBuilder = new StringBuilder();
		try {
			fileInput = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {								//aborts construction if file not found
			System.out.print(e);
			return;
		}
		//Assumes format for input file as given in shortSongs.txt
		
		try {
			while (fileInput.ready()) {
					line = fileInput.readLine();
					artist = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));	//cuts name of artist from line
					line = fileInput.readLine();
					title = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));		//cuts name of title from line
					line = fileInput.readLine();
					lyricBuilder.append(line.substring(line.indexOf('\"') + 1));				//begins concatenating lines of lyrics together
					line = fileInput.readLine();
					while (!(line.startsWith("\""))) {											//continues until the last line of lyrics
						lyricBuilder.append("\n" + line);
						line = fileInput.readLine();
					}
					tempList.add(new Song(artist, title, lyricBuilder.toString()));
					lyricBuilder.setLength(0);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		songs = tempList.toArray(new Song[tempList.size()]);							//converts from ArrayList to "primitive" array
		Arrays.sort(songs);
		// sort the songs array
	}

	// returns the array of all Songs
	// this is used as the data source for building other data structures
	public Song[] getAllSongs() {
		return songs;
	}

	// testing method
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("usage: prog songfile");
			return;
		}
		SongCollection sc = new SongCollection(args[0]);
		System.out.println("Total songs = " + sc.getAllSongs().length + ", first songs:");		//prints length of array
		for (int i = 0; (i < 10) && (i < sc.getAllSongs().length-1); i++) {						//only prints number of songs in array if less than 10, otherwise, prints 10
			System.out.println(sc.getAllSongs()[i].toString());
		}
		
	}
}
