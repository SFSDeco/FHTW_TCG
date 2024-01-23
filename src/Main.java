import main.logic.models.*;
import main.server.DBConnect.dbCommunication;
import main.server.HTTPServer.Server;

import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        new Server().start();

        /*

        //###Testing GameLogic variables
        Card Ogre = new Card("Fire Ogre", 10, "monster", "fire");
        Card Kitty = new Card("Kitty Cat", 1, "monster", "normal");
        User newUser = new User("Name");
        Deck userDeck = new Deck(new Vector<Card>());
        userDeck.addCard(Ogre);
        userDeck.addCard(Kitty);
        newUser.setCardDeck(userDeck);

        System.out.println(newUser);
        System.out.println(Ogre);
        System.out.println(Kitty);
        System.out.println(userDeck);

        //###Testing Battle Mechanics
        Card Rat = new Card("Rat", 2, "monster", "normal", "rat");
        Card FireSpell = new Card("Fireball", 1.5, "spell", "fire", "none");
        Card Kraken = new Card("Cute Kraken", 3, "monster", "water", "kraken");
        Card PowerSpell = new Card("Powerful Spell", 100, "spell", "normal", "none");

        Deck userDeckA = new Deck(new Vector<Card>());
        userDeckA.addCard(Rat);
        userDeckA.addCard(PowerSpell);
        Deck userDeckB = new Deck(new Vector<Card>());
        userDeckB.addCard(FireSpell);
        userDeckB.addCard(Kraken);

        User playerA = new User("Jay");
        User playerB = new User("Flay");
        playerA.setCardDeck(userDeckA);
        playerB.setCardDeck(userDeckB);

        switch(Rat.resolveWinner(FireSpell)){
            case 1:
                System.out.println("Rat won algorithm is wrong");
                break;
            case -1:
                System.out.println("FireSpell won as expected");
                break;
            default:
                System.out.println("Unexpected draw.");
        }
        switch(Kraken.resolveWinner(PowerSpell)){
            case 1:
                System.out.println("Kraken won as expected");
                break;
            case -1:
                System.out.println("Kraken lost, algorithm is wrong");
                break;
            default:
                System.out.println("Unexpected draw.");
        }


        System.out.println(playerA);
        System.out.println(playerB);

        Battle b = new Battle(playerA, playerB);

        b.start();

        System.out.println(playerA);
        System.out.println(playerB);
        */
    }
}