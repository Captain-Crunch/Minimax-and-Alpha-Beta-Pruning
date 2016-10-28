import java.util.*;
import java.io.*;

public class MinimaxComparator implements Comparator<MinimaxObject>
{
	@Override
	public int compare(MinimaxObject obj1, MinimaxObject obj2)
	{
		int retVal = 0;
		if(obj1.score > obj2.score)
		{
			retVal = 1;
		}
		else if(obj1.score == obj2.score)
		{
			retVal = 0;
		}
		else if(obj1.score < obj2.score)
		{
			retVal = -1;
		}
		return retVal;
	}
}