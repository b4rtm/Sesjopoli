package com.example.project_sesjopoli;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SideScreen extends BorderPane {

    Label testLabel;
    Button endTurnButton;

    SideScreen(GameController controller){
        super();
        testLabel = new Label();
        testLabel.setFont(new Font(18));
        testLabel.setTextFill(Color.WHITE);
        endTurnButton = new Button("Zakończ turę");
        endTurnButton.setFont(new Font(18));
        EventHandler<ActionEvent> endTurnEvent = e -> {
            controller.endTurn(testLabel);
        };
        endTurnButton.setOnAction(endTurnEvent);

        this.setTop(testLabel);
        this.setCenter(endTurnButton);
        this.setStyle("-fx-background-color: rgb(121, 9, 15)");
    }

    public Label getTestLabel() {
        return testLabel;
    }
}
