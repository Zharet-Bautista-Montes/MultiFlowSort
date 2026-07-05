import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class FlowSort 
{
	//{'E', 'A', 'O', 'S', 'R', 'N', 'I', 'D', 'L', 'C', 'T', 'U', 'M', 'P', 'B', 'G', 'V', 'Y', 'Q', 'H', 'F', 'Z', 'J', 'X', 'W', 'K', '0', '5', '4', '2', '9', '8', '6', '7', '3', '1' }
	//{'C', 'A', 'P', 'M', 'E', 'T', 'B', 'S', 'R', 'D', 'L', 'V', 'G', 'F', 'I', 'H', 'O', 'N', 'J', 'Z', 'Q', 'U', 'Y', 'K', 'W', 'X', '9', '4', '1', '3', '0', '5', '7', '2', '6', '8' }
	//{'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }
	//{'Z', '0', 'Y', '1', 'X', '2', 'W', '3', 'V', '4', 'U', '5', 'T', '6', 'S', '7', 'R', '8', 'Q', '9', 'P', 'A', 'O', 'B', 'N', 'C', 'M', 'D', 'L', 'E', 'K', 'F', 'J', 'G', 'I', 'H' } Nightmare case
	public static char[] vector = { 'Z', 'Y', 'X', 'W', 'V', 'U', 'T', 'S', 'R', 'Q', 'P', 'O', 'N', 'M', 'L', 'K', 'J', 'I', 'H', 'G', 'F', 'E', 'D', 'C', 'B', 'A', '9', '8', '7', '6', '5', '4', '3', '2', '1', '0' }; 
	public static int[] testarray;
	public static int streams;
	public static int comparisons;
	public static int swaps;
	private static Scanner modesetter;

	public static void main(String[] args) 
	{
		FlowSort mfs = new FlowSort();
		modesetter = new Scanner(System.in);
		boolean running = true;
		while(running)
		{
			System.out.println("Choose Mode:\n -C for detailed test with chars\n "
					+ "-I for massive tests with integers\n -E to exit the program ");
			String mode = modesetter.next();
			if(mode.equals("C"))
			{
				charMode(mfs);
			}
			else if(mode.equals("I"))
			{
				integerMode(mfs);
			}
			else if(mode.equals("E"))
			{
				running = false;
			}
			else System.out.println("Not valid option! \n");
		}
	}
	
	public static void charMode(FlowSort mfs)
	{
		streams = 0; comparisons = 0; swaps = 0;
		testarray = new String(vector).chars().toArray();
		printArray(testarray, "(Unsorted input)", true);
		long start = System.nanoTime();
		mfs.flowSort(testarray, true);
		long end = System.nanoTime();
		if(mfs.isSorted(testarray.length)) printArray(testarray, "(Sorted output)", true);
		System.out.println("Comparisons performed: " + comparisons);
		System.out.println("Swaps performed: " + swaps);
		System.out.println("Needed streams: " + streams);
		System.out.println("Total execution time: " + (end - start) + " ns");
		System.out.println("----------------------------------------------");
	}
	
	public static void integerMode(FlowSort mfs)
	{
		ArrayList<Integer> comparlist = new ArrayList<Integer>();
		ArrayList<Integer> swaplist = new ArrayList<Integer>();
		ArrayList<Integer> timelist = new ArrayList<Integer>();
		System.out.println("Length\tCompars\tSwaps\tSorted\tTime");
		for(int x = 8; x <= 20000; x *= 2)
		{
			testarray = IntStream.generate(() -> new Random().nextInt(100)).limit(x).toArray();
			int[] refarray = testarray.clone();
			streams = 0; comparisons = 0; swaps = 0;
			long start = System.nanoTime();
			mfs.flowSort(testarray, false);
			long end = System.nanoTime();
			//Clean and check results
			boolean validator = mfs.isSorted(testarray.length-1);
			comparisons -= 2*(testarray.length-1);
			String sorted = validator == true ? "Yes" : "No";
			comparlist.add(comparisons);
			swaplist.add(swaps);
			timelist.add((int) (end - start));
			System.out.println(x + "\t" + comparisons + "\t " + swaps 
					+ "\t" + sorted + " \t"  + (end - start) + " ns");
			if (!validator) printArray(refarray, "WARNING!", false);
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
	
	public void flowSort(int[] arrayed, boolean verbose)
	{
		int aux, top = 0;
		int btm = arrayed.length-1;
		if(top == btm) return; //Ignore 1-element subarrays
		else if(btm-top == 1 && less(btm, top)) exch(top, btm); //Handle 2-element subarrays
		else 
		{
			boolean oslever;
			while(true)
			{
				//Overlap every parallel fluxes found upstream
				aux = top; oslever = false; int a1 = top;
				for(int a2 = top; a2 < btm; a2++)
				{
					if(less(a2+1, a2) || a2+1 == btm)
					{
						if(oslever)
						{
							oslever = false; aux = a2+1;
							aux = overlapParallelFlows(a2, 1, a1); streams++; 
							if (top == aux) return;
						}
						else
						{	a1 = aux; oslever = true;	}
					}
				}
				if (verbose) printArray(arrayed, "[" + top + "-" + btm + "] " + "(Overlap upstream)", true); 
				streams++; if (top == aux) return;
				
				//Overlap every parallel fluxes found downstream
				aux = btm; oslever = false; int z1 = btm;
				for(int z2 = btm; z2 > top; z2--)
				{
					if(less(z2, z2-1) || z2-1 == top)
					{
						if(oslever)
						{
							oslever = false; aux = z2-1;
							aux = overlapParallelFlows(z2, -1, z1); streams++; 
							if (btm == aux) return;
						}
						else
						{	z1 = aux; oslever = true;	}
					}
				}
				if (verbose) printArray(arrayed, "[" + top + "-" + btm + "] " + "(Overlap downstream)", true); 
				streams++; if (btm == aux) return;
			}
		}
	}

	public void recursiveFlowSort(int[] arrayed, boolean verbose, int top, int btm) 
	{
		int aux = top;
		int half = (int) (btm-top)/2;
		if(half >= 1)
		{
			//Now perform recursion to create parallel flows
			recursiveFlowSort(arrayed, verbose, top, top+half);
			recursiveFlowSort(arrayed, verbose, btm-half, btm);
			while(true)
			{
				//Overlap every parallel fluxes found upstream
				aux = overlapParallelFlows(btm, 1, top); streams++; 
				if (verbose) printArray(arrayed, "[" + top + "-" + btm + "] " + "(Overlap upstream)", true); 
				if (top == aux || btm-top <= 1) return;
				
				//Reverse every contrary flux downstream
				aux = reverseContraryFlows(top, -1, btm); top = aux; streams++; 
				if (verbose) printArray(arrayed, "[" + top + "-" + btm + "] " + "(Reverse downstream)", true);
				if (btm == aux || btm-top <= 1) return;
	
				//Overlap every parallel fluxes found downstream
				aux = overlapParallelFlows(top, -1, btm); streams++; 
				if (verbose) printArray(arrayed, "[" + top + "-" + btm + "] " + "(Overlap downstream)", true); 
				if (btm == aux || btm-top <= 1) return;
				
				//Reverse every contrary flux found upstream
				aux = reverseContraryFlows(btm, 1, top); btm = aux; streams++; 
				if (verbose) printArray(arrayed, "[" + top + "-" + btm + "] " + "(Reverse upstream)", true);
				if (top == aux || btm-top <= 1) return;
			} 
		}
		else if(less(btm, top)) exch(top, btm);
		else return;
	}
	
	private int reverseContraryFlows(int limit, int dir, int aux)
	{
		boolean endRCF, startRCF; 
		int end, start, i = aux, tht = -1;
		while(dir == 1 ? i <= limit : i >= limit)
		{
			endRCF = true; startRCF = false;
			if(dir == 1)
			{ 
				end = i; start = tht; 
				if(i < limit)
				{ endRCF = less(i, i+1); startRCF = less(i+1, i); }
			}
			else
			{ 
				end = tht; start = i; 
				if(i > limit)
				{ endRCF = less(i-1, i); startRCF = less(i, i-1); }
			}
			if(tht != -1) //Marked
			{
				if(i == limit || endRCF)
				{
					if(end-start == 1) exch(end, start); 
					else while (start < end) exch(start++, end--);   
					aux = i; tht = -1; 
					if(i == limit) i += dir;
				}
				else i += dir;
			}
			else
			{ if(i != limit && startRCF) { tht = i; } i += dir; }
		}
		return aux;
	}
	
	private int overlapParallelFlows(int limit, int dir, int aux)
	{
		boolean endOPF, startOPF; 
		int end, start, u = aux, tht = -1;
		while(dir == 1 ? u < limit : u > limit)
		{
			endOPF = true; startOPF = false;
			if(dir == 1)
			{ 
				end = u; start = tht; startOPF = less(u+1, u);
				if(tht != -1 && tht != aux) { endOPF = !less(u+1, tht-1); }
			}
			else
			{ 
				end = tht; start = u; startOPF = less(u, u-1);
				if(tht != -1 && tht != aux) { endOPF = !less(tht+1, u-1); }
			}
			if(tht != -1) //Marked
			{
				if(tht == aux || endOPF || u == limit || startOPF)
				{
					int m = (end - start + 1) / 2;
					for(int n = 0; n < m; n++)
						exch(start+n, end-m+n+1); 
					aux = u; tht = -1;
				}
				else tht -= dir;
			}
			else if(startOPF) tht = u; 
			u += dir; 
		}
		if(tht != -1)
		{
			if(dir == 1 && less(u, u-1)) exch(u, u-1);
			else if(dir == -1 && less(u+1, u)) exch(u+1, u);
			aux = u;
		}
		return aux;
	}

	private boolean less(int w, int y)
	{
		comparisons++;
		return testarray[w] < testarray[y]; 
	}

	private void exch(int a, int z)
	{
		int temp = testarray[a]; 
		testarray[a] = testarray[z]; 
		testarray[z] = temp; 
		swaps++;
	}
	
	private boolean isSorted(int arraylength)
	{
		for(int n=0; n<arraylength-1; n++)
			if(!less(n, n+1) && less(n+1, n))
				return false;
		return true;
	}

	private static void printArray(int[] arrayed, String msg, boolean cryptic)
	{
		for(int k=0; k<arrayed.length; k++)
		{
			String token = cryptic ? ((char) arrayed[k] + "") : (arrayed[k] + "");
			System.out.print(token + " ");
		}
		System.out.println(" " + msg);
	}
}