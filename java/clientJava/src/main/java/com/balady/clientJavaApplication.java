package com.balady;

import java.util.Timer;
import java.util.TimerTask;

import com.balady.data.Coordinates;
import com.balady.data.GameMap;
import com.balady.data.Player;
import com.balady.data.Zone;
import com.balady.population.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;



public class clientJavaApplication extends Application {
	
	class ResizableCanvas extends Canvas {
		 
	    public ResizableCanvas() {
	      // Redraw canvas when size changes.
	      widthProperty().addListener(evt -> draw());
	      heightProperty().addListener(evt -> draw());
	    }
	 
	    private void draw() {
	      double width = getWidth();
	      double height = getHeight();
	
	      GraphicsContext gc = getGraphicsContext2D();
	      gc.clearRect(0, 0, width, height);
	      if (game.getMeteo() != null) {

				gc.setFill(new Color(1, 0.1, 0.1, 1));
				for (Player p : game.getPlayers()) {
					for (Zone z : p.getPubs()) {
						Coordinates pos = z.getCoordinates();
						float influence = (float) (z.getInfluence() / game.getEnd().getX() * width);
						gc.fillOval(pos.getX() / game.getEnd().getX() * width,
								pos.getY() / game.getEnd().getY() * height, influence, influence);
					}
				}

				gc.setFill(new Color(0.1, 1, 0.1, 1));
				for (Player p : game.getPlayers()) {
					Coordinates pos = p.getStand().getCoordinates();
					float influence = (float) (p.getStand().getInfluence() / game.getEnd().getX() * width);
					gc.fillOval(pos.getX() / game.getEnd().getX() * width,
							pos.getY() / game.getEnd().getY() * height, influence, influence);
				}

				gc.setFill(new Color(0.1, 0.1, 1, 1));
				for (Consumer c : game.getConsumers()) {
					Coordinates pos = c.getCoordinates();
					float influence = (float) (5 / game.getEnd().getX() * width);
					gc.fillRect(pos.getX() / game.getEnd().getX() * width,
							pos.getY() / game.getEnd().getY() * height, influence, influence);
				}
					
				Platform.runLater(() -> {
					leftPanel.getChildren().clear();
					for (Player p : game.getPlayers()) {
						Label t = new Label(p.getName() + " - " + p.getCash());
						t.setFont(new Font(20));
						leftPanel.getChildren().add(t);
					}
				});
			}
	      
	    }
	 
	    @Override
	    public boolean isResizable() {
	      return true;
	    }
	 
	    @Override
	    public double prefWidth(double height) {
	      return getWidth();
	    }
	 
	    @Override
	    public double prefHeight(double width) {
	      return getHeight();
	    }
	}

	
	
	GraphicsContext gc;
	GameMap game;
	ResizableCanvas canvas;
	VBox leftPanel;
	BorderPane border;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		game = new GameMap("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int current_hour = -1;

			@Override
			public void run() {
				game.refreshMap();

				if (game.getHour() == 1 && game.getHour() != current_hour && !game.getPlayers().isEmpty()) {
					game.clearConsumers();
					game.addConsumers();
				}
				if (game.getHour() != current_hour && !game.getPlayers().isEmpty()) {
					game.play();
					game.moveConsumers();
					game.sendSales();
					current_hour = game.getHour();
				}
				canvas.draw();
			}
		}, 1000, 1000);

		primaryStage.setTitle("Balady project");

		border = new BorderPane();
		Group carte = new Group();
		
		canvas = new ResizableCanvas();
		gc = canvas.getGraphicsContext2D();

		border.setCenter(carte);
		carte.getChildren().add(canvas);
		
		 canvas.widthProperty().bind(
                 border.widthProperty());
		 canvas.heightProperty().bind(
                 border.heightProperty());

		leftPanel = new VBox();
		border.setLeft(leftPanel);

		canvas.draw();

		Scene s = new Scene(border, 300, 300, Color.WHITE);
		primaryStage.setScene(s);
		primaryStage.show();
	}
}
