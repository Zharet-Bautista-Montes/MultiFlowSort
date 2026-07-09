public class HalvingSort extends BaseSort
{
	@Override
	protected void performSort()
	{
		halve(0, testarray.length-1);
	}
	
	private void halve(int l, int r)
	{
		if(l == r) return;
		else if(r-l == 1 && less(r, l)) exch(l, r);   	
		else
		{
			int half = (int) (r-l)/2;
			int m1 = l+half, m2 = r-half;
			halve(l, m1); streams++;
			halve(m2, r); streams++;
			//Reversing version
			for (int n = half; n >= 0; n--)
				if(less(r-n, l+n))
					exch(l+n, r-n);
			streams++;
			//Overlapping version 
//			for (int m = l; m <= m1 && m2+m <= r; m++)
//				if(less(m2+m, m))
//					exch(m, m2+m);
			if (verbose) printArray("(Halved from " + l + " to " + r + ")");
			halve(l, m1); streams++;
			halve(m2, r); streams++;
		}
	}
}