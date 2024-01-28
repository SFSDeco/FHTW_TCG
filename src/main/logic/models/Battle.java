package main.logic.models;

import main.logic.interfaces.Combat;

import java.util.Random;

public class Battle implements Combat {

    private final User playerA, playerB;
    private User winner = null;
    //maybe not needed? private User loser = null;
    private int maxGames = 100;
    private int gamesPlayed = 0;
    private String crudeLog = "";


    public Battle(User playerA, User playerB){
        this.playerA = playerA;
        this.playerB = playerB;
    }

    public Battle(User playerA, User playerB, int maxGames) {
        this.playerA = playerA;
        this.playerB = playerB;
        this.maxGames = maxGames;
    }

    public void start(){
        crudeLog = "\nThe game between " + playerA.getUserName() + " and " + playerB.getUserName() + " begins.";

        while(winner == null && gamesPlayed < maxGames){
            resolveRound(playerA, playerB);
            winner = determineWinner(playerA, playerB);
        }

        updatePlayerScores();

        System.out.println("Player A new Score: " + playerA.getScore() + "\nPlayer B new Score: " + playerB.getScore());
    }

    @Override
    public void resolveRound(User playerA, User playerB){
        Random random = new Random();

        //get the respective player deck
        Deck playerDeckA = playerA.getCardDeck();
        Deck playerDeckB = playerB.getCardDeck();
        if(playerDeckA.isEmpty() || playerDeckB.isEmpty()) {
            gamesPlayed = maxGames;
            return;
        }
        int randomCardA = random.nextInt(playerDeckA.size());
        int randomCardB = random.nextInt(playerDeckB.size());

        Card cardToPlayA = playerDeckA.getCardFromIndex(randomCardA);
        Card cardToPlayB = playerDeckB.getCardFromIndex(randomCardB);

        crudeLog += "\nRound Number " + (gamesPlayed+1) + "\n Player " + playerA.getUserName() + " plays: " + cardToPlayA.getName();
        crudeLog += "\n Player " + playerB.getUserName() + " plays: " + cardToPlayB.getName();

        crudeLog += "\n" + cardToPlayA.getName() + " deals " + (cardToPlayA.getDamage() * cardToPlayA.calculateSpell(cardToPlayB.getElement()) * cardToPlayA.resolveSpecial(cardToPlayB)) + " damage.";
        crudeLog += "\n" + cardToPlayB.getName() + " deals " + (cardToPlayB.getDamage() * cardToPlayB.calculateSpell(cardToPlayA.getElement()) * cardToPlayB.resolveSpecial(cardToPlayA)) + " damage.";


        switch(cardToPlayA.resolveWinner(cardToPlayB)){
            case 1:
                crudeLog += "\n Player " + playerA.getUserName() + " won the round!\n";
                //add/remove cards from their respective copies of the deck, set the player decks
                playerDeckA.addCard(cardToPlayB);
                playerA.setCardDeck(playerDeckA);
                playerDeckB.removeCard(cardToPlayB);
                playerB.setCardDeck(playerDeckB);
                gamesPlayed++;
                return;
            case -1:
                crudeLog += "\n Player " + playerB.getUserName() + " won the round!\n";
                playerDeckA.removeCard(cardToPlayA);
                playerA.setCardDeck(playerDeckA);
                playerDeckB.addCard(cardToPlayA);
                playerB.setCardDeck(playerDeckB);
                gamesPlayed++;
                return;
            default:
                crudeLog += "\nThe Round ended in a draw...\n";
                gamesPlayed++;
        }
    }

    @Override
    public User determineWinner(User playerA, User playerB) {
        if(!(gamesPlayed > 0) || (playerA.getCardDeck().isEmpty() && playerB.getCardDeck().isEmpty())){
            return null;
        }

        if(playerA.getCardDeck().isEmpty()){
            return playerB;
        }
        else if(playerB.getCardDeck().isEmpty()){
            return playerA;
        }
        return null;
    }

    public String outcomeString(){
        if(winner == null){
            return crudeLog + "\nThe game ended in a draw!";
        }
        else{
            return crudeLog + "\nThe winner is: " + winner.getUserName();
        }
    }

    public void updatePlayerScores(){
        int scoreA = playerA.getScore(), scoreB = playerB.getScore();


        if(winner == null) return;

        else if(winner == playerA) {
            playerA.setScore(scoreCalc(true, scoreA, scoreB));
            playerB.setScore(scoreCalc(false, scoreB, scoreA));
        }

        else{

            playerA.setScore(scoreCalc(false, scoreA, scoreB));
            playerB.setScore(scoreCalc(true, scoreB, scoreA));
        }
    }

    public int scoreCalc(boolean won, int scoreSelf, int scoreOpponent){
        //Most basic MMR calculation
        //Score + Win/Loss * ( Opponent / Player )
        //can't be beneath the minimum of 5
        int minimum = 5;
        double change = 10;
        double score = scoreSelf;
        double Modifier = (double) scoreOpponent / scoreSelf, reverseModifier = (double) scoreSelf / scoreOpponent;



        if(won){
            score = Math.round(score + change * Modifier);
        }
        else{
            score = Math.round(score - change * reverseModifier);
        }


        return (int) Math.max(score, minimum);
    }

    public User getPlayerA() {
        return playerA;
    }

    public User getPlayerB() {
        return playerB;
    }
}
