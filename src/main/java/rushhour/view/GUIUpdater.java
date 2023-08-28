package rushhour.view;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourObserver;
import rushhour.model.Vehicle;

public class GUIUpdater implements RushHourObserver {
    private RushHour game;
    private RushHourGUI gui;

    public GUIUpdater(RushHour game, RushHourGUI gui) {
        this.game = game;
        this.gui = gui;
    }

    @Override
    public void vehicleMoved(Vehicle car) {
        GridPane gp = gui.getGridPane();
        if(game.isGameOver() == true) {
            Node tile = gui.getNodeFromGridPane(gp, 5, 2);
            Button square = (Button)(tile);
            square.setText("");
        }
        for(int i=0; i<6; i++) {
            for(int j=0; j<6; j++) {
                Node tile = gui.getNodeFromGridPane(gp, j, i);
                Button square = (Button)(tile);
                square.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
                square.setGraphic(null);
                square.setOnAction(null);
                square.setBorder(new Border(
                    new BorderStroke(
                        Color.BLACK, 
                        BorderStrokeStyle.SOLID, 
                        CornerRadii.EMPTY, 
                        BorderStroke.THIN)));
            }
        }
        for(Character symbol : game.getVehicles().keySet()) {
            Vehicle vehicle = game.getVehicles().get(symbol);
            if(vehicle.getBack().getRow() == vehicle.getFront().getRow()) {
                //set back arrow
                Node backTile = gui.getNodeFromGridPane(gp, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                Button backSquare = (Button)(backTile);
                backSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.LEFT), gui));
                backSquare.setGraphic(new ImageView(new Image("file:data/images/left_arrow.png", 40, 40, false, true)));
                //set front arrow
                Node frontTile = gui.getNodeFromGridPane(gp, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                Button frontSquare = (Button)(frontTile);
                frontSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.RIGHT), gui));
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/right_arrow.png", 30, 30, false, true)));
                
                int middleCol = (vehicle.getBack().getCol() + vehicle.getFront().getCol()) / 2;
                Node middleTile = gui.getNodeFromGridPane(gp, middleCol, vehicle.getBack().getRow());
                Button middleSquare = (Button) middleTile;
                backSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                middleSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                frontSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
            } else {
                //set back arrow
                Node backTile = gui.getNodeFromGridPane(gp, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                Button backSquare = (Button)(backTile);
                backSquare.setGraphic(new ImageView(new Image("file:data/images/up_arrow.png", 30, 30, false, true)));
                backSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.UP), gui));
        
                //set front arrow
                Node frontTile = gui.getNodeFromGridPane(gp, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                Button frontSquare = (Button)(frontTile);
                frontSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.DOWN), gui));
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/down_arrow.png", 55, 55, false, true)));
                
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/down_arrow.png", 50, 50, false, true)));
                int middleRow = (vehicle.getBack().getRow() + vehicle.getFront().getRow()) / 2;
                Node middleTile = gui.getNodeFromGridPane(gp, vehicle.getBack().getCol(), middleRow);
                Button middleSquare = (Button) middleTile;
                backSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                middleSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                frontSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
            }
        }
    }
    
}
