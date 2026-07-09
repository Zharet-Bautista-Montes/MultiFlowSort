public class DirectFlowSort extends BaseSort
{
	protected void performSort()
	{
		directFlowSort(0, testarray.length-1, true); 
	}
	
	public void directFlowSort(int top, int btm, boolean upstream) 
	{
		if(top == btm) return; //Ignore 1-element subarrays
		else if(btm-top == 1 && less(btm, top)) exch(top, btm); //Handle 2-element subarrays
		else 
		{
			int aux = top;
			int half = (int) (btm-top)/2;
			int lhalf = top+half, rhalf = btm-half;
			if(lhalf == rhalf) lhalf--;
			//Control ascending or descending flows to optimize recursion
			if(upstream)
			{
				//Reverse every contrary flux found upstream
				aux = reverseContraryFlows(btm, 1, top); 
				if (verbose) printArray("[" + top + "-" + btm + "] " + "(Reverse upstream)");
				btm = aux - 1; streams++; if (top == aux) return;
			}
			else
			{
				//Reverse every contrary flux downstream
				aux = reverseContraryFlows(top, -1, btm); 
				if (verbose) printArray("[" + top + "-" + btm + "] " + "(Reverse downstream)");
				top = aux + 1; streams++; if (btm == aux) return;
			}
			//Now perform recursion to handle parallel flows
			directFlowSort(top, lhalf, !upstream);
			directFlowSort(rhalf, btm, !upstream);
			//Perform overlap only if the right half is lower than the left half
			if(less(rhalf, lhalf))
			{
				aux = overlapParallelFlows(btm, 1, lhalf); 
				if (verbose) printArray("[" + top + "-" + btm + "] " + "(Overlap upstream)"); 
				btm = aux - 1; streams++; if (top == aux) return;
				while(true)
				{
					//Overlap every parallel fluxes found upstream
					aux = overlapParallelFlows(btm, 1, top); 
					if (verbose) printArray("[" + top + "-" + btm + "] " + "(Overlap upstream)"); 
					btm = aux - 1; streams++; if (top == aux) return;
		
					//Overlap every parallel fluxes found downstream
					aux = overlapParallelFlows(top, -1, btm); 
					if (verbose) printArray("[" + top + "-" + btm + "] " + "(Overlap downstream)"); 
					top = aux + 1; streams++; if (btm == aux) return;
				} 
			}
		}
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
					if(endRCF) i -= dir;
				}
			}
			else if(startRCF) tht = i;
			i += dir;
		}
		return aux;
	}
	
	private int overlapParallelFlows(int limit, int dir, int aux)
	{
		boolean endOPF, startOPF; 
		int end, start, u = aux, tht = -1;
		while(dir == 1 ? u <= limit : u >= limit)
		{
			endOPF = true; startOPF = false;
			if(dir == 1)
			{ 
				end = u; start = tht; 
				if(u < limit)
				{
					startOPF = less(u+1, u);
					if(tht != -1 && tht != aux)
						endOPF = !less(u+1, tht-1);
				}
			}
			else
			{ 
				end = tht; start = u; 
				if(u > limit)
				{
					startOPF = less(u, u-1);
					if(tht != -1 && tht != aux) 
						endOPF = !less(tht+1, u-1);
				}
			}
			if(tht != -1) //Marked
			{
				if(tht == aux + dir || endOPF || u == limit - dir || startOPF)
				{
					int m = (end - start + 1) / 2;
					for(int n = 0; n < m; n++)
						exch(start+n, end-m+n+1); 
					aux = u; tht = -1; u -= dir;
				}
				else tht -= dir;
			}
			else if(startOPF) tht = u; 
			u += dir; 
		}
		return aux;
	}
}