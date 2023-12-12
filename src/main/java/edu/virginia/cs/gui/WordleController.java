package edu.virginia.cs.gui;

import edu.virginia.cs.wordle.*;
import javafx.animation.PauseTransition;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import javafx.util.Duration;


public class WordleController {
    private Wordle wordle;
    private String answer;
    private ArrayList<Character> guess = new ArrayList<>();
    private int row;
    private int column;
    private TextField[][] wordleGrid;
    @FXML
    private TextField letter00;
    @FXML
    private TextField letter10;
    @FXML
    private TextField letter20;
    @FXML
    private TextField letter30;
    @FXML
    private TextField letter40;
    @FXML
    private TextField letter01;
    @FXML
    private TextField letter11;
    @FXML
    private TextField letter21;
    @FXML
    private TextField letter31;
    @FXML
    private TextField letter41;
    @FXML
    private TextField letter02;
    @FXML
    private TextField letter12;
    @FXML
    private TextField letter22;
    @FXML
    private TextField letter32;
    @FXML
    private TextField letter42;
    @FXML
    private TextField letter03;
    @FXML
    private TextField letter13;
    @FXML
    private TextField letter23;
    @FXML
    private TextField letter33;
    @FXML
    private TextField letter43;
    @FXML
    private TextField letter04;
    @FXML
    private TextField letter14;
    @FXML
    private TextField letter24;
    @FXML
    private TextField letter34;
    @FXML
    private TextField letter44;
    @FXML
    private TextField letter05;
    @FXML
    private TextField letter15;
    @FXML
    private TextField letter25;
    @FXML
    private TextField letter35;
    @FXML
    private TextField letter45;
    @FXML
    private Button errorPrompt;
    @FXML
    private PauseTransition errorPause;
    @FXML
    private HBox barYesOrNo;
    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;

    public void initialize() {
        wordle = new WordleImplementation();
        answer = wordle.getAnswer();
        wordleGrid = new TextField[][]{
                {letter00, letter10, letter20, letter30, letter40},
                {letter01, letter11, letter21, letter31, letter41},
                {letter02, letter12, letter22, letter32, letter42},
                {letter03, letter13, letter23, letter33, letter43},
                {letter04, letter14, letter24, letter34, letter44},
                {letter05, letter15, letter25, letter35, letter45},
        };
        errorPause = new PauseTransition(Duration.seconds(1.1));
        errorPause.setOnFinished( event ->
                errorPrompt.setVisible(false)
        );
    }
    private void addLetter(String letter) {
        if (wordle.isWin()){
        } //idk if this is a cheese but it works , you can't type after u win
        else if ( letter.charAt(0) >= 'A' && letter.charAt(0) <= 'Z' && column < 5) {
            wordleGrid[row][column].setText(letter);
            guess.add(letter.charAt(0));
            column++;
        }
    }
    private void removeLetter() {
        if (column != 0) {
            column--;
            wordleGrid[row][column].setText("");
            guess.remove(column);
        }
    }
    @FXML
    private void onTextEntry(KeyEvent event) {
        String l = event.getCode().getChar();
        addLetter(l);
        if (event.getCode().equals(KeyCode.BACK_SPACE)) {
            removeLetter();
        }
        else if (event.getCode().equals(KeyCode.ENTER)){
            String ans = getAns();
//            errorPrompt.setText(wordle.getAnswer()); for testing to see the word
//            errorPrompt.setVisible(true);
            if (guess.size() < 5) {
                errorPrompt.setText("Not enough letters");
                errorPrompt.setVisible(true);
                errorPause.play();
            }
            else {
                LetterResult[] correctness = submitAnswer(ans);
                if (correctness != null) {
                    tileColor(correctness);
                    nextRow();
                    if(wordle.isWin()){
                        errorPrompt.setText("WINNER WINNER CHICKEN DINNER! Would you like to play again?");
                        errorPrompt.setVisible(true);
                        wordle.isGameOver();
                        barYesOrNo.setVisible(true);
                    }
                    else if(wordle.isLoss()) {
                        errorPrompt.setText("You lost! The word is " + wordle.getAnswer() +
                                ". Would you like to play again?");
                        errorPrompt.setVisible(true);
                        barYesOrNo.setVisible(true);
                    }
                }
            }
        }
    }
    private void tileColor(LetterResult[] correctness) {
        for (int i = 0; i < guess.size(); i++) {
            // color hex obtained from Wordle website
            if (correctness[i] == LetterResult.GREEN) {
                wordleGrid[row][i].setStyle("-fx-text-fill: white; -fx-background-color: #6aaa64");
            }
            else if (correctness[i] == LetterResult.YELLOW) {
                wordleGrid[row][i].setStyle("-fx-text-fill: white; -fx-background-color: #c9b458");
            }
            else {
                wordleGrid[row][i].setStyle("-fx-text-fill: white; -fx-background-color: #787c7e");
            }
        }
    }
    private void nextRow() {
        this.row++;
        this.column = 0;
        this.guess.clear();
    }
    private String getAns() {
        String ans = "";
        for (int i = 0; i < guess.size(); i++) {
            ans = ans + guess.get(i);
        }
        return ans;
    }
    private LetterResult[] submitAnswer(String answer){
        try{
            return wordle.submitGuess(answer); //their answers are right or wrong
        }
        catch(IllegalWordException word) { //answer is not real or too short (<5 letters)
            errorPrompt.setText("Not in word list");
            errorPrompt.setVisible(true);
            errorPause.play();
            //stackoverflow.com/questions/29487645/how-to-make-a-label-visible-for-a-certain-time-and-then-should-be-invisible-with
            //how to make a prompt temporarily show up
        }
        return null;
    }
    @FXML
    private void playAgain() {
        newGame();
    }
    @FXML
    private void exitGame() {
        System.exit(0);
    }

    private void newGame(){
        this.guess.clear();
        barYesOrNo.setVisible(false);
        errorPrompt.setVisible(false); //remove prompts and bars
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5 ; j++){
                this.wordleGrid[i][j].setStyle("-fx-border-color: black ; -fx-background-color: white");
                this.wordleGrid[i][j].setText("");
            }
        }
        initialize();
        this.row = 0;
        this.column = 0;
    }
}
