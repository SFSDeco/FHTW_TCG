import main.logic.models.*;
import main.server.HTTPServer.Server;

import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        new Server().start();



        /*
        ##Testing GameLogic variables
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

        */
    }
}