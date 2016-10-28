public class Point
{
	int x;
	int y;
	
	public Point(Point p1)
	{
		this.x = p1.getXCoordinate();
		this.y = p1.getYCoordinate();
	}
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getXCoordinate()
	{
		return this.x;
	}
	
	public int getYCoordinate()
	{
		return this.y;
	}
	
	@Override
	public String toString()
	{
		return "X: " + x + " Y: " +y;
	}
	
	public boolean equals(Point point)
	{
		return ((this.getXCoordinate() == point.getXCoordinate()) && (this.getYCoordinate() == point.getYCoordinate())); 
	}
}