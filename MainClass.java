import java.util.*;
import java.io.*;

public class MainClass
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
		MainClass obj = new MainClass();
		
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
		
		List<String> lines = obj.getInput(fileName);
		
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
		obj.writeOutput(output, ofileName);
	}
}