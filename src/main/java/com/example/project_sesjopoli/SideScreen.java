package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Pawn;
import com.example.project_sesjopoli.game_objects.QuizField;
import com.example.project_sesjopoli.game_objects.SubjectField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.project_sesjopoli.GameController.LINK;

public class SideScreen extends AnchorPane {

    public static final int FONT_SIZE = 18;
    private static final int INITIAL_ECTS = 30;
    private static final int LOOSE_FONT_SIZE = 25;
    Board board;
    Label infoLabel;
    Label buyQuestion;
    Button endTurnButton;
    Button movePawnButton;
    Button doBuyHouse;
    Button doNotBuyHouse;
    GridPane buyHousePane;
    AnchorPane quizPane;
    Label question;
    ArrayList<Button> quizButtons;
    AnchorPane playerInfoPane;
    int lastDicedPosition;

    SideScreen(GameController controller, Board board) throws IOException {
        super();
        this.board=board;
        initLabelsAndButtons();

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

                int random = 3;
                int randomPosition = (pawn.getPosition()+random)%24;
                lastDicedPosition = randomPosition;
                infoLabel.setText("Wylosowano: " + random + "\nPole: " + board.getFields().get(randomPosition).getName());
                movePawnButton.setVisible(false);

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
                movePawnButton.setVisible(false);
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

    private void initLabelsAndButtons() throws IOException {

        infoLabel = new Label();
        infoLabel.setFont(new Font(18));
        infoLabel.setTextFill(Color.WHITE);
        infoLabel.setLayoutX(200);
        infoLabel.setLayoutY(300);

        endTurnButton = new Button("Zakończ turę");
        endTurnButton.setFont(new Font(18));
        endTurnButton.setLayoutX(250);
        endTurnButton.setLayoutY(600);

        movePawnButton = new Button("Rzuć kostką");
        movePawnButton.setFont(new Font(18));
        movePawnButton.setLayoutX(0);
        movePawnButton.setLayoutY(600);

        doBuyHouse = new Button("TAK");
        doBuyHouse.setFont(new Font(12));

        doNotBuyHouse = new Button("NIE");
        doNotBuyHouse.setFont(new Font(12));


        buyQuestion = new Label("Czy chcesz kupic pole");
        buyQuestion.setFont(new Font(12));
        buyQuestion.setTextFill(Color.WHITE);


        initBuyHousePane();
        initPlayersLabels();
        initQuizPane();

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

    private AnchorPane createAnchorPane(String filename, int x, int y) throws IOException {
        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader rectangleLoader = new FXMLLoader(Menu.class.getResource(filename));
        Rectangle rectangle = rectangleLoader.load();
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);
        anchorPane.setVisible(false);
        anchorPane.setLayoutX(x);
        anchorPane.setLayoutY(y);


        Image diceImage = new Image("/dice.png");
        ImageView diceImageView = new ImageView(diceImage);
        diceImageView.setLayoutX(130);
        diceImageView.setLayoutY(45);
        diceImageView.setStyle("-fx-background-color: BLACK");
        diceImageView.setVisible(false);
        diceImageView.setFitHeight(50);
        diceImageView.setFitWidth(50);

        Text nameText = new Text();
        nameText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        nameText.setFill(Color.WHITE);
        nameText.setStroke(Color.BLACK);
        nameText.setStrokeWidth(1);
        nameText.setLayoutX(15);
        nameText.setLayoutY(30);

        Text moneyText = new Text();
        moneyText.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        moneyText.setFill(Color.WHITE);
        moneyText.setStroke(Color.BLACK);
        moneyText.setStrokeWidth(1);
        moneyText.setLayoutX(15);
        moneyText.setLayoutY(70);

        anchorPane.getChildren().add(rectangle);
        anchorPane.getChildren().add(nameText);
        anchorPane.getChildren().add(moneyText);
        anchorPane.getChildren().add(diceImageView);

        return anchorPane;
    }
    private void initPlayersLabels() throws IOException {
        playerInfoPane = new AnchorPane();
        playerInfoPane.setLayoutX(0);
        playerInfoPane.setLayoutY(0);
        this.getChildren().add(playerInfoPane);

        playerInfoPane.getChildren().add(createAnchorPane("rect_yellow.fxml", 5, 40));
        playerInfoPane.getChildren().add(createAnchorPane("rect_green.fxml", 200, 40));
        playerInfoPane.getChildren().add(createAnchorPane("rect_blue.fxml", 5, 150));
        playerInfoPane.getChildren().add(createAnchorPane("rect_orange.fxml", 200, 150));
    }

    public void setTextInQuizPane(Question q) {
        question.setText(q.getQuestion());
        for (int i=0;i<4;++i){
            quizButtons.get(i).setText(q.getAnswers().get(i));
        }
    }

    public void displayLooserInfo(){
        for (Pawn pawn: board.getPawns()){
            //moneyInfoLabels.get(pawn.getPlayerId() - 1).setVisible(false);
        }
        Label looseInfo = new Label("PRZEGRAŁEŚ!");
        looseInfo.setFont(new Font(LOOSE_FONT_SIZE));
        looseInfo.setTextFill(Color.WHITE);
        Platform.runLater(() -> {
           // moneyPane.add(looseInfo, 0, 1);
        });
    }

    public void displayWinnerInfo(){
        for (Pawn pawn: board.getPawns()){
            //moneyInfoLabels.get(pawn.getPlayerId() - 1).setVisible(false);
        }
        Label winnerInfo = new Label("WYGRAŁEŚ!");
        winnerInfo.setFont(new Font(LOOSE_FONT_SIZE));
        winnerInfo.setTextFill(Color.WHITE);
        Platform.runLater(() -> {
            //moneyPane.add(winnerInfo, 0, 1);
        });

    }

    public void disableAllButtons(){
        movePawnButton.setDisable(true);
        endTurnButton.setDisable(true);
    }

    private void initQuizPane() {
        quizPane = new AnchorPane();
        quizPane.setBackground(Background.fill(Color.WHITE));
        quizPane.setVisible(false);
        this.getChildren().add(quizPane);
        quizPane.setLayoutX(5);
        quizPane.setLayoutY(400);
    }


    public Button getEndTurnButton() {
        return endTurnButton;
    }
    public Button getMovePawnButton() {
        return movePawnButton;
    }

    public void initBuyHousePane(){
        buyHousePane = new GridPane();
        buyHousePane.setPadding(new Insets(5));
        buyHousePane.setHgap(5);
        buyHousePane.setVgap(5);

        buyHousePane.add(buyQuestion,1,13); // kolumna, wiersz
        buyHousePane.add(doBuyHouse,0,14);
        buyHousePane.add(doNotBuyHouse,2,14);
        buyHousePane.setLayoutY(200);
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
