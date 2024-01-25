package main.logic.models;

import main.logic.interfaces.Combat;

import java.util.Random;

public class Battle implements Combat {

    private final User playerA, playerB;
    private User winner = null;
    //maybe not needed? private User loser = null;
    private int maxGames = 100;
    private int gamesPlayed = 0;

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

        while(winner == null && gamesPlayed < maxGames){
            resolveRound(playerA, playerB);
            winner = determineWinner(playerA, playerB);
        }

        printWinner();
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

        switch(cardToPlayA.resolveWinner(cardToPlayB)){
            case 1:
                //add/remove cards from their respective copies of the deck, set the player decks
                playerDeckA.addCard(cardToPlayB);
                playerA.setCardDeck(playerDeckA);
                playerDeckB.removeCard(cardToPlayB);
                playerB.setCardDeck(playerDeckB);
                gamesPlayed++;
                return;
            case -1:
                playerDeckA.removeCard(cardToPlayA);
                playerA.setCardDeck(playerDeckA);
                playerDeckB.addCard(cardToPlayA);
                playerB.setCardDeck(playerDeckB);
                gamesPlayed++;
                return;
            default:
                gamesPlayed++;
                return;
        }
    }

    @Override
    public User determineWinner(User playerA, User playerB) {
        if(!(gamesPlayed > 0)){
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

    public void printWinner(){
        if(winner == null){
            System.out.println("The game ended in a draw!");
        }
        else{
            System.out.println("The winner is: " + winner.getUserName());
        }
    }
}
