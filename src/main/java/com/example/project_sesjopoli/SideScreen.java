package com.example.project_sesjopoli;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class SideScreen extends BorderPane {

    public static final int FONT_SIZE = 18;
    Label testLabel;
    Button endTurnButton;

    SideScreen(GameController controller){
        super();
        testLabel = new Label();
        testLabel.setFont(new Font(FONT_SIZE));
        testLabel.setTextFill(Color.WHITE);
        endTurnButton = new Button("Zakończ turę");
        endTurnButton.setFont(new Font(FONT_SIZE));
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
