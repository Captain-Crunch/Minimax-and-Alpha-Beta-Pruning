public class MinimaxObject
{
	int score;
	Point loc;
	boolean isRaid;
	
	public MinimaxObject(int score, Point p, boolean isRaid)
	{
		this.score = score;
		this.loc = p;
		this.isRaid = isRaid;
	}
	
	public MinimaxObject(MinimaxObject o)
	{
		this.score = o.score;
		this.loc = o.loc;
		this.isRaid = o.isRaid;
	}
	
	@Override
	public String toString()
	{
		return "Score: " + score + " Loc: " + loc + " isRaid: " + isRaid;
	}
	
	
}