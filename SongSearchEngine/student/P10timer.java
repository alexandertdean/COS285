package student;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;

// timer for Prog10 searching and ranking of lyrics phrase
// read the allSongs.txt
// building the map of words to songs
// time doing 50 searches on famous song quotes
public class P10timer {

   static String[] quotes = {
      "a man hears what he wants to hear and disregards the rest",
      "All of us get lost in the darkness",
      "All you need is love",
      "And in the end, the love you take is equal to the love you make",
      "Before you accuse me take a look at yourself",
      "Bent out of shape from society's pliers",
      "Different strokes for different folks",
      "Don't ask me what I think of you",
      "Don't you draw the Queen of Diamonds",
      "Fear is the lock and laughter the key to your heart",
      "For what is a man, what has he got? If not himself",
      "Get your head out of the mud",
      "Hero not the handsome actor who plays a hero's role",
      "How many ears must one man have before he can hear people cry?",
      "I don't need no money, fortune, or fame",
      "I got a lot to say",
      "I understand about indecision",
      "I was born with a plastic spoon in my mouth",
      "I was so much older then, I'm younger than that now",
      "I'd rather be a hammer than a nail",
      "If you choose not to decide, you still have made a choice",
      "If you smile at me I will understand",
      "It ain't me, it ain't me, I ain't no Senator's son",
      "It seems to me, sorry seems to be the hardest word",
      "It's been a hard day's night",
      "Just slip out the back, Jack",
      "Love when you can, cry when you have to",
      "Nobody wants him, he just stares at the world",
      "Ob-la-di, Ob-la-da, life goes on",
      "Same old song, just a drop of water in an endless sea",
      "Send me dead flowers to my wedding",
      "Some people never come clean, I think you know what I mean",
      "Talk about your plenty, talk about your ills, one man gathers what another man spills",
      "The pain of war cannot exceed the woe of aftermath",
      "The preacher said, you know you always have the Lord by your side",
      "The swift don't win the race",
      "The words of the prophets are written on the subway walls",
      "Then one day you find, ten years have got behind you",
      "There are places I remember all my life",
      "There's an opera out on the turnpike",
      "Time can bring you down",
      "War is not the answer",
      "We learned more from a three minute record than we ever learned in school",
      "Well, you don't tug on Superman's cape",
      "We're just two lost souls swimming in a fish bowl year after year",
      "When I first saw you with your smile so tender",
      "When you call my name, I salivate like Pavlov's dog",
      "You can't always get what you want",
      "You don't have to be old to be wise",
      "She loves you"
   };

   static int correctNumMatches[] = {1, 1, 81, 1, 1, 1, 1, 3, 2, 1, 1, 1, 1, 1, 2,
      18, 1, 2, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 9, 2, 1, 1, 1, 1, 1, 5, 1, 58}; 

   public static void main(String[] args) {
      if (args.length == 0) {
         System.err.println("uses arguments: songfile [-verbose]");
         System.err.println("\nTiming results are written to the file: timeinfofile");
         return;
      }

      // command line argument to enable printing results
      boolean verbose = args.length == 2 && args[1].equals("-verbose");

      // read the song collection and build the search class' data structure
      SongCollection sc = new SongCollection(args[0]);
      SearchByLyricsPhrase searchByLP = new SearchByLyricsPhrase(sc);

      // it is too hard to check rankings because they could very a bit
      // based on treatment of endofline characters and possible future
      // liberalization of ranking rules
      // just check number of matches
      // start timing 
      long startTime = System.nanoTime();

      int tooManyCnt = 0, tooFewCnt=0;
      for (int i = 0; i < quotes.length; i++) { // for every test phrase
         String phrase = quotes[i];      
         if (verbose) System.out.println("\nSearch: "+phrase);
         Song[] songList = searchByLP.search(phrase); 
         int matches = songList.length;
         if (verbose) { // list found songs for verification
            for (Song song : songList) { // for every song
               String lyrics = song.getLyrics();
               int rank = PhraseRanking.rankPhrase(lyrics, phrase);
               System.out.printf("%d %s \"%s\"\n", rank, song.getArtist(), song.getTitle());
            }
         }
         if (matches != correctNumMatches[i]) {
            System.out.printf("ERROR: search \"%s\"\nexpected %d matches but found %d\n",
                  phrase, correctNumMatches[i], matches);
            if (matches < correctNumMatches[i])
               tooFewCnt++;
            else
               tooManyCnt++;
         }
      }

      // stop timing
      long finishTime = System.nanoTime();

      // Calculate the elapsed time:
      long elapsedTime = finishTime - startTime;
      File f = new File("timeinfofile");
      PrintStream outputFile = null;
      try {
         outputFile = new PrintStream(f);
      } catch (FileNotFoundException e) {
         System.err.println("Could not create timeinfofile");
         System.exit(1);
      }
      outputFile.printf(" %.2fms ",(elapsedTime / 1000000.0) );
      if (tooFewCnt+tooManyCnt > 0) {
         outputFile.print("  errors ");
         if (tooFewCnt > 0)
            outputFile.printf("(%d too few) ", tooFewCnt);
         if (tooManyCnt > 0)
            outputFile.printf("(%d too many) ", tooManyCnt);
      }
      outputFile.println();
      outputFile.close();
   }  

   /**
    * helper method to print out the first N from an array of songs
    * copied from my SongCollection
    * @param array of songs
    */
   public static void showFirstNSongs(Song[] songList, int n) {
      if (songList == null || songList.length == 0) {
         System.out.println("No results returned.");
         return;
      }
      System.out.print("Total songs = " + songList.length);
      if (n < songList.length) {
         System.out.println(", first " + n + " matches:");
      } else {
         System.out.println(" :");
         n = songList.length;
      }

      for (int i = 0; i < n; i++)
         System.out.println(songList[i]);
   }

}
