package com.example.project_sesjopoli;

import com.example.project_sesjopoli.game_objects.Board;
import com.example.project_sesjopoli.game_objects.Pawn;
import com.example.project_sesjopoli.game_objects.QuizField;
import com.example.project_sesjopoli.game_objects.SubjectField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.project_sesjopoli.GameController.LINK;

public class SideScreen extends AnchorPane {

    public static final int FONT_SIZE = 16;
    private static final int LOOSE_FONT_SIZE = 25;
    Board board;
    Label info;
    Text buyQuestion;
    Button endTurnButton;
    ImageView throwDice;
    ImageView dicedValue;
    Button doBuyHouse;
    Button doNotBuyHouse;
    AnchorPane buyHousePane;
    AnchorPane punishmentInfoPane;
    AnchorPane quizPane;
    Label question;
    Text winnerInfo;
    Text looserInfo;
    ArrayList<Button> quizButtons;
    ArrayList<Image> images;
    AnchorPane playerInfoPane;
    int lastDicedPosition;

    SideScreen(GameController controller, Board board) throws IOException {
        super();
        this.board=board;
        initLabelsAndButtons();
        loadImages();

        this.getChildren().add(endTurnButton);
        this.getChildren().add(throwDice);
        this.getChildren().add(dicedValue);

        this.setStyle("-fx-background-color: rgb(121, 9, 15)");

    }

    private void loadImages(){
        images = new ArrayList<>();
        for (int i=1;i<=6;++i){
            Image img = new Image("/dice_" + i + "_icon.png");
            images.add(img);
        }
    }

    private void showQuiz(GameController controller, Pawn pawn, ArrayList<Question> questions) {
        Platform.runLater(() -> {
            int random = new Random().nextInt(questions.size());
            int answer = questions.get(random).getCorrectAnswerIndex();
            for(int i=0;i< quizButtons.size();++i){
                int finalI = i;
                quizButtons.get(i).setOnAction(new EventHandler<>() {
                    @Override
                    public void handle(ActionEvent event) {
                        controller.sendQuizAnswer(finalI, random, pawn.getPlayerId() - 1);
                        System.out.println("i: " + finalI);
                        System.out.println("answer: " + answer);
                        if(finalI==answer){
                            info.setText("Poprawna odpowiedź");
                        }
                        else{
                            info.setText("Błędna odpowiedź");
                        }
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
            dicedValue.setVisible(false);
        };
        EventHandler<MouseEvent> movePawnEvent = e -> {

                int random = new Random().nextInt(6)  + 1;
                int randomPosition = (pawn.getPosition()+random)%24;
                lastDicedPosition = randomPosition;
                info.setText("Stoisz na polu: " + board.getFields().get(randomPosition).getName());
                dicedValue.setImage(images.get(random-1));
                dicedValue.setVisible(true);
                throwDice.setVisible(false);

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
                info.setText("Kupiono przedmiot");
                throwDice.setVisible(false);
                buyHousePane.setVisible(false);
                controller.sendPurchaseInformation(pawn.getPlayerId()-1, lastDicedPosition);
        };

        EventHandler<ActionEvent> doNotBuyHouseEvent = e -> {
                buyHousePane.setVisible(false);
        };

        Scale scale = new Scale(1.2, 1.2, 0, 0);

        throwDice.setOnMouseEntered(event -> {
            Bounds bounds = throwDice.getBoundsInLocal();
            double centerX = bounds.getWidth() / 2;
            double centerY = bounds.getHeight() / 2;

            scale.setPivotX(centerX);
            scale.setPivotY(centerY);
            throwDice.getTransforms().add(scale);
        });
        throwDice.setOnMouseExited(event -> {
            throwDice.getTransforms().remove(scale); // Remove the scaling transform
        });

        endTurnButton.setOnAction(endTurnEvent);
        throwDice.setOnMouseClicked(movePawnEvent);
        doBuyHouse.setOnAction(buyHouseEvent);
        doNotBuyHouse.setOnAction(doNotBuyHouseEvent);
    }

    private void initLabelsAndButtons() throws IOException {
        looserInfo = new Text("PRZEGRAŁEŚ!");
        looserInfo.setFont(Font.font("Arial", FontWeight.BOLD, 44));
        looserInfo.setFill(Color.WHITE);
        looserInfo.setStroke(Color.BLACK);
        looserInfo.setStrokeWidth(2);
        looserInfo.setSmooth(true);
        looserInfo.setLayoutX(5);
        looserInfo.setLayoutY(370);
        looserInfo.setVisible(false);
        this.getChildren().add(looserInfo);

        winnerInfo = new Text("WYGRAŁEŚ!");
        winnerInfo.setFont(Font.font("Arial", FontWeight.BOLD, 44));
        winnerInfo.setFill(Color.WHITE);
        winnerInfo.setStroke(Color.BLACK);
        winnerInfo.setStrokeWidth(2);
        winnerInfo.setSmooth(true);
        winnerInfo.setLayoutX(5);
        winnerInfo.setLayoutY(370);
        winnerInfo.setVisible(false);
        this.getChildren().add(winnerInfo);

        FXMLLoader punishmentInfoPaneLoader = new FXMLLoader(Menu.class.getResource("punishment_info.fxml"));
        punishmentInfoPane = punishmentInfoPaneLoader.load();
        info = new Label("Panel Informacyjny...");
        info.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 18));
        info.setLayoutX(10);
        info.setLayoutY(5);
        info.setTextAlignment(TextAlignment.CENTER);
        info.setWrapText(true);
        info.setMaxWidth(360);
        info.setTextFill(Color.BLACK);
        punishmentInfoPane.getChildren().add(info);
        punishmentInfoPane.setLayoutX(5);
        punishmentInfoPane.setLayoutY(230);
        this.getChildren().add(punishmentInfoPane);

        endTurnButton = new Button("Zakończ turę");
        endTurnButton.setFont(new Font(18));
        endTurnButton.setLayoutX(250);
        endTurnButton.setLayoutY(600);
        endTurnButton.setVisible(false);

        Image diceImage = new Image("/dice3.png");
        throwDice = new ImageView(diceImage);
        throwDice.setFitHeight(80);
        throwDice.setPreserveRatio(true);
        throwDice.setLayoutX(0);
        throwDice.setLayoutY(550);
        throwDice.setSmooth(true);
        throwDice.setVisible(false);

        dicedValue = new ImageView();
        dicedValue.setFitHeight(80);
        dicedValue.setPreserveRatio(true);
        dicedValue.setLayoutX(20);
        dicedValue.setLayoutY(550);
        dicedValue.setSmooth(true);

        doBuyHouse = new Button("TAK");
        doBuyHouse.setFont(new Font(20));
        doBuyHouse.setLayoutX(40);
        doBuyHouse.setLayoutY(100);

        doNotBuyHouse = new Button("NIE");
        doNotBuyHouse.setFont(new Font(20));
        doNotBuyHouse.setLayoutX(240);
        doNotBuyHouse.setLayoutY(100);


        buyQuestion = new Text("Czy chcesz kupić to pole?");
        buyQuestion.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        buyQuestion.setFill(Color.WHITE);
        buyQuestion.setStroke(Color.BLACK);
        buyQuestion.setStrokeWidth(1.2);
        buyQuestion.setLayoutX(20);
        buyQuestion.setLayoutY(40);


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
        question.setTextAlignment(TextAlignment.CENTER);
        question.setWrapText(true);
        question.setMaxWidth(360);
        question.setLayoutX(10);
        question.setLayoutY(40);
        but1.setLayoutX(20);
        but1.setLayoutY(100);
        but2.setLayoutX(250);
        but2.setLayoutY(100);
        but3.setLayoutX(20);
        but3.setLayoutY(150);
        but4.setLayoutX(250);
        but4.setLayoutY(150);
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

    private AnchorPane createPlayerAnchorPane(String filename, int x, int y) throws IOException {
        AnchorPane anchorPane = new AnchorPane();
        FXMLLoader rectangleLoader = new FXMLLoader(Menu.class.getResource(filename));
        Rectangle rectangle = rectangleLoader.load();
        rectangle.setLayoutX(0);
        rectangle.setLayoutY(0);
        anchorPane.setVisible(false);
        anchorPane.setLayoutX(x);
        anchorPane.setLayoutY(y);


        Image diceImage = new Image("/dice2.png");
        ImageView diceImageView = new ImageView(diceImage);
        diceImageView.setLayoutX(126);
        diceImageView.setLayoutY(45);
        diceImageView.setVisible(false);
        diceImageView.setFitHeight(45);
        diceImageView.setPreserveRatio(true);
        diceImageView.setSmooth(true);

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

        playerInfoPane.getChildren().add(createPlayerAnchorPane("rect_yellow.fxml", 5, 10));
        playerInfoPane.getChildren().add(createPlayerAnchorPane("rect_green.fxml", 200, 10));
        playerInfoPane.getChildren().add(createPlayerAnchorPane("rect_blue.fxml", 5, 120));
        playerInfoPane.getChildren().add(createPlayerAnchorPane("rect_orange.fxml", 200, 120));
    }

    public void setTextInQuizPane(Question q) {
        question.setText(q.getQuestion());
        for (int i=0;i<4;++i){
            quizButtons.get(i).setText(q.getAnswers().get(i));
        }
    }

    public void displayLooserInfo(){
        Platform.runLater(() -> {
            looserInfo.setVisible(true);
        });
    }

    public void displayWinnerInfo(){
        Platform.runLater(() -> {
            winnerInfo.setVisible(true);
        });
    }

    private void initQuizPane() throws IOException {
        FXMLLoader quizRectLoader = new FXMLLoader(Menu.class.getResource("quiz_rect.fxml"));
        quizPane = quizRectLoader.load();
        quizPane.setVisible(false);
        this.getChildren().add(quizPane);
        quizPane.setLayoutX(5);
        quizPane.setLayoutY(340);
    }


    public Button getEndTurnButton() {
        return endTurnButton;
    }
    public ImageView getMovePawnButton() {
        return throwDice;
    }

    public void initBuyHousePane() throws IOException {
        FXMLLoader buyHousePaneLoader = new FXMLLoader(Menu.class.getResource("buy_house_rect.fxml"));
        buyHousePane = buyHousePaneLoader.load();
        buyHousePane.setVisible(false);
        buyHousePane.setLayoutX(5);
        buyHousePane.setLayoutY(340);

        buyHousePane.getChildren().add(buyQuestion);
        buyHousePane.getChildren().add(doBuyHouse);
        buyHousePane.getChildren().add(doNotBuyHouse);
        this.getChildren().add(buyHousePane);
    }

    public AnchorPane getBuyHousePane() {
        return buyHousePane;
    }

    public AnchorPane getQuizPane() {
        return quizPane;
    }

    public void displayPunishmentInfo(String payerName, String payeeName, int cost, String yourName, int field) {
        if (yourName.equals(payerName)){
            info.setText("Płacisz graczowi " + payeeName + " " + cost + " ECTS za wejście na: " + board.getFields().get(field).getName());
        } else if (yourName.equals(payeeName)){
            info.setText(payerName + " płaci ci " + cost + " ECTS za wejście na: " + board.getFields().get(field).getName());
        } else {
            info.setText(payerName + " płaci graczowi " + payeeName + " " + cost + " ECTS za wejście na: " + board.getFields().get(field).getName());
        }
    }

    public void displayInnovationInfo(String payeeName, int cost, String yourName) {
        if (yourName.equals(payeeName)){
            if(cost < 0){
                info.setText("Tracisz " + Math.abs(cost) + " ECTS za wejście na pole Innowacja");
            }
            else{
                info.setText("Dostajesz " + cost + " ECTS za wejście na pole Innowacja");
            }
        }
        else {
            if(cost < 0){
                info.setText(payeeName + " traci " + Math.abs(cost) + " ECTS za wejście na pole Innowacja");
            }
            else{
                info.setText(payeeName + " dostaje " + cost + " ECTS za wejście na pole Innowacja");
            }

        }
    }


    public AnchorPane getPunishmentInfoPane() {
        return punishmentInfoPane;
    }

    public void hidePunishmentInfo() {
        Platform.runLater(() -> {
            punishmentInfoPane.setVisible(false);
            punishmentInfoPane.requestLayout();
            System.out.println("Weszlo");
        });
    }
}
