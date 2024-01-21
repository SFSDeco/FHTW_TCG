package main.logic.interfaces;

import main.logic.models.User;

public interface Combat {
    User determineWinner(User playerA, User playerB);
    void resolveRound(User playerA, User playerB);

}
