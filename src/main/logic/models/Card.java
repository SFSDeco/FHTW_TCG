package main.logic.models;

import java.util.Objects;

public class Card {
    private String name;
    private double dmg;
    private String cardType;
    private String element;
    private String specialty;

    public Card(String name, double dmg, String cardType, String element, String specialty) {
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

    public double getDmg() {
        return dmg;
    }

    public void setDmg(double dmg) {
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

    public int resolveWinner(Card opponent){
        double damageSelf, damageOpponent;

        damageSelf = dmg;
        damageOpponent = opponent.getDmg();

        damageSelf = damageSelf * this.calculateSpell(opponent.getElement());
        damageOpponent = damageOpponent * opponent.calculateSpell(this.element);

        //resolve the Specialties
        damageSelf = damageSelf * this.resolveSpecial(opponent);
        damageOpponent = damageOpponent * opponent.resolveSpecial(this);

        //Winner is calling Card
        if(damageSelf > damageOpponent) return 1;
        //Winner is opposing Card
        else if (damageOpponent > damageSelf) return -1;

        //return draw as default
        return 0;
    }

    public double resolveSpecial(Card opposing){
        if( (this.specialty.equals("goblin") && opposing.getSpecialty().equals("dragon")) ||
                (this.specialty.equals("ork") && opposing.getSpecialty().equals("wizard")) ||
                (this.specialty.equals("knight") && (opposing.getElement().equals("water") && opposing.getCardType().equals("spell"))) ||
                (this.cardType.equals("spell") && opposing.getSpecialty().equals("kraken")) ||
                (this.cardType.equals("dragon") && opposing.getSpecialty().equals("fire elf"))
        ) return 0;


        return 1;
    }

    public double calculateSpell(String OppElement){
        String weakness;

        if(!(this.cardType.equals("spell"))){
            return 1;
        }

        if(this.element.equals(OppElement)){
            return 1;
        }
        weakness = switch (this.element) {
            case "water" -> "normal";
            case "fire" -> "water";
            default -> "fire";
        };

        if(OppElement.equals(weakness)){
            return 0.5;
        }

        return 2;
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
