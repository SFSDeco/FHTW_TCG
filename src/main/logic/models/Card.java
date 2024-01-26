package main.logic.models;

public class Card {
    private String name;
    private String Id;
    private double dmg;
    private String cardType;
    private String element;
    private String specialty;

    public Card(){}

    public Card(String Id, String name, double dmg, String cardType, String element, String specialty) {
        this.Id = Id;
        this.name = name;
        this.dmg = dmg;
        this.cardType = cardType;
        this.element = element;
        this.specialty = specialty;
    }

    public Card(String name, String id, double dmg, String cardType, String element) {
        this.name = name;
        this.Id = id;
        this.dmg = dmg;
        this.cardType = cardType;
        this.element = element;
        this.specialty = this.getSpecialFromName();
    }

    public Card(String Id, String name, double dmg){
        this.Id = Id;
        this.name = name;
        this.dmg = dmg;
        this.cardType = getTypeFromName();
        this.element = getElementFromName();
        this.specialty = getSpecialFromName();
    }

    public String getId() {
        return Id;
    }

    public void setId(String ID) {
        this.Id = ID;
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

    public double getDamage() {
        return dmg;
    }

    public void setDamage(double dmg) {
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
        damageOpponent = opponent.getDamage();

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
                (this.cardType.equals("dragon") && opposing.getSpecialty().equals("fire elf") ||
                        opposing.getSpecialty().equals("joker"))
        ) return 0;


        return 1;
    }

    public String getElementFromName(){
        String element = "normal";
        String nameCopy = name.toLowerCase();

        if(nameCopy.contains("water")) element = "water";
        else if(nameCopy.contains("fire")) element = "fire";

        return element;
    }

    public String getSpecialFromName(){
        String special = "";
        String nameCopy = name.toLowerCase();

        if(nameCopy.contains("goblin")) special = "goblin";
        else if (nameCopy.contains("dragon")) special = "dragon";
        else if (nameCopy.contains("ork")) special = "ork";
        else if (nameCopy.contains("wizard")) special = "wizard";
        else if (nameCopy.contains("knight")) special = "knight";
        else if (nameCopy.contains("kraken")) special = "kraken";
        else if (nameCopy.contains("fire elf")) special = "fire elf";
        else if (nameCopy.contains("joker")) special = "joker";

        return special;
    }

    public String getTypeFromName(){
        String type = "monster";
        String nameCopy = name.toLowerCase();
        if(nameCopy.contains("spell")) type = "spell";

        return type;
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

    public boolean compareToCard(Card c){
        return this.Id.equals(c.getId());
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", Id='" + Id + '\'' +
                ", dmg=" + dmg +
                ", cardType='" + cardType + '\'' +
                ", element='" + element + '\'' +
                ", specialty='" + specialty + '\'' +
                '}';
    }

}
