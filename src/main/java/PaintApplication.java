//Recursive Paint Lab

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class PaintApplication extends Application
{

	private final int width = 400;
	private final int height = 400;
	WritableImage image = new WritableImage(width,height);
	ImageView iView;
	PaintBrush brush = new PaintBrush();
	private int mult=8;
	
	
	
	@Override
   //Where the application starts after the launch
	public void start(Stage stage) throws Exception
	{
		
		brush.setPaint(new PaintColor(Color.BLACK));
		brush.pointMode();
		
		BorderPane pane = new BorderPane();
		VBox buttons = new VBox();
		buttons.setSpacing(10);

		buttons.getChildren().add(makeSettings());
		buttons.getChildren().add(makeColorButtons());
		buttons.getChildren().add(makeModeButtons());
		buttons.getChildren().add(makePresetButtons());
		pane.setTop(buttons);
		
		
		iView = new ImageView(image);
		flood(Color.WHITE);
		iView.addEventHandler(MouseEvent.MOUSE_CLICKED,
				new EventHandler<MouseEvent>()
				{

					@Override
					public void handle(MouseEvent e)
					{
						System.out.println("clicked"+e.getX()+","+e.getY());
						paint(e.getX(),e.getY());						
					}			
				}
				);
		
		
		
		updateSettings();
		pane.setCenter(iView);
		
		Scene scene = new Scene(pane,500,550);
			
		stage.setTitle("Pretty Paint");
		stage.setScene(scene);
		stage.show();
	}

	
	
	
/**
generates the color buttons bar
*/	
	private Node makeColorButtons()
	{
		HBox colors=new HBox();
		Button black=new Button("Black");
		Button white=new Button("White");
		Button red=new Button("Red");
		Button green=new Button("Green");
		Button blue=new Button("Blue");
		Button brighter = new Button("Brighter");
		Button darker = new Button("Darker");
	
		black.setOnAction(e->setPaint(new PaintColor(Color.BLACK))) ;
		white.setOnAction(e->setPaint(new PaintColor(Color.WHITE)));
		red.setOnAction(e->setPaint(new PaintColor(Color.RED)));
		green.setOnAction(e->setPaint(new PaintColor(Color.GREEN)));
		blue.setOnAction(e->setPaint(new PaintColor(Color.BLUE)));
		brighter.setOnAction(e->{brush.setBrighter(); updateSettings();});
		darker.setOnAction(e->{brush.setDarker();updateSettings();});
		
		colors.getChildren().addAll(new Label("Paint Colors"),black,white,red,green,blue,brighter,darker);
			
		return colors;
		
		
	}

	private void setPaint(Paint p)
	{
		brush.setPaint(p);
		updateSettings();
		
	}
	Label colorBox;
	Label drawMode=new Label("Point Mode");
	

//redraw settings based on changes	
	private void updateSettings()
	{
		colorBox.setBackground(
					new Background(
					new BackgroundFill(brush.getPaint().getColor(),
										new CornerRadii(30),Insets.EMPTY)
										));	
	
	}

/**
generates the settings buttons bar
*/	
	
	private Node makeSettings()
	{
		HBox settings=new HBox();
		
		Label pixelSize = new Label("Pixel Size "+mult+"   ");
		
		Slider slider = new Slider(0,4,3);
		slider.setMajorTickUnit(1.0);

		slider.setBlockIncrement(1.0);
		slider.setSnapToTicks(true);
		slider.valueProperty().addListener((a,b,c)->
		{
			mult = (int) Math.pow(2,Math.floor(slider.getValue()));
			pixelSize.setText("Pixel Size "+mult+"   ");
		});
		
		colorBox = new Label("          ");
		
		settings.setSpacing(10);
		settings.getChildren().addAll(new Label("Color"), colorBox,
				pixelSize,slider
					);
		return settings;
	}
	
   /**
   generates the mode buttons bar
   */	

	private Node makeModeButtons()
	{
		HBox colors=new HBox();
		Label label = new Label("Paint Mode");
		
		Button point=new Button("Point");
		Button fill=new Button("Fill");
		Button pattern1=new Button("Pattern 1");
		Button pattern2=new Button("Pattern 2");

		
		point.setOnAction(e->brush.pointMode());
		fill.setOnAction(e->brush.fillMode());
		pattern1.setOnAction(e->brush.pattern1Mode());
		pattern2.setOnAction(e->brush.pattern2Mode());
		
		
		
		
		
		colors.getChildren().addAll(label,point,fill,pattern1,pattern2);
			
		return colors;
		
		
	}

	Paint[][] firstSave;
	Paint[][] secondSave;
	
   /**
      generates the save/load buttons bar
   */	

	private Node makePresetButtons()
	{
		HBox colors=new HBox();
		Label label = new Label("Load/save ");
		Button circle=new Button("Load Shapes");
		Button save1=new Button("Save One");
		Button load1=new Button("Load One");
		Button save2=new Button("Save Two");
		Button load2=new Button("Load Two");
				
		circle.setOnAction(e->loadImage("imgs/shapes.png"));
		
		load1.setDisable(true);
		load2.setDisable(true);
		
		save1.setOnAction(e->{firstSave = saveCanvas();  load1.setDisable(false);});
		save2.setOnAction(e->{secondSave = saveCanvas();  load2.setDisable(false);});
		load1.setOnAction(e->drawCanvas(firstSave));
		load2.setOnAction(e->drawCanvas(secondSave));
		
		
		
		colors.getChildren().addAll(label,circle,save1,load1,save2,load2);
			
		return colors;
		
		
	
	}
	
   
	private Paint [][] saveCanvas()
	{
		
		Paint [][] mesh = new Paint[width][height];
		
		PixelReader read = image.getPixelReader();
	
		for(int i=0;i<width;i++)
		{	for(int j=0;j<height;j++)
			{
				mesh[i][j] = new PaintColor(read.getColor(i,j));
			}
		}
		
		return mesh;
		
	}
	
	private void drawCanvas(Paint[][] mesh)
	{
		PixelWriter write = this.image.getPixelWriter();
		
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				write.setColor(x,y,mesh[x][y].getColor());				
			
			}
		}
		
		
		
	}

   //loads an image up to the screen
	private void loadImage(String path)
	{
		Image img = new Image("file:"+path);
		
		int myWidth = (int) Math.min(width, img.getWidth());
		int myHeight = (int) Math.min(height, img.getHeight());
		
		
		PixelReader read = img.getPixelReader();
		PixelWriter write = this.image.getPixelWriter();
		
		
		
		for(int x=0;x<myWidth;x++)
		{
			for(int y=0;y<myHeight;y++)
			{
				write.setColor(x,y,read.getColor(mult*(x/mult),mult*(y/mult)));				
			
			}
		}
		
		
		
		
		
	}
	
	//draws a single color to the screen
private void flood(Color c)
{
	PixelWriter write = image.getPixelWriter();
	for(int i=0;i<width;i++)
	{	for(int j=0;j<height;j++)
		{
			write.setColor(i, j, c);
		}
	}
	
}


	
	/**
	 * calls the paint brush to paint the scene
	 * @param x - x coord of the image
	 * @param y - y coord of the image
	 */
	private void paint(double x,double y)
	{
		int xD = (int)(x/mult);
		int yD = (int)(y/mult);
		//make 2d mesh of image
		
		Paint [][] mesh = new Paint[width/mult][height/mult];
		
		PixelReader read = image.getPixelReader();
		
		for(int i=0;i<width/mult;i++)
		{	for(int j=0;j<height/mult;j++)
			{
				mesh[i][j] = new PaintColor(read.getColor(mult*i,mult*j));
			}
		}
		
		brush.paint(xD,yD,mesh);
		
		PixelWriter write = image.getPixelWriter();
		for(int i=0;i<width;i++)
		{	for(int j=0;j<height;j++)
			{
				write.setColor(i, j, mesh[i/mult][j/mult].getColor());
			}
		}
		
	}
	
	
	
	
	public static void main(String [] args)
	{
		launch(args);
	}
	
	
}
