import java.util.*;
import java.io.*;

public class AlphaBeta
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