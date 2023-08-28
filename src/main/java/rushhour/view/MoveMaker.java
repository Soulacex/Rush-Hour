package rushhour.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import rushhour.model.Move;
import rushhour.model.RushHourException;

public class MoveMaker implements EventHandler<ActionEvent> {
    private RushHourGUI rushHour;
    private Move move;

    public MoveMaker(Move move, RushHourGUI rushHour) {
        this.rushHour = rushHour;
        this.move = move;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            rushHour.makeMove(move);
            rushHour.moveLabel.setText("Moves: " + rushHour.moveCounter);
        } catch (RushHourException rhe) {
            System.out.println(rhe.getMessage());
        }
    } 
}
