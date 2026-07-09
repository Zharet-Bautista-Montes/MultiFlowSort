import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class TestManager 
{
	//{'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }
	//{'C', 'A', 'P', 'M', 'E', 'T', 'B', 'S', 'R', 'D', 'L', 'V', 'G', 'F', 'I', 'H', 'O', 'N', 'J', 'Z', 'Q', 'U', 'Y', 'K', 'W', 'X', '9', '4', '1', '3', '0', '5', '7', '2', '6', '8' }
	//{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }
	//{'Z', '0', 'Y', '1', 'X', '2', 'W', '3', 'V', '4', 'U', '5', 'T', '6', 'S', '7', 'R', '8', 'Q', '9', 'P', 'A', 'O', 'B', 'N', 'C', 'M', 'D', 'L', 'E', 'K', 'F', 'J', 'G', 'I', 'H' } Nightmare case
	public static char[] vector = { 'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }; 
	public static int[] testarray;
	private static Scanner modesetter;
	private static BaseSort testSort;

	public static void main(String[] args) 
	{
		modesetter = new Scanner(System.in);
		boolean running = true;
		while(running)
		{
			System.out.println("Choose Mode:\n -C for detailed test with chars\n "
					+ "-I for massive tests with integers\n -E to exit the program ");
			String mode = modesetter.next();
			if(mode.equals("C"))
			{
				testSort = selectSort();
				if (testSort != null) 
				{
					testSort.verbose = true;
					charMode(testSort);
				}
			}
			else if(mode.equals("I"))
			{
				testSort = selectSort();
				if (testSort != null) 
				{
					testSort.verbose = false;
					integerMode(testSort);
				}
			}
			else if(mode.equals("E"))
			{
				running = false;
			}
			else System.out.println("Not valid option! \n");
		}
	}
	
	public static BaseSort selectSort() 
	{
		System.out.println("Select Sorting Algorithm:\n 1. MultiFlowSort \n "
				+ "2. HalvingSort \n 3. DirectFlowSort ");
		String mode = modesetter.next();
		if(mode.equals("1"))
		{
			return new MultiFlowSort();
		}
		else if(mode.equals("2"))
		{
			return new HalvingSort();
		}
		else if(mode.equals("3"))
		{
			return new MultiFlowSort();
		}
		else 
		{
			System.out.println("Not valid option! \n");
			return null;
		}
	}
	
	public static void charMode(BaseSort fs)
	{
		testarray = new String(vector).chars().toArray();
		testSort.testarray = testarray;
		testSort.printArray("(Unsorted input)");
		long start = System.nanoTime();
		fs.performSort();
		long end = System.nanoTime();
		if(fs.isSorted()) testSort.printArray("(Sorted output)");
		System.out.println("Comparisons performed: " + testSort.comparisons);
		System.out.println("Swaps performed: " + testSort.swaps);
		System.out.println("Needed streams: " + testSort.streams);
		System.out.println("Total execution time: " + (end - start) + " ns");
		System.out.println("----------------------------------------------");
	}
	
	public static void integerMode(BaseSort bs)
	{
		ArrayList<Integer> comparlist = new ArrayList<Integer>();
		ArrayList<Integer> swaplist = new ArrayList<Integer>();
		ArrayList<Integer> timelist = new ArrayList<Integer>();
		System.out.println("Length\tCompars\tSwaps\tSorted\tTime");
		for(int x = 8; x <= 50000; x *= 2)
		{
			testarray = IntStream.generate(() -> new Random().nextInt(100)).limit(x).toArray();
			testSort.testarray = testarray;
			int[] refarray = testarray.clone();
			long start = System.nanoTime();
			bs.performSort();
			long end = System.nanoTime();
			boolean validator = bs.isSorted();
			String sorted = validator == true ? "Yes" : "No";
			comparlist.add(testSort.comparisons);
			swaplist.add(testSort.swaps);
			timelist.add((int) (end - start));
			System.out.println(x + "\t" + testSort.comparisons + "\t " + testSort.swaps 
					+ "\t" + sorted + " \t"  + (end - start) + " ns"); 
			if (!validator) testSort.printArray(refarray, "WARNING!");
		}
		double comparlog = 0.0, swaplog = 0.0, timelog = 0.0;
		for(int r = 1; r < timelist.size(); r++)
		{
			comparlog += (Math.log10(comparlist.get(r)) - Math.log10(comparlist.get(r-1)))/Math.log10(2);
			swaplog += (Math.log10(swaplist.get(r)) - Math.log10(swaplist.get(r-1)))/Math.log10(2);
			timelog += (Math.log10(timelist.get(r)) - Math.log10(timelist.get(r-1)))/Math.log10(2);
		}
		System.out.println((comparlog/(timelist.size()-1)) + " Logarithmic comparisons");
		System.out.println((swaplog/(timelist.size()-1)) + " Logarithmic swaps");
		System.out.println((timelog/(timelist.size()-1)) + " Logarithmic time");
	}
}