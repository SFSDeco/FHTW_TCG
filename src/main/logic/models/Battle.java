package main.logic.models;

import main.logic.interfaces.Combat;

public class Battle implements Combat {

    private final User playerA, playerB;
    private User winner = null;
    private User loser = null;
    private int maxGames = 10;
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

        }
    }

    @Override
    public void playGame(User playerA, User playerB){

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
