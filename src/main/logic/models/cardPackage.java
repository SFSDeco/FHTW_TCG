package main.logic.models;

import java.util.ArrayList;

public class cardPackage {
    private final ArrayList<Card> cardPackage = new ArrayList<>(5);
    private int packageID;

    public cardPackage() {
    }

    public ArrayList<Card> getCardPackage() {
        return cardPackage;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public boolean addCard(Card c){
        if(cardPackage.size() < 5){
            cardPackage.add(c);
            return true;
        }
        else{
            System.err.println("Tried to add Card to full package");
            return false;
        }
    }

    public void addCards(ArrayList<Card> cards){
        for(Card c : cards){
            boolean resolve = this.addCard(c);

            //if addCard doesn't resolve correctly, end the method
            if(!resolve){
                return;
            }
        }
    }

    @Override
    public String toString() {
        return "cardPackage{" +
                "cardPackage=" + cardPackage +
                '}';
    }
}