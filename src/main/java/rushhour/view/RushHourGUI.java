package rushhour.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import rushhour.model.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourSolver;
import rushhour.model.Vehicle;

public class RushHourGUI extends Application {
    private RushHour game;
    private GridPane gp;
    public Label moveLabel; 
    public int moveCounter = 0;
    private String filename;
    private Label messageLabel;

    @Override
    public void start(Stage stage) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a filename: ");
        filename = scanner.nextLine();
        scanner.close();
        game = new RushHour(filename);
        game.registerObserver(new GUIUpdater(game, this));
        gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setPadding(new Insets(5));
        for(int i=0; i<6; i++) {
            for(int j=0; j<6; j++) {
                gp.add(makeButton(i, j), j, i);
            }
        }

        for(Character symbol : game.getVehicles().keySet()) {
            Vehicle vehicle = game.getVehicles().get(symbol);
            if(vehicle.getBack().getRow() == vehicle.getFront().getRow()) {
                //set back arrow
                Node backTile = getNodeFromGridPane(gp, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                Button backSquare = (Button)(backTile);
                backSquare.setFont(new Font("Arial", 30));
                backSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.LEFT), this));
                backSquare.setGraphic(new ImageView(new Image("file:data/images/left_arrow.png", 40, 40, false, true)));
                //set front arrow
                Node frontTile = getNodeFromGridPane(gp, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                Button frontSquare = (Button)(frontTile);
                frontSquare.setFont(new Font("Arial", 30));
                frontSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.RIGHT), this));
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/right_arrow.png", 30, 30, false, true)));
                
                int middleCol = (vehicle.getBack().getCol() + vehicle.getFront().getCol()) / 2;
                Node middleTile = getNodeFromGridPane(gp, middleCol, vehicle.getBack().getRow());
                Button middleSquare = (Button) middleTile;
                backSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                middleSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                frontSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
            } else {
                //set back arrow
                Node backTile = getNodeFromGridPane(gp, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                Button backSquare = (Button)(backTile);
                backSquare.setFont(new Font("Arial", 30));
                backSquare.setGraphic(new ImageView(new Image("file:data/images/up_arrow.png", 30, 30, false, true)));
                backSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.UP), this));
        
                //set front arrow
                Node frontTile = getNodeFromGridPane(gp, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                Button frontSquare = (Button)(frontTile);
                frontSquare.setFont(new Font("Arial", 30));
                frontSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.DOWN), this));
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/down_arrow.png", 55, 55, false, true)));
                
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/down_arrow.png", 50, 50, false, true)));
                int middleRow = (vehicle.getBack().getRow() + vehicle.getFront().getRow()) / 2;
                Node middleTile = getNodeFromGridPane(gp, vehicle.getBack().getCol(), middleRow);
                Button middleSquare = (Button) middleTile;
                backSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                middleSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                frontSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
            }
        }
        Node exit = getNodeFromGridPane(gp, 5, 2);
        Button square = (Button)(exit);
        square.setFont(new Font("Arial", 10));
        square.setText("EXIT");

        BorderPane bottomPane = new BorderPane();
        bottomPane.setPadding(new Insets(5));
        bottomPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        moveLabel = new Label("Moves: " + moveCounter);
        moveLabel.setFont(new Font(15));
        bottomPane.setCenter(moveLabel);


        messageLabel = new Label();
        messageLabel.setTextFill(Color.GREEN);
        bottomPane.setLeft(messageLabel);
        messageLabel.setText("New Game!");
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(e -> {
            try {
                game = new RushHour(filename);
                game.registerObserver(new GUIUpdater(game, this));
                resetGame();
            } catch (IOException IOException) {
                System.out.println("File does not exist.");
            }
            moveCounter = 0;
            moveLabel.setText("Moves: " + moveCounter);
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("A New Game Has Started!");
        });
        bottomPane.setRight(resetButton);

        Button hintButton = new Button("Hint");
        hintButton.setOnAction(new Hinting(this));
        hintButton.setPrefWidth(50);

        Button solveButton = new Button("Solve");
        solveButton.setOnAction(event -> {
            RushHourSolver solver = new RushHourSolver(game, new ArrayList<>());
            RushHourSolver solution = solver.solve(game);
            if (solution == null) {
                messageLabel.setText("No solution found.");
            } else {
                List<Move> winningMoves = solution.getMoves();
                new Thread(() -> {
                    for (Move move : winningMoves) {
                        Platform.runLater(() -> {
                            try {
                                game.moveVehicle(move);
                            } catch (RushHourException RHE) {
                                System.out.println(RHE.getMessage());
                            }
                        });

                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException IE) {}
                    }
                }).start();
            }
        });
        solveButton.setPrefWidth(50);
        
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(hintButton, solveButton, resetButton);

        bottomPane.setRight(buttonBox);


        BorderPane root = new BorderPane();
        root.setCenter(gp);
        root.setBottom(bottomPane);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Rush Hour The Movie! What is war good for? Absolutely Nothing!");
        System.out.println(game);
        game.updatePostions();
        stage.show();
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    protected Button makeButton(int row, int col) {
        Button button = new Button();
        button.setPrefSize(56, 56);
        button.setPadding(Insets.EMPTY);
        button.setBorder(new Border(
            new BorderStroke(
                Color.BLACK, 
                BorderStrokeStyle.SOLID, 
                CornerRadii.EMPTY, 
                BorderStroke.THIN)));
        return button;
    }

    protected Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }    

    public void makeMove(Move move) throws RushHourException{
        game.updatePostions();
        if(game.isGameOver() == true) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Congratulations!");
            return;
        }
        try {
            Boolean shouldMove = false;
            for(Move possibleMove : game.getPossibleMoves()) {
                if(move.equals(possibleMove)) {
                    shouldMove = true;
                }
            }
            if(shouldMove == true && game.isGameOver() == false) {
                moveCounter++;
                game.moveVehicle(move);
                messageLabel.setTextFill(Color.BLACK);
                messageLabel.setText("Good Move!");
            } else {
                if(game.isGameOver() == false) {
                    messageLabel.setTextFill(Color.RED);
                    messageLabel.setText("Invalid move: Vehicle is impeded");
                }
            }
        } catch (RushHourException rhe) {
            System.out.println(rhe.getMessage());
        }
        if(game.isGameOver() == true) {
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Congratulations!");
        }
    }

    public RushHour getGame() {
        return game;
    }

    public GridPane getGridPane() {
        return gp;
    }

    private void resetGame() throws IOException {
        gp.getChildren().clear();
        for(int i=0; i<6; i++) {
            for(int j=0; j<6; j++) {
                gp.add(makeButton(i, j), j, i);
            }
        }
        for(Character symbol : game.getVehicles().keySet()) {
            Vehicle vehicle = game.getVehicles().get(symbol);
            if(vehicle.getBack().getRow() == vehicle.getFront().getRow()) {
                //set back arrow
                Node backTile = getNodeFromGridPane(gp, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                Button backSquare = (Button)(backTile);
                backSquare.setFont(new Font("Arial", 30));
                backSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.LEFT), this));
                backSquare.setGraphic(new ImageView(new Image("file:data/images/left_arrow.png", 40, 40, false, true)));
                //set front arrow
                Node frontTile = getNodeFromGridPane(gp, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                Button frontSquare = (Button)(frontTile);
                frontSquare.setFont(new Font("Arial", 30));
                frontSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.RIGHT), this));
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/right_arrow.png", 30, 30, false, true)));
                
                int middleCol = (vehicle.getBack().getCol() + vehicle.getFront().getCol()) / 2;
                Node middleTile = getNodeFromGridPane(gp, middleCol, vehicle.getBack().getRow());
                Button middleSquare = (Button) middleTile;
                backSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                middleSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                frontSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
            } else {
                //set back arrow
                Node backTile = getNodeFromGridPane(gp, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                Button backSquare = (Button)(backTile);
                backSquare.setFont(new Font("Arial", 30));
                backSquare.setGraphic(new ImageView(new Image("file:data/images/up_arrow.png", 30, 30, false, true)));
                backSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.UP), this));
        
                //set front arrow
                Node frontTile = getNodeFromGridPane(gp, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                Button frontSquare = (Button)(frontTile);
                frontSquare.setFont(new Font("Arial", 30));
                frontSquare.setOnAction(new MoveMaker(new Move(symbol, Direction.DOWN), this));
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/down_arrow.png", 55, 55, false, true)));
                
                frontSquare.setGraphic(new ImageView(new Image("file:data/images/down_arrow.png", 50, 50, false, true)));
                int middleRow = (vehicle.getBack().getRow() + vehicle.getFront().getRow()) / 2;
                Node middleTile = getNodeFromGridPane(gp, vehicle.getBack().getCol(), middleRow);
                Button middleSquare = (Button) middleTile;
                backSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                middleSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
                frontSquare.setBackground(new Background(new BackgroundFill(vehicle.getColor(), null, null)));
            }
        }
        Node exit = getNodeFromGridPane(gp, 5, 2);
        Button square = (Button)(exit);
        square.setFont(new Font("Arial", 10));
        square.setText("EXIT");
        game.updatePostions();
    }        

    public static void main(String[] args) {
        launch(args);
    }
}