public abstract class BaseSort 
{
	public int[] testarray;
	protected int comparisons;
	protected int swaps;
	protected int streams;
	protected String name;
	public boolean verbose;
	
	protected void buildSort(){}
	
	protected void performSort(){}
	
	protected boolean less(int w, int y)
	{
		comparisons++;
		return testarray[w] < testarray[y]; 
	}

	protected void exch(int a, int z)
	{
		int temp = testarray[a]; 
		testarray[a] = testarray[z]; 
		testarray[z] = temp; 
		swaps++;
	}
	
	protected boolean isSorted()
	{
		int arraylength = testarray.length;
		for(int n=0; n<arraylength-1; n++)
			if(!less(n, n+1) && less(n+1, n))
				return false;
		return true;
	}

	protected void printArray(String msg)
	{
		int arraylength = testarray.length;
		for(int k=0; k<arraylength; k++)
		{
			String token = verbose ? ((char) testarray[k] + "") : (testarray[k] + "");
			System.out.print(token + " ");
		}
		System.out.println(" " + msg);
	}
	
	protected void printArray(int[] arrayed, String msg)
	{
		for(int k=0; k<arrayed.length; k++)
		{
			String token = verbose ? ((char) arrayed[k] + "") : (arrayed[k] + "");
			System.out.print(token + " ");
		}
		System.out.println(" " + msg);
	}
}