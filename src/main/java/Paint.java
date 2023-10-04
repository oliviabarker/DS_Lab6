import javafx.scene.paint.Color;


abstract public class Paint
{

   //returns the color of the paint	
	public abstract Color getColor();	
	
   
   //determines if this paint is equal to that paint.
	boolean equals(Paint p)
	{
		return getColor().toString()
					.equals(p.getColor().toString());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getColor().toString();
	}
	
	
	
}
