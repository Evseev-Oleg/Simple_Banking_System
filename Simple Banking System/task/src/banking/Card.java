package banking;

import java.util.Objects;

public class Card {
    private final String numberCard;
    private final String pin;
    private int balance;

    public Card(String numberCard, String pin) {
        this.numberCard = numberCard;
        this.pin = pin;
        this.balance = 0;
    }

    public String getNumberCard() {
        return numberCard;
    }

    public String getPin() {
        return pin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(numberCard, card.numberCard) &&
                Objects.equals(pin, card.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberCard, pin);
    }
}
