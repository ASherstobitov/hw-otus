package ru.hwAtm.banknote;

public class Banknote {

    private Integer note;

    private String currency;

    public Banknote(Integer note, String currency) {
        this.note = note;
        this.currency = currency;
    }

    public Integer getNote() {
        return note;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Banknote)) return false;

        Banknote banknote = (Banknote) o;

        if (getNote() != null ? !getNote().equals(banknote.getNote()) : banknote.getNote() != null) return false;
        return getCurrency() != null ? getCurrency().equals(banknote.getCurrency()) : banknote.getCurrency() == null;
    }

    @Override
    public int hashCode() {
        int result = getNote() != null ? getNote().hashCode() : 0;
        result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "note=" + note +
                ", currency='" + currency + '\'' +
                '}';
    }
}
