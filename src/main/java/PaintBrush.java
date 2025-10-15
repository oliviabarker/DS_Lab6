import java.util.Stack;
import javafx.scene.paint.Color;


public class PaintBrush
{

	
	Paint paint;
	
	
	enum BrushMode{
		paintMode,
		fillMode,
		pattern1Mode,
		pattern2Mode
	}

	BrushMode mode;
	
	Paint Gold = new PaintColor(Color.GOLD);
	Paint White = new PaintColor(Color.WHITE);
	

/**
set the "paint" for the paintbrush
*/	
	public void setPaint(Paint paint)
	{
		this.paint = paint;
   
	}


/*
   gets the present paint on the paint brush
*/
	public Paint getPaint()
	{
		return this.paint;
	}
	
   
   /*
   makes the paint on the paint brush a brigter shade.
   */
	public void setBrighter()
	{		
		Paint brighter = new PaintBrighter(this.paint);
		this.paint = new PaintColor(brighter.getColor());
	}


   /*
      makes the paint on the paintbrush a darker shade
   */
	public void setDarker()
	{
		Paint darker = new PaintDarker(this.paint);
		this.paint = new PaintColor(darker.getColor());
	}


   /*
      paints the mesh, using the current paint and mode at point x,y
   */
	public void paint(int x, int y, Paint[][] mesh)
	{
		if (mode==BrushMode.paintMode)
		{
			mesh[x][y]=this.getPaint();
		}
		else if (mode==BrushMode.fillMode)
		{
			fill(x,y, mesh, mesh[x][y]);
		}
		else if (mode==BrushMode.pattern1Mode)
		{
			boolean bool = true;
			pattern1(x,y, mesh, mesh[x][y], bool);
		}
		else if (mode==BrushMode.pattern2Mode)
		{
			pattern2(x,y, mesh, mesh[x][y]);
		}
	}
	
	
	public void pattern2(int x, int y, Paint[][] mesh, Paint current)
	{
		Stack<Integer> stackx = new Stack<Integer>();
		Stack<Integer> stacky = new Stack<Integer>();
		stackx.push(x);
		stacky.push(y);
		while (!stackx.isEmpty())
		{
			int tempx = stackx.pop();
			int tempy = stacky.pop();
			if (tempy%2==0) 
			{
				mesh[tempx][tempy]=this.paint;
			}
			else
			{
				mesh[tempx][tempy]=this.White;
			}
			if (mesh[tempx+1][tempy].equals(current))
			{
				stackx.push(tempx+1);
				stacky.push(tempy);
			}
			else if (mesh[tempx-1][tempy].equals(current))
			{
				stackx.push(tempx-1);
				stacky.push(tempy);
			}
			else if (mesh[tempx][tempy+1].equals(current))
			{
				stackx.push(tempx);
				stacky.push(tempy+1);
			}
			else if (mesh[tempx][tempy-1].equals(current))
			{
				stackx.push(tempx);
				stacky.push(tempy-1);
			}
		}		
	}
	

	
	
	public void pattern1(int x, int y, Paint[][] mesh, Paint current, boolean bool)
	{
		if (!mesh[x][y].equals(current) || mesh[x][y].equals(this.paint))
		{
			return;
		}
		else
		{
			if (bool)
			{ 
				mesh[x][y]=this.paint;
			}
			pattern1(x+1,y, mesh, current, !bool);			
			pattern1(x-1,y, mesh, current, !bool);			
			pattern1(x,y+1, mesh, current, !bool);			
			pattern1(x,y-1, mesh, current, !bool);			
		}
	}
	
	
	
	
	
	
	
	
	
	
//make sure check for in domain later	
	public void fill(int x, int y, Paint[][] mesh, Paint current)
	{
		if (!mesh[x][y].equals(current) || mesh[x][y].equals(this.paint))
		{
			return;
		}
		else
		{
			mesh[x][y]=this.paint;
			fill(x+1,y,mesh, current);
			fill(x-1,y,mesh, current);
			fill(x,y+1,mesh, current);
			fill(x,y-1,mesh, current);
		}
	}
	
	
/*
   set the drawing mode of the paint brush.
*/
	public void pointMode()
	{
		mode= BrushMode.paintMode;
	}

	public void fillMode()
	{
		mode = BrushMode.fillMode;
	}

	public void pattern1Mode()
	{
		mode = BrushMode.pattern1Mode;
	}

	public void pattern2Mode()
	{
		mode = BrushMode.pattern2Mode;
	}

}
