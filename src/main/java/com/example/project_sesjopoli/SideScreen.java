package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.*;
import javafx.application.Platform;
import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Pawn;
import com.example.project_sesjopoli.game_objects.SubjectField;
import javafx.application.Platform;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Random;

import static com.example.project_sesjopoli.GameController.LINK;

public class SideScreen extends AnchorPane {

    public static final int FONT_SIZE = 18;
    private static final int INITIAL_ECTS = 30;
    private static final int LOOSE_FONT_SIZE = 25;
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
    AnchorPane quizPane;
    ArrayList<Label> moneyInfoLabels;
    Label question;
    ArrayList<Button> quizButtons;
    int lastDicedPosition;

    SideScreen(GameController controller, Board board){
        super();
        this.board=board;
        this.moneyInfoLabels =new ArrayList<>();
        initLabelsAndButtons();

        this.getChildren().add(isYourTurnLabel);
        this.getChildren().add(infoLabel);
        this.getChildren().add(endTurnButton);
        this.getChildren().add(movePawnButton);

        this.setStyle("-fx-background-color: rgb(121, 9, 15)");
    }

    private void showQuiz(GameController controller, Pawn pawn, ArrayList<Question> questions) {
        Platform.runLater(() -> {
            int random = new Random().nextInt(questions.size());
            for(int i=0;i< quizButtons.size();++i){
                int finalI = i;
                quizButtons.get(i).setOnAction(new EventHandler<>() {
                    @Override
                    public void handle(ActionEvent event) {
                        controller.sendQuizAnswer(finalI, random, pawn.getPlayerId() - 1);
                        quizPane.setVisible(false);
                    }
                });
            }
            setTextInQuizPane(questions.get(random));
        });
    }

    public void sendPawnInfo(GameController controller, Pawn pawn){
        assignEventHandlers(controller, pawn, board);
    }

    private void assignEventHandlers(GameController controller, Pawn pawn, Board board) {
        EventHandler<ActionEvent> endTurnEvent = e -> {
            controller.endTurnOnServer();
        };
        EventHandler<ActionEvent> movePawnEvent = e -> {

                int random = /*new Random().nextInt(6) + 1*/ 3;
                int randomPosition = (pawn.getPosition()+random)%24;
                lastDicedPosition = randomPosition;
                infoLabel.setText("Wylosowano: " + random + "\nPole: " + board.getFields().get(randomPosition).getName());
                movePawnButton.setDisable(true);

                if(board.getFields().get(randomPosition) instanceof SubjectField){
                    if (!controller.getPositionsWithHouses().containsKey(randomPosition) || !controller.getPositionsWithHouses().get(randomPosition).isVisible()) { // nikt nie ma tego pola
                        buyHousePane.setVisible(true);
                    }
                }
                if(board.getFields().get(randomPosition) instanceof QuizField){
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<GameState> responseEntity = restTemplate.getForEntity(LINK + "/", GameState.class);
                    GameState current = responseEntity.getBody();
                    showQuiz(controller, pawn, current.questions);
                    quizPane.setVisible(true);
                }

                controller.sendPositionUpdateToServer(pawn.getPlayerId()-1, pawn.getPosition()+random);

        };
        EventHandler<ActionEvent> buyHouseEvent = e -> {
                infoLabel.setText("Kupiono przedmiot");
                movePawnButton.setDisable(true);
                buyHousePane.setVisible(false);
                controller.sendPurchaseInformation(pawn.getPlayerId()-1, lastDicedPosition);
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
        initQuizPane();


        int rowIterator=0;
        for(Pawn pawn: board.getPawns()){
            Label label = new Label("gracz " + pawn.getPlayerId() + " ma " + INITIAL_ECTS + "ects");
            label.setFont(new Font(FONT_SIZE));
            label.setTextFill(Color.WHITE);
            moneyInfoLabels.add(label);
            moneyPane.add(label,0,rowIterator++);
        }
        this.getChildren().add(moneyPane);


        question = new Label("");
        question.setFont(new Font(FONT_SIZE));
        Button but1 = new Button("");
        but1.setFont(new Font(FONT_SIZE));
        Button but2 = new Button("");
        but2.setFont(new Font(FONT_SIZE));
        Button but3 = new Button("");
        but3.setFont(new Font(FONT_SIZE));
        Button but4 = new Button("");
        but4.setFont(new Font(FONT_SIZE));
        but1.setLayoutX(0);
        but1.setLayoutY(30);
        but2.setLayoutX(120);
        but2.setLayoutY(30);
        but3.setLayoutX(0);
        but3.setLayoutY(100);
        but4.setLayoutX(120);
        but4.setLayoutY(100);
        quizButtons = new ArrayList<>();
        quizButtons.add(but1);
        quizButtons.add(but2);
        quizButtons.add(but3);
        quizButtons.add(but4);


        quizPane.getChildren().add(question);
        quizPane.getChildren().add(but1);
        quizPane.getChildren().add(but2);
        quizPane.getChildren().add(but3);
        quizPane.getChildren().add(but4);
    }

    public void setTextInMoneyPane(int id, int money, String name) {
        if (money <= 0){
            moneyInfoLabels.get(id).setText(name + " przegrał!");
        } else{
            moneyInfoLabels.get(id).setText(name + " ma " + money + "ects");
        }
    }
    public void setTextInQuizPane(Question q) {
        question.setText(q.getQuestion());
        for (int i=0;i<4;++i){
            quizButtons.get(i).setText(q.getAnswers().get(i));
        }
    }

    public void displayLooserInfo(){
        for (Pawn pawn: board.getPawns()){
            moneyInfoLabels.get(pawn.getPlayerId() - 1).setVisible(false);
        }
        Label looseInfo = new Label("PRZEGRAŁEŚ!");
        looseInfo.setFont(new Font(LOOSE_FONT_SIZE));
        looseInfo.setTextFill(Color.WHITE);
        Platform.runLater(() -> {
            moneyPane.add(looseInfo, 0, 1);
        });
    }

    public void displayWinnerInfo(){
        for (Pawn pawn: board.getPawns()){
            moneyInfoLabels.get(pawn.getPlayerId() - 1).setVisible(false);
        }
        Label winnerInfo = new Label("WYGRAŁEŚ!");
        winnerInfo.setFont(new Font(LOOSE_FONT_SIZE));
        winnerInfo.setTextFill(Color.WHITE);
        Platform.runLater(() -> {
            moneyPane.add(winnerInfo, 0, 1);
        });

    }

    public void disableAllButtons(){
        movePawnButton.setDisable(true);
        endTurnButton.setDisable(true);
    }


    private void initMoneyPane() {
        moneyPane = new GridPane();
        moneyPane.setPadding(new Insets(10));
        moneyPane.setHgap(10);
        moneyPane.setVgap(10);
        moneyPane.setBackground(Background.fill(Color.DARKGRAY));
        moneyPane.setLayoutY(200);
    }

    private void initQuizPane() {
        quizPane = new AnchorPane();
        quizPane.setBackground(Background.fill(Color.WHITE));
        quizPane.setVisible(false);
        this.getChildren().add(quizPane);
        quizPane.setLayoutX(5);
        quizPane.setLayoutY(400);
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

    public GridPane getBuyHousePane() {
        return buyHousePane;
    }

    public AnchorPane getQuizPane() {
        return quizPane;
    }
}
