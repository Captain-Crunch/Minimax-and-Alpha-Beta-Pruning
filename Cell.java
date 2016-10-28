public class Cell
{
	int x;
	int y;
	Point p;
	int val;
	String player;
	
	public Cell(Cell c1)
	{
		this.x = c1.x;
		this.y = c1.y;
		this.p = new Point(c1.p);
		this.val = c1.val;
		this.player = c1.player;
	}
	
	public Cell(Point p, int val)
	{
		this.x = p.getXCoordinate();
		this.y = p.getYCoordinate();
		this.p = p;
		this.val = val;
		this.player = null;
	}
	
	@Override
	public String toString()
	{
		return p + " val: " + val + " player: " + player;
	}
	
	public boolean equals(Cell cell)
	{
		return this.p.equals(cell.p);
	}
}