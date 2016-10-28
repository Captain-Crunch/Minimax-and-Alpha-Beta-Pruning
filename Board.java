import java.util.*;
import java.io.*;

public class Board
{
	Cell[][] board;
	int size;
	List<Point> open;
	HashMap<String, Integer> scores = new HashMap<String, Integer>();
	String turn;
	
	public Board(Board b)
	{
		this.size = b.size;
		this.board = new Cell[size][size];
		this.open = new ArrayList<Point>();
		for(int x = 0; x < size; ++x)
		{
			for(int y = 0; y < size; ++y)
			{
				this.board[x][y] = new Cell(b.board[x][y]);
			}
		}
		
		for(Point p: b.open)
		{
			this.open.add(new Point(p));
		}
		
		for (String key : b.scores.keySet())
		{
			this.scores.put(key, b.scores.get(key));
		}
		
		this.turn = b.turn;
	}
	
	public Board(List<String> lines)
	{
		size = Integer.parseInt(lines.get(0));
		String play = lines.get(2);
		
		board = new Cell[size][size];
		open = new ArrayList<Point>();
		
		scores.put("green", 0);
		scores.put("blue", 0);
		
		if("X".equals(play))
		{
			turn = "blue";
		}
		else if("O".equals(play))
		{
			turn = "green";
		}
		
		int y = 0;
		for(int i = 4; i < 4 + size; ++i)
		{
			String[] s = lines.get(i).split(" ");
			int x = 0;
			for(int j = 0; j < s.length; ++j)
			{
				Point p = new Point(y, x);
				board[y][x] = new Cell(p, Integer.parseInt(s[j]));
				x += 1;
			}
			y += 1;
		}
		
		y = 0;
		for(int i = 4 + size; i < 4 + (2*size); ++i)
		{
			String s = lines.get(i);
			int x = 0;
			for(int j = 0; j < s.length(); ++j)
			{
				char ch = s.charAt(j);
				if (ch == '.')
				{
					Point p = new Point(y,x);
					open.add(p);
				}
				else if(ch == 'X')
				{
					board[y][x].player = "blue";
					scores.put("blue", scores.get("blue") + board[y][x].val);
				}
				else if(ch == 'O')
				{
					board[y][x].player = "green";
					scores.put("green", scores.get("green") + board[y][x].val);
				}
				
				x += 1;
			}
			y += 1;
		}
	}
	
	public void printBoard()
	{
		for(int i = 0; i < board.length; ++i)
		{
			for(int j = 0; j < board[i].length; ++j)
			{
				System.out.println(board[i][j]);
			}
		}
	}
	
	public void printOpen()
	{
		for(int i = 0; i < open.size(); ++i)
		{
			System.out.println(open.get(i));
		}
	}
	
	public Cell getCell(Point p)
	{
		int x = p.getXCoordinate();
		int y = p.getYCoordinate();
		
		if((x >= 0 && x < size) && (y >= 0 && y < size))
		{
			return board[x][y];
		}
		
		return null;
	}
	
	public int getIndex(Point p)
	{
		int idx = 0;
		Iterator<Point> iter = open.iterator();
		while(iter.hasNext())
		{
			Point pt = iter.next();
			if(pt.equals(p))
			{
				return idx;
			}
			idx++;
		}
		return -1;
	}
	
	public void capture(Point p, String player)
	{
		int idx = getIndex(p);
		if(idx > -1)
			open.remove(idx);
		
		Cell c = getCell(p);
		
		// Capture the opponents cell! Haha!
		if(c.player != null && !c.player.equals(player))
		{
			scores.put(c.player, scores.get(c.player) - c.val);
			scores.put(player, scores.get(player) + c.val);
			
		}
		
		else if(c.player == null)
		{
			scores.put(player, scores.get(player) + c.val);
		}
		
		c.player = player;
	}
	
	public void stake(Point p, String player)
	{
		int idx = getIndex(p);
		if(idx > -1)
			open.remove(idx);
		
		Cell c = getCell(p);
		
		if(c!= null && c.player == null)
		{
			//capture(p, player);
			scores.put(player, scores.get(player) + c.val);
			c.player = player;
		}
	}
	
	public void raid(Point p, String player) throws RaidException
	{
		List<Point> directions = new ArrayList<Point>();
		directions.add(new Point(0, 1)); //North
		directions.add(new Point(0, -1)); //South
		directions.add(new Point(1, 0)); //East
		directions.add(new Point(-1, 0)); // West
		
		boolean adj_team = false;
		boolean isRaid = false;
		
		Cell c = getCell(p);
		if(c != null && c.player == null)
		{
			for(Point dir: directions)
			{
				Cell adj_cell = getCell(new Point(c.p.getXCoordinate() + dir.getXCoordinate(), c.p.getYCoordinate() + dir.getYCoordinate()));
				if(adj_cell != null && adj_cell.player != null && adj_cell.player.equals(player))
				{
					adj_team = true;
				}
			}
			if(!adj_team)
			{
				throw new RaidException();
			}
			
			capture(c.p, player);
			for(Point dir: directions)
			{
				Cell adj_cell = getCell(new Point(c.p.getXCoordinate() + dir.getXCoordinate(), c.p.getYCoordinate() + dir.getYCoordinate()));
				if(adj_cell != null && adj_cell.player != null && !adj_cell.player.equals(player))
				{
					isRaid = true;
					capture(adj_cell.p, player);
				}
			}
			if(!isRaid)
			{
				throw new RaidException();
			}
		}
	}
	
	public void nextTurn()
	{
		if(this.turn.equals("green"))
		{
			this.turn = "blue";
		}
		else
		{
			this.turn = "green";
		}
	}
	
	public void print()
	{
		for(int i = 0; i < size; ++i)
		{
			String s = "";
			for(int j = 0; j < size; ++j)
			{
				if(board[i][j].player == null)
				{
					s = s + ".";
				}
				else if(board[i][j].player.equals("blue"))
				{
					s = s + "X";
				}
				else if(board[i][j].player.equals("green"))
				{
					s = s + "O";
				}
			}
			System.out.println(s);
		}
	}
	
	public void fillOutput(List<String> op)
	{
		for(int i = 0; i < size; ++i)
		{
			String s = "";
			for(int j = 0; j < size; ++j)
			{
				if(board[i][j].player == null)
				{
					s = s + ".";
				}
				else if(board[i][j].player.equals("blue"))
				{
					s = s + "X";
				}
				else if(board[i][j].player.equals("green"))
				{
					s = s + "O";
				}
			}
			//System.out.println(s);
			op.add(s);
		}
	}
}

class RaidException extends Exception
{
	public RaidException() 
	{
	}
	public RaidException(String message)
	{
		super(message);
	}
}