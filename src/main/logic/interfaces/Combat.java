package main.logic.interfaces;

import main.logic.models.User;

public interface Combat {
    User determineWinner(User playerA, User playerB);
    void playGame(User playerA, User playerB);

}
