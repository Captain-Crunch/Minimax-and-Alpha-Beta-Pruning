import java.util.*;
import java.io.*;

class AlphaBeta
{
	int max_depth;
	String max_player;
	String adv_player;
	boolean isRaid = false;
	
	public AlphaBeta(int max_depth, String player)
	{
		this.max_depth = max_depth;
		if("X".equals(player))
		{
			this.max_player = "blue";
			this.adv_player = "green";
		}
		else if("O".equals(player))
		{
			this.max_player = "green";
			this.adv_player = "blue";
		}	
	}
	
	public MinimaxObject calculateAlphaBeta(String curPlayer, String adversary, Board board, int depth, Point p, MinimaxObject alpha, MinimaxObject beta, Boolean isStake)
	{
		Board b = new Board(board);
		Cell cell =null;
		boolean canRaid = false;
		
		if(depth == 0)
		{
			List<MinimaxObject> l = new ArrayList<MinimaxObject>();
			for(Point nextPoint: b.open)
			{
				MinimaxObject temp = calculateAlphaBeta(curPlayer, adversary, b, depth + 1, nextPoint, alpha, beta, true);
				if(temp != null)
				{
					temp.loc = nextPoint;
					temp.isRaid = false;
					l.add(temp);
				}
				
				MinimaxObject temp1 = calculateAlphaBeta(curPlayer, adversary, b, depth + 1, nextPoint, alpha, beta, false);
				if(temp1 != null)
				{
					temp1.loc = nextPoint;
					temp1.isRaid = true;
					l.add(temp1);
				}
			}
			if(curPlayer.equals(max_player))
			{
				MinimaxObject max = Collections.max(l, new MinimaxComparator());
				for(MinimaxObject obj: l)
				{
					if(obj.score == max.score)
					{
						if(!obj.isRaid)
						{
							return obj;
						}
					}
					
				}
				return max;
			}
			else
			{
				return Collections.min(l, new MinimaxComparator());
			}
		}
		
		if(isStake)
		{
			cell = b.getCell(p);
			b.stake(p, curPlayer);
		}
		else
		{
			cell = b.getCell(p);
			b.capture(p, curPlayer);
				
			List<Cell> neighbours = new ArrayList<Cell>();
			neighbours.add(b.getCell(new Point(p.getXCoordinate()+1, p.getYCoordinate())));
			neighbours.add(b.getCell(new Point(p.getXCoordinate()-1, p.getYCoordinate())));
			neighbours.add(b.getCell(new Point(p.getXCoordinate(), p.getYCoordinate()+1)));
			neighbours.add(b.getCell(new Point(p.getXCoordinate(), p.getYCoordinate()-1)));
			
			for(Cell neighbour: neighbours)
			{
				if(neighbour != null && neighbour.player!= null && neighbour.player.equals(curPlayer))
				{
					for(Cell advCell: neighbours)
					{
						if(advCell != null && advCell.player!= null && advCell.player.equals(adversary))
						{
							if(curPlayer.equals(max_player))
							{
								b.scores.put(max_player, b.scores.get(max_player) + advCell.val);
								b.scores.put(adv_player, b.scores.get(adv_player) - advCell.val);
							}
							else
							{
								b.scores.put(max_player, b.scores.get(max_player) - advCell.val);
								b.scores.put(adv_player, b.scores.get(adv_player) + advCell.val);
							}
							advCell.player = curPlayer;
							canRaid = true;
						}
					}
					break;
				}
			}
			if(!canRaid) //This has been already explored
			{
				return null;
			}
		}
		
		if(depth >= max_depth || b.open.size() == 0)
		{
			if(cell.p != null)
			{
				return new MinimaxObject(b.scores.get(max_player) - b.scores.get(adv_player), null, isStake);
			}
			else
			{
				return null;
			}
		}
		else
		{
			List<MinimaxObject> l = new ArrayList<MinimaxObject>();
			
			if(!curPlayer.equals(max_player))
			{
				for(Point nextPoint: b.open)
				{
					l.clear();
					l.add(alpha);
					
					MinimaxObject temp = calculateAlphaBeta(adversary, curPlayer, b, depth + 1, nextPoint, alpha, beta, true);
					if(temp != null)
					{
						temp.isRaid = false;
						l.add(temp);
					}
					
					
					MinimaxObject temp1 = calculateAlphaBeta(adversary, curPlayer, b, depth + 1, nextPoint, alpha, beta, false);
					if(temp1 != null)
					{
						temp1.isRaid = true;
						l.add(temp1);
					}
					
					alpha = Collections.max(l, new MinimaxComparator());
					if(beta.score <= alpha.score)
					{
						break;
					}
				}
				return alpha;
			}
			else
			{
				for(Point nextPoint: b.open)
				{
					l.clear();
					l.add(beta);
					
					MinimaxObject temp = calculateAlphaBeta(adversary, curPlayer, b, depth + 1, nextPoint, alpha, beta, true);
					if(temp != null)
					{
						temp.isRaid = false;
						l.add(temp);
					}
					
					
					MinimaxObject temp1 = calculateAlphaBeta(adversary, curPlayer, b, depth + 1, nextPoint, alpha, beta, false);
					if(temp1 != null)
					{
						temp1.isRaid = true;
						l.add(temp1);
					}
					
					beta = Collections.min(l, new MinimaxComparator());
					if(beta.score <= alpha.score)
					{
						break;
					}
				}
				return beta;
			}
		}
	}
}

class MiniMax
{
	int max_depth;
	String max_player;
	String adv_player;
	
	public MiniMax(int max_depth, String player)
	{
		this.max_depth = max_depth;
		if("X".equals(player))
		{
			this.max_player = "blue";
			this.adv_player = "green";
		}
		else if("O".equals(player))
		{
			this.max_player = "green";
			this.adv_player = "blue";
		}	
	}
	
	public MinimaxObject calculateMinimax(String curPlayer, String adversary, Board board, int depth, Point p, Boolean isStake)
	{
		Board b = new Board(board);
		Cell cell = null;
		boolean canRaid = false;
		
		if(depth == 0)
		{
			List<MinimaxObject> l = new ArrayList<MinimaxObject>();
			for(Point nextPoint: b.open)
			{
				MinimaxObject temp = calculateMinimax(curPlayer, adversary, b, depth + 1, nextPoint, true);
				if(temp != null)
				{
					temp.loc = nextPoint;
					temp.isRaid = false;
					l.add(temp);
				}
				
				MinimaxObject temp1 = calculateMinimax(curPlayer, adversary, b, depth + 1, nextPoint, false);
				if(temp1 != null)
				{
					temp1.loc = nextPoint;
					temp1.isRaid = true;
					l.add(temp1);
				}
			}
			if(curPlayer.equals(max_player))
			{
				MinimaxObject max = Collections.max(l, new MinimaxComparator());
				for(MinimaxObject obj: l)
				{
					if(obj.score == max.score)
					{
						if(!obj.isRaid)
							return obj;
					}
				}
				return max;
			}
			else
			{
				return Collections.min(l, new MinimaxComparator());
			}
		}
		
		if(isStake)
		{
			cell = b.getCell(p);
			b.stake(p, curPlayer);
		}
		else
		{
			cell = b.getCell(p);
			b.capture(p, curPlayer);
			
			List<Cell> neighbours = new ArrayList<Cell>();
			neighbours.add(b.getCell(new Point(p.getXCoordinate()+1, p.getYCoordinate())));
			neighbours.add(b.getCell(new Point(p.getXCoordinate()-1, p.getYCoordinate())));
			neighbours.add(b.getCell(new Point(p.getXCoordinate(), p.getYCoordinate()+1)));
			neighbours.add(b.getCell(new Point(p.getXCoordinate(), p.getYCoordinate()-1)));
			
			for(Cell neighbour: neighbours)
			{
				if(neighbour != null && neighbour.player!= null && neighbour.player.equals(curPlayer))
				{
					for(Cell advCell: neighbours)
					{
						if(advCell != null && advCell.player!= null && advCell.player.equals(adversary))
						{
							if(curPlayer.equals(max_player))
							{
								b.scores.put(max_player, b.scores.get(max_player) + advCell.val);
								b.scores.put(adv_player, b.scores.get(adv_player) - advCell.val);
							}
							else
							{
								b.scores.put(max_player, b.scores.get(max_player) - advCell.val);
								b.scores.put(adv_player, b.scores.get(adv_player) + advCell.val);
							}
							advCell.player = curPlayer;
							canRaid = true;
						}
					}
					break;
				}
			}
			if(!canRaid)
			{
				return null;
			}
		}
		
		if(depth >= max_depth || b.open.size() == 0)
		{
			if(cell.p != null)
			{
				return new MinimaxObject(b.scores.get(max_player) - b.scores.get(adv_player), null, isStake);
			}
			else
			{
				return null;
			}
		}
		else
		{
			List<MinimaxObject> l = new ArrayList<MinimaxObject>();
			for(Point nextPoint: b.open)
			{
				MinimaxObject temp = calculateMinimax(adversary, curPlayer, b, depth + 1, nextPoint, true);
				if(temp != null)
				{
					temp.isRaid = false;
					l.add(temp);
				}
				
				
				MinimaxObject temp1 = calculateMinimax(adversary, curPlayer, b, depth + 1, nextPoint, false);
				if(temp1 != null)
				{
					temp1.isRaid = true;
					l.add(temp1);
				}
				

			}
			if(curPlayer.equals(max_player))
			{
				return Collections.min(l, new MinimaxComparator());
			}
			else
			{
				return Collections.max(l, new MinimaxComparator());
			}
		}
	}
}

class MinimaxObject
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

class MinimaxComparator implements Comparator<MinimaxObject>
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

class Point
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
class Cell
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
 
class Board
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

public class homework
{
	//static String fileName = "input10.txt";
	
	public List<String> getInput(String fileName)
	{
		BufferedReader br = null;
		List<String> lines = new ArrayList<String>();
		
		try
		{
			br = new BufferedReader(new FileReader(fileName));
			
			String line = br.readLine(); // Read the first line!
			while(line != null)
			{
				lines.add(line.trim());
				line = br.readLine();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				br.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return lines;
	}
	
	public void writeOutput(List<String> lines, String outputFile)
	{
		Writer writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
			for(String line: lines)
			{
				writer.write(line + System.lineSeparator());
			}
		} 
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				writer.close();
			}
			catch (Exception ex) 
			{
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		homework hw = new homework();
		
		String fileName = "";
		String ofileName = "";
		
		if(args.length == 1)
		{
			fileName = args[0];
			ofileName = "output.txt";
		}
		else if(args.length == 2)
		{
			fileName = args[0];
			ofileName = args[1];
		}
		else
		{
			fileName = "input.txt";
			ofileName = "output.txt";
		}
		
		List<String> lines = hw.getInput(fileName);
		
		Board b = new Board(lines);
		
		String algo = lines.get(1);
		//System.out.println("Algo: " + algo);
		
		String player = lines.get(2);
		
		String iPlay = "";
		String oppoPlay = "";
		if("X".equals(player))
		{
			iPlay = "blue";
			oppoPlay = "green";
		}
		else if("O".equals(player))
		{
			iPlay = "green";
			oppoPlay = "blue";
		}
		
		MinimaxObject o = null;
		if(algo.equals("MINIMAX"))
		{
			MiniMax m = new MiniMax(Integer.parseInt(lines.get(3)), player);
			o = m.calculateMinimax(iPlay, oppoPlay, b, 0, null, null);
		}
		
		if(algo.equals("ALPHABETA"))
		{
			AlphaBeta ab = new AlphaBeta(Integer.parseInt(lines.get(3)), player);
			o = ab.calculateAlphaBeta(iPlay, oppoPlay, b, 0, null, new MinimaxObject(Integer.MIN_VALUE, null, false), new MinimaxObject(Integer.MAX_VALUE, null, false), null);
		}
		
		List<String> output = new ArrayList<String>();
		//System.out.println(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " " + o.score);
		if(o.isRaid)
		{
			try
			{
				b.raid(o.loc, iPlay);
				//System.out.println(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " Raid");
				output.add(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " Raid");
				b.fillOutput(output);
			}
			catch(RaidException re)
			{
				b.stake(o.loc, iPlay);
				//System.out.println(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " Stake");
				output.add(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " Stake");
				b.fillOutput(output);
			}
		}
		else
		{
			b.stake(o.loc, iPlay);
			//System.out.println(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " Stake");
			output.add(String.valueOf((char)(o.loc.getYCoordinate()+65)) + (o.loc.getXCoordinate()+1) + " Stake");
			b.fillOutput(output);
		}
		hw.writeOutput(output, ofileName);
	}
}