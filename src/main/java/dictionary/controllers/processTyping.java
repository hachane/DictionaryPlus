package dictionary.controllers;

import dictionary.models.Dao.WordCollectionDao;
import dictionary.models.Entity.Word;
import dictionary.apiservices.TTSService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class processTyping extends process {
    @FXML
    private AnchorPane firstDeck = new AnchorPane();

    @FXML
    private ImageView imgOnDeck = new ImageView();

    @FXML
    private AnchorPane secondDeck = new AnchorPane();

    @FXML
    private Label userAnswer = new Label();

    @FXML
    private TextField userAnswerTextField = new TextField();

    @FXML
    public void initialize(URL location, ResourceBundle Resources) {
        fixedNameCollection = ReviewPlayController.currentCollection;
        current = WordCollectionDao.queryWordInCollection(fixedNameCollection);
        if(ReviewPlayController.currentSortBy.equals("Alphabetically")) {
            WordCollectionDao.sortWord(current);
        } else if (ReviewPlayController.currentSortBy.equals("History")) {

        } else if (ReviewPlayController.currentSortBy.equals("Randomly")) {
            Collections.shuffle(current);
        }
        int tmp = current.size();
        for (int i = 0; i < tmp - ReviewPlayController.numCards; i++) {
            current.remove(tmp - 1 - i);
        }
        idOfDisplayWord = 0;
        fakeNumberOfWordsinCollection = current.size();
        wordsLeft = fakeNumberOfWordsinCollection;
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        firstDeck.setVisible(true);
        secondDeck.setVisible(false);
        showAnswer.setVisible(true);
        transfer.setVisible(false);
        displayWord = current.get(idOfDisplayWord);
        currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
        done.setVisible(false);
        userAnswerTextField.setOnKeyPressed(event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                showAnswer();
            }
        } );
    }
    @FXML
    public void showAnswer() {
        secondDeck.setVisible(true);
        userAnswer.setText(userAnswerTextField.getText());
        infoOfWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
        if(userAnswerTextField.getText().equals(displayWord.getMeaning())) {
            imgOnDeck.setImage(new Image(getClass().getResourceAsStream("/utils/icons/review/correctDeck.png")));
        } else {
            imgOnDeck.setImage(new Image(getClass().getResourceAsStream("/utils/icons/review/incorrectDeck.png")));
        }

        firstDeck.setVisible(false);
        userAnswerTextField.setText("");

        transfer.setVisible(true);
        showAnswer.setVisible(false);
    }
    @FXML
    public void easyButton() {
        try {
            if (!WordCollectionDao.queryWordInCollection(fixedNameCollection).isEmpty()) {
                WordCollectionDao.deleteWordFromCollection(displayWord.getWord(), fixedNameCollection);
                wordsLeft--;
                progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
                current.remove(idOfDisplayWord);
            }
            if (wordsLeft > 0) {
                displayWord = current.get(idOfDisplayWord);
                currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
                secondDeck.setVisible(false);
                firstDeck.setVisible(true);
                transfer.setVisible(false);
                showAnswer.setVisible(true);
            }
            else {
                speaker.setVisible(false);
                currentWord.setVisible(false);
                progress.setVisible(false);
                transfer.setVisible(false);
                showAnswer.setVisible(false);
                done.setVisible(true);
            }
            //currentWord.setText("");
            //infoOfWord.setText("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void hardButton() {
        progress.setText(wordsLeft + "/" + fakeNumberOfWordsinCollection);
        int len = current.size();
        idOfDisplayWord++;
        if(idOfDisplayWord >= len) idOfDisplayWord = 0;
        displayWord = current.get(idOfDisplayWord);
        currentWord.setText(displayWord.getWord() + "\n" + displayWord.getPronunciation());
        secondDeck.setVisible(false);
        firstDeck.setVisible(true);
        transfer.setVisible(false);
        showAnswer.setVisible(true);
    }
}