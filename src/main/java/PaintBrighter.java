import javafx.scene.paint.Color;


public class PaintBrighter extends Paint
{

	Paint inner;
	
	
	public PaintBrighter(Paint inner)
	{		
		this.inner = inner;		
	}
	
	
	@Override
	public Color getColor()
	{
		return this.inner.getColor().brighter();
	}

}
