package rushhour.view;

import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.Vehicle;

public class Hinting implements EventHandler<ActionEvent>{
    private RushHourGUI gui;

    public Hinting(RushHourGUI gui) {
        this.gui = gui;
    }
    
    @Override
    public void handle(ActionEvent event) {
        gui.getGame().updatePostions();
        Random random = new Random();
        Object[] possibleMoves = gui.getGame().getPossibleMoves().toArray();
        Move move = (Move)(possibleMoves[random.nextInt(possibleMoves.length)]);
        gui.getMessageLabel().setTextFill(Color.BLACK);
        gui.getMessageLabel().setText("Try the highlighted move");
        Vehicle car = gui.getGame().getVehicles().get(move.getSymbol());
        if(move.getDirection() == Direction.RIGHT || move.getDirection() == Direction.DOWN) {
            Node tile = gui.getNodeFromGridPane(gui.getGridPane(), car.getFront().getCol(), car.getFront().getRow());
            Button square = (Button)tile;
            square.setBorder(new Border(new BorderStroke(Color.DARKGREEN, 
            BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        } else {
            Node tile = gui.getNodeFromGridPane(gui.getGridPane(), car.getBack().getCol(), car.getBack().getRow());
            Button square = (Button)tile;
            square.setBorder(new Border(new BorderStroke(Color.DARKGREEN, 
            BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.MEDIUM)));
        }
    }
}
