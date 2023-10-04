// Demonstrate how to remove recursion
import java.util.Stack;

public class RemoveRecursion
{
   private static class Record
   {
      public int n; // value of n parameter
      public int pos = 0; // where in the code am I?
      public int ret = 0; // also have to store return value
      
      public Record(int n)
      {
         this.n = n; // pos and ret default to 0
      }
   }

   // Rewrite fib() to eliminate recursion
   public static int fib2(int n)
   {
      Stack<Record> s = new Stack<Record>(); // simulate system stack
      
      s.push(new Record(n)); // push parameter n on stack
      int ret = 0; // return value
      while (! s.isEmpty()) // while stack not empty
      {
         Record r = s.pop(); // pop record from stack
         if (r.pos == 0) // not returning from anything: at start of code
         {
            if (r.n <= 1)
            {
               ret = r.n; // Asa's method
               continue; // sorry, Dr. Toth
            }
            r.pos = 2; // simulate recursive call
            s.push(r); // store parameter, etc. on stack
            s.push(new Record(r.n - 1));
         }
         else if (r.pos == 2) // returning from fib(n-1) just before "+"
         {
            r.ret = ret;
            r.pos = 3;
            s.push(r);
            s.push(new Record(r.n - 2));
         }
         else // returning from fib(n - 2) just before ";"
         {
            ret = r.ret + ret; // compute the sum
         }
      }
      return ret; // we are finished!
   }
   
   public static int fib(int n)
   {
      if (n <= 0)
         return 0;
      else if (n == 1)
         return 1;
      else
         return fib(n - 1) + fib(n - 2);
   }
   
   public static void main(String[] args)
   {
      for (int i=1; i<=10; i++)
      {
         System.out.println("fib(" + i + ") = " + fib2(i)); // i'th Fibonacci number
      }
      
      // still takes a long time since no memoization :(
      System.out.println();
      System.out.println("fib(30) = " + fib2(30)); 
   }

}