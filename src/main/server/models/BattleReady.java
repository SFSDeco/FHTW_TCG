package main.server.models;

import main.logic.models.Battle;
import main.logic.models.User;

public class BattleReady {
    private User waitingUser = null;
    private boolean waiting = false;

    private Battle battleArena;

    public BattleReady(){}

    public User getWaitingUser() {
        return waitingUser;
    }

    public void setWaitingUser(User waitingUser) {
        this.waitingUser = waitingUser;
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public Battle getBattle() {
        return battleArena;
    }

    public void setBattle(Battle battleArena) {
        this.battleArena = battleArena;
    }

    @Override
    public String toString() {
        return "BattleReady{" +
                "waitingUser=" + waitingUser +
                ", waiting=" + waiting +
                ", battleArena=" + battleArena +
                '}';
    }
}
