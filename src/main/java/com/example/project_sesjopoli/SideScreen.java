package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Pawn;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

public class SideScreen extends AnchorPane {

    public static final int FONT_SIZE = 18;
    Label isYourTurnLabel;
    Button endTurnButton;

    SideScreen(GameController controller, Pawn p, Board b){
        super();
        initLabelsAndButtons();
        assignEventHandlers(controller, p, b);

        this.getChildren().add(isYourTurnLabel);
        this.getChildren().add(endTurnButton);
        this.setStyle("-fx-background-color: rgb(121, 9, 15)");
    }

    private void assignEventHandlers(GameController controller, Pawn p, Board b) {
        EventHandler<ActionEvent> endTurnEvent = e -> {
            controller.endTurnOnServer();
        };
        endTurnButton.setOnAction(endTurnEvent);
    }

    private void initLabelsAndButtons() {
        isYourTurnLabel = new Label();
        isYourTurnLabel.setFont(new Font(18));
        isYourTurnLabel.setTextFill(Color.WHITE);
        endTurnButton = new Button("Zakończ turę");
        endTurnButton.setFont(new Font(18));
        isYourTurnLabel.setLayoutX(0);
        isYourTurnLabel.setLayoutY(5);
        endTurnButton.setLayoutX(250);
        endTurnButton.setLayoutY(600);
    }

    public Label getIsYourTurnLabel() {
        return isYourTurnLabel;
    }
    public Button getEndTurnButton() {
        return endTurnButton;
    }
}
