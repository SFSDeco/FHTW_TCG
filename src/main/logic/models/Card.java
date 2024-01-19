package main.logic.models;

public class Card {
    private String name;
    private int dmg;
    private String cardType;
    private String element;
    private String specialty;

    public Card(String name, int dmg, String cardType, String element, String specialty) {
        this.name = name;
        this.dmg = dmg;
        this.cardType = cardType;
        this.element = element;
        this.specialty = specialty;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", dmg=" + dmg +
                ", cardType='" + cardType + '\'' +
                ", element='" + element + '\'' +
                '}';
    }

}
