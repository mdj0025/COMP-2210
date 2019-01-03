import java.io.File;

import java.io.IOException;
import java.util.HashMap;



import java.util.Random;

import java.util.Scanner;

import java.util.Set;

/**
 * MarkovModel.java Creates an order k Markov model of the supplied source
 * text. The value of k determines the size of the "kgrams" used to generate
 * the model. A kgram is a sequence of k consecutive characters in the source
 * text.
 *
 * @author     Your Name (you@auburn.edu)
 * @author     Dean Hendrix (dh@auburn.edu)
 * @version    2018-04-17
 *
 */
public class MarkovModel {

   // Map of <kgram, chars following> pairs that stores the Markov model.
   private HashMap<String, String> model;

   // add other fields as you need them ...

   String value;
   /**
    * Reads the contents of the file sourceText into a string, then calls
    * buildModel to construct the order k model.
    *
    * DO NOT CHANGE THIS CONSTRUCTOR.
    *
    */
   
   public MarkovModel(int k, File sourceText) {
      
      model = new HashMap<>();
      try {
         String text = new Scanner(sourceText).useDelimiter("\\Z").next();
         buildModel(k, text);
      }
      catch (IOException e) {
         System.out.println("Error loading source text: " + e);
      }
   }


   /**
    * Calls buildModel to construct the order k model of the string sourceText.
    *
    * DO NOT CHANGE THIS CONSTRUCTOR.
    *
    */
   public MarkovModel(int k, String sourceText) {
      model = new HashMap<>();
      buildModel(k, sourceText);
   }


   /**
    * Builds an order k Markov model of the string sourceText.
    */
   private void buildModel(int k, String sourceText) {
      int s = 0;
      int z = 0;
      value = sourceText.substring(0 , k);
      while (s + k <= sourceText.length()) {
         String empt = "";
         String kg = sourceText.substring(s, s + k);
         if (!model.containsKey(kg)) {
            int j = k;
            while (z + j < sourceText.length()) {
               String g = sourceText.substring(z, z + j);
               if (z + k >= sourceText.length()) {
                  empt += '\u0000';
               }
               if (kg.equals(g)) {
                  empt += sourceText.substring(z + j, z + j + 1);
               }
               z++;
            }
            model.put(kg, empt);
         }
         z = 0;
         s++;
      }
   }


   /** Returns the first kgram found in the source text. */
   public String getFirstKgram() {
      return value;
   }


   /** Returns a kgram chosen at random from the source text. */
   public String getRandomKgram() {
      int k = model.size();
      int z = 0;
      Random random = new Random();
      int newIndex = random.nextInt(k);
      for (String s : model.keySet()) {
         if (newIndex == z) {
            return s;
         }
         z++;
      }
      return null;
   }


   /**
    * Returns the set of kgrams in the source text.
    *
    * DO NOT CHANGE THIS METHOD.
    *
    */
   public Set<String> getAllkgrams() {
      return model.keySet();
   }


   /**
    * Returns a single character that follows the given kgram in the source
    * text. This method selects the character according to the probability
    * distribution of all characters that follow the given kgram in the source
    * text.
    */
   public char getNextChar(String kgram) {
      Random rand = new Random();
      String empt = "";
      int z = 0;
      for (String n: model.keySet()) {
         if (n.equals(kgram)) {
            empt = model.get(kgram);
            int i = empt.length();
            if (i > 0) {
               z = rand.nextInt(i) + 1;
            }
         }
      }
      int q = z - 1;
      if (!empt.equals("")) {
         return empt.charAt(q);
      }
      return '\u0000';
   }


   /**
    * Returns a string representation of the model.
    * This is not part of the provided shell for the assignment.
    *
    * DO NOT CHANGE THIS METHOD.
    *
    */
   @Override
    public String toString() {
      return model.toString();
   }

}
