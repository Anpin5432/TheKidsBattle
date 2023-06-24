package application;
	

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;


public class rompecocos extends Application {
	@FXML
	private StackPane rompeContainer;

	// Event Listener on Button.onAction
		
	
	private Timeline timeline;
	
	private void init(Stage primaryStage) {
		
		Group root = new Group();
		primaryStage.setScene(new Scene(root));
		Image image = new Image (getClass().getResourceAsStream("dragon.jpg"));
		int numOfColumns = (int) (image.getWidth() / Piece.SIZE);
		int numOfRows = (int) (image.getHeight() / Piece.SIZE);
		
		final Desk desk = new Desk(numOfColumns, numOfRows);
		
		final List<Piece> pieces = new ArrayList<Piece>();
		for (int col = 0; col < numOfColumns; col++) {
			for(int row = 0; row < numOfRows; row++) {
				int x = col * Piece.SIZE;
				int y = row * Piece.SIZE;
				final Piece piece = new Piece (image, x, y, row>0, col>0,
						row<numOfRows -1, col < numOfColumns -1,
						desk.getWidth(), desk.getHeight());
				pieces.add(piece);
			}
		}
		desk.getChildren().addAll(pieces);
		
		Button shuffleButton = new Button("Revolver");
		shuffleButton.setStyle("-fx-font-size: 2em;");
		shuffleButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle (ActionEvent actionEvent) {
				if (timeline != null) timeline.stop();
				timeline = new Timeline();
				for (final Piece piece : pieces) {
					piece.setActive();
					double shuffleX = Math.random()*
							(desk.getWidth() - Piece.SIZE + 48f) -
							24f - piece.getCorrectX();
					double shuffleY = Math.random() *
							(desk.getHeight() - Piece.SIZE + 30f) -
							15f - piece.getCorrectY();
					timeline.getKeyFrames().add(
							new KeyFrame(Duration.seconds(1),
									new KeyValue(piece.translateXProperty(), shuffleX),
									new KeyValue(piece.translateYProperty(), shuffleY)));
				}
				timeline.playFromStart();
				
			}
		});
		Button solveButton = new Button("Resolver");
		solveButton.setStyle("-fx-font-size: 2em;");
		solveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle (ActionEvent actionEvent) {
				if (timeline != null) timeline.stop();
				timeline = new Timeline();
				for (final Piece piece : pieces) {
					piece.setInactive();
					timeline.getKeyFrames().add(
							new KeyFrame(Duration.seconds(1),
									new KeyValue(piece.translateXProperty(), 0),
									new KeyValue(piece.translateYProperty(), 0)));
				}
				timeline.playFromStart();
				
			}

		});
		HBox buttonBox = new HBox(8);
		buttonBox.getChildren().addAll(shuffleButton, solveButton);
		VBox vb = new VBox(10);
		vb.getChildren().addAll(desk, buttonBox);
		root.getChildren().addAll(vb);
		
	}
	
	public static class Desk extends Pane {
		Desk(int numOfColumns, int numOfRows) {
			setStyle("-fx-background-color: #cccccc; "+
		             "-fx-border-color: #464646; "+
					 "-fx-effect: innershadow( two-pass-box, rgba(0,0,0,0.8) , 15, 0.0, 0, 4);" );
			double DESK_WIDTH = Piece.SIZE * numOfColumns;
			double DESK_HEIGHT = Piece.SIZE * numOfRows;
			setPrefSize(DESK_WIDTH, DESK_HEIGHT);
			setMaxSize(DESK_WIDTH, DESK_HEIGHT);
			autosize();
			
			Path grid = new Path();
			grid.setStroke(Color.rgb(70, 70, 70));
			getChildren().add(grid);
			for (int col=0; col < numOfColumns - 1; col++) {
				grid.getElements().addAll(
						new MoveTo(Piece.SIZE + Piece.SIZE * col, 5),
						new LineTo(Piece.SIZE + Piece.SIZE * col, Piece.SIZE * numOfRows - 5));
			}
			
			for (int row=0; row < numOfRows - 1; row++) {
				grid.getElements().addAll(
						new MoveTo(5, Piece.SIZE + Piece.SIZE * row),
						new LineTo(Piece.SIZE * numOfColumns - 5, Piece.SIZE + Piece.SIZE * row));
			}
		}
		@Override protected void layoutChildren() {}
		
	}
	
	public static class Piece extends Parent {

		public static final int SIZE = 100;
		private final double correctX;
		private final double correctY;
		private final boolean hasTopTab;
		private final boolean hasLeftTab;
		private final boolean hasBottomTab;
		private final boolean hasRightTab;
		private double startDragX;
		private double startDragY;
		private Shape pieceStroke;
		private Shape pieceClip;
		private ImageView imageView = new ImageView();
		private Point2D dragAnchor;
		
		public Piece (Image image, final double correctX, final double correctY, 
				      boolean topTab, boolean leftTab, boolean bottomTab, boolean rightTab,
				      final double deskWidth, final double deskHeight) {
			this.correctX = correctX;
			this.correctY = correctY;
			this.hasTopTab = topTab;
			this.hasLeftTab = leftTab;
			this.hasBottomTab = bottomTab;
			this.hasRightTab = rightTab;
			
			pieceClip = createPiece();
			pieceClip.setFill(Color.WHITE);
			pieceClip.setStroke(null);
			
			pieceStroke = createPiece();
			pieceStroke.setFill(null);
			pieceStroke.setStroke(Color.BLACK);
			
			imageView.setImage(image);
			imageView.setClip(pieceClip);
			setFocusTraversable(true);
			getChildren().addAll(imageView, pieceStroke);
			
			setCache(true);
			
			setInactive();
			
			setOnMousePressed(new EventHandler<MouseEvent>() {
				public void handle (MouseEvent me) {
					toFront();
					startDragX = getTranslateX();
					startDragY = getTranslateY();
					dragAnchor = new Point2D(me.getSceneX(), me.getSceneY());
					}
			});
			setOnMouseReleased(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent me) {
					if (getTranslateX() < (10) && getTranslateX() > (-10) &&
							getTranslateY() < (10) && getTranslateY() > (-10)) {
						setTranslateX(0);
						setTranslateY(0);
						setInactive();
					}
				}
			});
		    setOnMouseDragged (new EventHandler<MouseEvent>() {
		    	public void handle(MouseEvent me) {
		    		double newTranslateX = startDragX
		    				                + me.getSceneX() - dragAnchor.getX();
		    		double newTranslateY = startDragY
		    				                + me.getSceneY() - dragAnchor.getY();
		    		double minTranslateX = - 45f - correctX;
		    		double maxTranslateX = (deskWidth - Piece.SIZE + 50f) - correctX;
		    		double minTranslateY = - 30f - correctY;
		    		double maxTranslateY = (deskHeight - Piece.SIZE + 70f) + correctY;
		    		if ((newTranslateX > minTranslateX) && 
		    				(newTranslateX < maxTranslateX) &&
		    				(newTranslateY > minTranslateY) &&
		    				(newTranslateY < maxTranslateY)) {
		    			setTranslateX(newTranslateX);
		    			setTranslateY(newTranslateY);
		    		}			
		    	}
		    });	
		}

		private Shape createPiece() {
			Shape shape = createPieceRectangle();
			if (hasRightTab) {
				shape = Shape.union(shape,
						createPieceTab(69.5f, 0f, 10f, 17.5f, 50f, -12.5f, 11.5f, 25f,
								56.25f, -14f, 6.25f, 56.25f, 14f, 6.25f));
			}
			if (hasBottomTab) {
				shape = Shape.union(shape,
						createPieceTab(0f, 69.5f, 17.5f, 10f, -12.5f, 50f, 25f,
								11f, -14f, 56.25f, 6.25f, 14f, 56.25f, 6.25f));
			}
			if (hasLeftTab) {
				shape = Shape.union(shape,
						createPieceTab(-31f, 0f, 10f, 17.5f, -50f, -12.5f, 11f, 25f,
								-43.75f, -14f, 6.25f, -43.75f, 14f, 6.25f));
			}
			if (hasTopTab) {
				shape = Shape.union(shape,
						createPieceTab(0f, -31f, 17.5f, 10f, -12.5f, -50f, 25f, 12.5f,
							 -14f, -43.75f, 6.25f, 14f, -43.75f, 6.25f));
			}
			shape.setTranslateX(correctX);
			shape.setTranslateY(correctY);
			shape.setLayoutX(50f);
			shape.setLayoutY(50f);
			return shape;
		}
		
		private Rectangle createPieceRectangle() {
			Rectangle rec = new Rectangle();
			rec.setX(-50);
			rec.setY(-50);
			rec.setWidth(SIZE);
			rec.setHeight(SIZE);
			return rec;
			}
		
		private Shape createPieceTab(double eclipseCenterX, double eclipseCenterY, double eclipseRadiusX, double eclipseRadiusY,
				                     double rectangleX, double rectangleY, double rectangleWidth, double rectangleHeight,
				                     double circle1CenterX, double circle1CenterY, double circle1Radius,
				                     double circle2CenterX, double circle2CenterY, double circle2Radius) {
			Ellipse e = new Ellipse(eclipseCenterX, eclipseCenterY, eclipseRadiusX, eclipseRadiusY);
			Rectangle r = new Rectangle(rectangleX, rectangleY, rectangleWidth, rectangleHeight);
			Shape tab = Shape.union(e, r);
			Circle c1 = new Circle(circle1CenterX, circle1CenterY, circle1Radius);
			tab = Shape.subtract(tab, c1);
			Circle c2 = new Circle(circle2CenterX, circle2CenterY, circle2Radius);
			tab = Shape.subtract(tab, c2);
			return tab;
		}
		
		public void setActive() {
			setDisable(false);
			setEffect(new DropShadow());
			toFront();	
		}
		
		public void setInactive() {
			setDisable(true);
			setEffect(null);
			toBack();	
		}
		
		public double getCorrectX() {
		return correctX;
	}
		public double getCorrectY() {
			return correctY;
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		init(primaryStage);
		primaryStage.show();
	}
		
	
	
	public static void main(String[] args) {
		launch(args);
	
}
	
}
