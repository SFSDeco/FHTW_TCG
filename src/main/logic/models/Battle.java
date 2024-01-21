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
    }

    @Override
    public void resolveRound(User playerA, User playerB){
        Random random = new Random();

        //get the respective player deck
        Deck playerDeckA = playerA.getCardDeck();
        Deck playerDeckB = playerB.getCardDeck();
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
                return;
            case -1:
                playerDeckA.removeCard(cardToPlayA);
                playerA.setCardDeck(playerDeckA);
                playerDeckB.addCard(cardToPlayA);
                playerB.setCardDeck(playerDeckB);
                return;
            default:
                return;
        }
    }

    @Override
    public User determineWinner(User playerA, User playerB) {
        if(playerA.getCardDeck().isEmpty()){
            return playerB;
        }
        else if(playerB.getCardDeck().isEmpty()){
            return playerA;
        }
        return null;
    }
}
