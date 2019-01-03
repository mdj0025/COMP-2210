import java.util.Arrays;

import sun.security.krb5.internal.KRBError;

import java.lang.*;
import java.util.*;

/**
* Defines a library of selection methods
* on arrays of ints.
*
* @author   Michael Johnson (mdj0025@auburn.edu)
* @author   Dean Hendrix (dh@auburn.edu)
* @version  2018-01-15
*
*/
public final class Selector {


   /**
    * Can't instantiate this class.
    *
    * D O N O T C H A N G E T H I S C O N S T R U C T O R
    *
    */
   private Selector() { }


   /**
    * Selects the minimum value from the array a. This method
    * throws IllegalArgumentException if a is null or has zero
    * length. The array a is not changed by this method.
    */
   public static int min(int[] a)throws IllegalArgumentException{
      if(a.length == 0 || a == null) {
         throw new IllegalArgumentException();
      }
      int min = a[0];
      for(int i = 0; i < a.length; i++){
         if(a[i] < min){
            min = a[i];
         }
      }
      return min;
   }


   /**
    * Selects the maximum value from the array a. This method
    * throws IllegalArgumentException if a is null or has zero
    * length. The array a is not changed by this method.
    */
   public static int max(int[] a)throws IllegalArgumentException {
      if(a.length == 0 || a == null)
         throw new IllegalArgumentException();
      int max = a[0];
      for(int i = 0; i < a.length; i++){
         if (a[i] > max){
            max = a[i];
         }
      }
      return max;
   }


   /**
    * Selects the kth minimum value from the array a. This method
    * throws IllegalArgumentException if a is null, has zero length,
    * or if there is no kth minimum value. Note that there is no kth
    * minimum value if k < 1, k > a.length, or if k is larger than
    * the number of distinct values in the array. The array a is not
    * changed by this method.
    */
   public static int kmin(int[] a, int k)throws IllegalArgumentException {
      if(a.length == 0 || a == null){
         throw new IllegalArgumentException();
      }
      else if(k < 1 || k > a.length){
         throw new IllegalArgumentException();
      }
      for (int i = 0; i < a.length; i++){
         for(int j = i + 1; j < a.length; j++){
            if(a[j] < a[i]){
               int right = a[i];
               a[i] = a[j];
               a[j] = right;
            }
         }
      }
      return a[k-1];
   }


   /**
    * Selects the kth maximum value from the array a. This method
    * throws IllegalArgumentException if a is null, has zero length,
    * or if there is no kth maximum value. Note that there is no kth
    * maximum value if k < 1, k > a.length, or if k is larger than
    * the number of distinct values in the array. The array a is not
    * changed by this method.
    */
   public static int kmax(int[] a, int k) {
      if(a.length == 0 || a == null){
         throw new IllegalArgumentException();
      }
      else if(k < 1 || k > a.length){
         throw new IllegalArgumentException();
      }
      //while i < length we want to compare the element at index j to index of i
      // if index of j is less than i then we want to swap the two and then keep swapping to the left
      for (int i = 0; i < a.length; i++){
         for(int j = i + 1; j < a.length; j++){
            if(a[j] < a[i]){
               int right = a[i];
               a[i] = a[j];
               a[j] = right;
            }
         }
      }
      return a[a.length - k];
   }


   /**
    * Returns an array containing all the values in a in the
    * range [low..high]; that is, all the values that are greater
    * than or equal to low and less than or equal to high,
    * including duplicate values. The length of the returned array
    * is the same as the number of values in the range [low..high].
    * If there are no qualifying values, this method returns a
    * zero-length array. Note that low and high do not have
    * to be actual values in a. This method throws an
    * IllegalArgumentException if a is null or has zero length.
    * The array a is not changed by this method.
    */
   public static int[] range(int[] a, int low, int high)throws IllegalArgumentException {
      if(a.length == 0 || a == null){
         throw new IllegalArgumentException();
      }
      int k = 0;
      for(int i = 0; i < a.length; i++){
         if(a[i] > low && a[i] < high){
            k++;
         }
      }
      int [] b = new int[k];
      int j = 0;
      for(int i = 0; i < a.length; i++){
         if(a[i] > low && a[i] < high){
            b[j] = a[i];
            j++;
         }
      }
      return b;
   }


   /**
    * Returns the smallest value in a that is greater than or equal to
    * the given key. This method throws an IllegalArgumentException if
    * a is null or has zero length, or if there is no qualifying
    * value. Note that key does not have to be an actual value in a.
    * The array a is not changed by this method.
    */
   public static int ceiling(int[] a, int key)throws IllegalArgumentException {
      if(a.length == 0 || a == null){
         throw new IllegalArgumentException();
      }
      int start = a[0];
      for (int var : a) {
         if(var < start && var >= key){
            start = var;
         }
      }
      if(start < key){
         throw new IllegalArgumentException();
      }
      return start;
   }


   /**
    * Returns the largest value in a that is less than or equal to
    * the given key. This method throws an IllegalArgumentException if
    * a is null or has zero length, or if there is no qualifying
    * value. Note that key does not have to be an actual value in a.
    * The array a is not changed by this method.
    */
   public static int floor(int[] a, int key)throws IllegalArgumentException {
      if(a.length == 0 || a == null){
         throw new IllegalArgumentException();
      }
      int floor = a[0];
      for (int var1 : a) {
         if(var1 > floor && var1 <= key){
            floor = var1;
         }
      }
      if(floor > key){
         throw new IllegalArgumentException();
      }
      return floor;
   }

   public static void main(String[] args){
      int[] arr = {32, 51, 11, 0, 10, 32, -5};
      int minimum = min(arr);
      int max = max(arr);
      int kMin = kmin(arr, 7);
      int kMax = kmax(arr, 3);
      int k = 4;
      int l = 45;
      int[] range1 = range(arr, k, l);
      System.out.println("Minimum: " + minimum);
      System.out.println("Maximum: " + max);
      System.out.println("K'th minimum value: " + kMin);
      System.out.println("K'th maximum value: " + kMax);
      System.out.print("Range of numbers between " + k + " and " + l + ":");
      for (int a : range1) {
         System.out.print(a + " ");
      }
   }
}
