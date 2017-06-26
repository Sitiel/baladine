package com.balady;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.balady.data.Coordinates;
import com.balady.data.GameMap;
import com.balady.data.Player;
import com.balady.data.Zone;
import com.balady.population.Consumer;
import com.balady.rest.ClientRest;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 

public class clientJavaApplication extends Application {
	GraphicsContext gc;
	GameMap game;
	Canvas canvas;
	VBox leftPanel;
	public static void main(String[] args) {
		launch(args);	
	}
	
	
	public void refreshDisplay(){
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
        gc.setFill(new Color(1, 0.1, 0.1, 1));
        for (Player p : game.getPlayers()) {
        	for(Zone z : p.getPubs()){
        		Coordinates pos = z.getCoordinates();
            	float influence =  (float) (z.getInfluence()/game.getEnd().getX()*canvas.getWidth());
            	gc.fillOval(pos.getY()/game.getEnd().getY()*canvas.getHeight(), pos.getX()/game.getEnd().getX()*canvas.getWidth(), influence, influence);
        	}        	
        }
		
		gc.setFill(new Color(0.1, 1, 0.1, 1));
        for (Player p : game.getPlayers()) {
        	Coordinates pos = p.getStand().getCoordinates();
        	float influence =  (float) (p.getStand().getInfluence()/game.getEnd().getX()*canvas.getWidth());
        	gc.fillOval(pos.getY()/game.getEnd().getY()*canvas.getHeight(), pos.getX()/game.getEnd().getX()*canvas.getWidth(), influence, influence);
        }
        
        
        gc.setFill(new Color(1, 1, 0.1, 1));
        for (Consumer c : game.getConsumers()) {
        	Coordinates pos = c.getCoordinates();
        	float influence =  10;
        	gc.fillRect(pos.getY()/game.getEnd().getY()*canvas.getHeight(), pos.getX()/game.getEnd().getX()*canvas.getWidth(), influence, influence);
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
	
	@Override
    public void start(Stage primaryStage) {
		
		game = new GameMap("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int current_hour = 0;
			
			@Override
			public void run() {
				game.refreshMap();
				
				if (game.getHour() == 0) {
					game.addConsumers();
				}
				if (game.getHour() != current_hour) {
					game.play();
					game.moveConsumers();
					game.sendSales();
					current_hour = game.getHour();
				}
			refreshDisplay();
			}
		}, 1000, 1000);
		
        primaryStage.setTitle("Balady project");
        
        
        BorderPane border = new BorderPane();
        Group carte = new Group();
        
        canvas = new Canvas(1000,1000);
        gc = canvas.getGraphicsContext2D();
        
        
        border.setCenter(carte);
        carte.getChildren().add(canvas);
        
        leftPanel = new VBox();
        border.setLeft(leftPanel);
        
        refreshDisplay();
        
        Scene s = new Scene(border, 300, 300, Color.WHITE);
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
