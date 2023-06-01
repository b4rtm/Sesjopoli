package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Pawn;
import com.example.project_sesjopoli.game_objects.SubjectField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class SideScreen extends AnchorPane {

    public static final int FONT_SIZE = 18;
    private static final int INITIAL_ECTS = 30;
    Board board;
    Label isYourTurnLabel;
    Label infoLabel;
    Label buyQuestion;
    Button endTurnButton;
    Button movePawnButton;
    Button doBuyHouse;
    Button doNotBuyHouse;
    GridPane buyHousePane;
    GridPane moneyPane;
    ArrayList<Label> moneyInfoLabel;

    SideScreen(GameController controller, Pawn pawn, Board board){
        super();
        this.board=board;
        this.moneyInfoLabel=new ArrayList<>();
        initLabelsAndButtons();
        assignEventHandlers(controller, pawn, board);

        this.getChildren().add(isYourTurnLabel);
        this.getChildren().add(infoLabel);
        this.getChildren().add(endTurnButton);
        this.getChildren().add(movePawnButton);

        this.setStyle("-fx-background-color: rgb(121, 9, 15)");
    }

    private void assignEventHandlers(GameController controller, Pawn pawn, Board board) {
        EventHandler<ActionEvent> endTurnEvent = e -> {
            controller.endTurnOnServer();
        };
        EventHandler<ActionEvent> movePawnEvent = e -> {
            if(controller.getTurn()== pawn.getPlayerId()){
                int random = /*new Random().nextInt(6) +*/ 1;
                int randomPosition = pawn.getPosition()+random;
                infoLabel.setText("Wylosowano: " + random + "\nPole: " + board.getFields().get(randomPosition).getName());
                movePawnButton.setDisable(true);

                if(board.getFields().get(randomPosition) instanceof SubjectField){
                    if (!controller.getPositionsWithHouses().contains(randomPosition)) { // nikt nie ma tego pola
                        buyHousePane.setVisible(true);
                    }
                }
                controller.sendPositionUpdateToServer(pawn.getPlayerId()-1, pawn.getPosition()+random);
            }
        };
        EventHandler<ActionEvent> buyHouseEvent = e -> {
            if(controller.getTurn()== pawn.getPlayerId()){
                infoLabel.setText("Kupiono przedmiot");
                movePawnButton.setDisable(true);
                buyHousePane.setVisible(false);
                controller.sendPurchaseInformation(pawn.getPlayerId()-1, pawn.getPosition());


            }
        };

        EventHandler<ActionEvent> doNotBuyHouseEvent = e -> {
                buyHousePane.setVisible(false);
        };

        endTurnButton.setOnAction(endTurnEvent);
        movePawnButton.setOnAction(movePawnEvent);
        doBuyHouse.setOnAction(buyHouseEvent);
        doNotBuyHouse.setOnAction(doNotBuyHouseEvent);
    }

    private void initLabelsAndButtons() {
        isYourTurnLabel = new Label();
        isYourTurnLabel.setFont(new Font(18));
        isYourTurnLabel.setTextFill(Color.WHITE);
        isYourTurnLabel.setLayoutX(0);
        isYourTurnLabel.setLayoutY(5);


        infoLabel = new Label();
        infoLabel.setFont(new Font(18));
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setLayoutX(0);
        infoLabel.setLayoutY(45);

        endTurnButton = new Button("Zakończ turę");
        endTurnButton.setFont(new Font(18));
        endTurnButton.setLayoutX(250);
        endTurnButton.setLayoutY(600);

        movePawnButton = new Button("Rzuć kostką");
        movePawnButton.setFont(new Font(18));
        movePawnButton.setLayoutX(0);
        movePawnButton.setLayoutY(600);

        doBuyHouse = new Button("TAK");
        doBuyHouse.setFont(new Font(18));

        doNotBuyHouse = new Button("NIE");
        doNotBuyHouse.setFont(new Font(18));


        buyQuestion = new Label("Czy chcesz kupic pole");
        buyQuestion.setFont(new Font(18));
        buyQuestion.setTextFill(Color.WHITE);



        initBuyHousePane();
        initMoneyPane();


        int rowIterator=0;
        for(Pawn pawn: board.getPawns()){
            Label label = new Label("gracz " + pawn.getPlayerId() + " ma " + INITIAL_ECTS + "ects");
            label.setFont(new Font(FONT_SIZE));
            label.setTextFill(Color.WHITE);
            moneyInfoLabel.add(label);
            moneyPane.add(label,0,rowIterator++);
        }
        this.getChildren().add(moneyPane);

    }

    public void setTextInMoneyPane(int id, int money, String name) {
        moneyInfoLabel.get(id).setText(name + " ma " + money + "ects");
    }

    private void initMoneyPane() {
        moneyPane = new GridPane();
        moneyPane.setPadding(new Insets(10));
        moneyPane.setHgap(10);
        moneyPane.setVgap(10);
        moneyPane.setBackground(Background.fill(Color.DARKGRAY));
        moneyPane.setLayoutY(200);
    }

    public Label getIsYourTurnLabel() {
        return isYourTurnLabel;
    }
    public Button getEndTurnButton() {
        return endTurnButton;
    }
    public Button getMovePawnButton() {
        return movePawnButton;
    }

    public void initBuyHousePane(){
        buyHousePane = new GridPane();
        buyHousePane.setPadding(new Insets(10));
        buyHousePane.setHgap(10);
        buyHousePane.setVgap(10);

        buyHousePane.add(buyQuestion,1,9); // kolumna, wiersz
        buyHousePane.add(doBuyHouse,0,10);
        buyHousePane.add(doNotBuyHouse,2,10);
        buyHousePane.setVisible(false);
        this.getChildren().add(buyHousePane);
    }
}
