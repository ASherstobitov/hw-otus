package ru.atm;

public class Banknote {

    private Integer note;

    private String currency;

    public Banknote(Integer note, String currency) {
        this.note = note;
        this.currency = currency;
    }

    private Banknote(Builder banknote) {
        this.note = banknote.note;
        this.currency = banknote.currency;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public static class Builder {

        private Integer note;

        private String currency;

        public Builder withNote(Integer note) {
            this.note = note;
            return this;
        }

        public Builder withCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Banknote build() {
            return new Banknote(this);
        }

    }

    @Override
    public String toString() {
        return "Banknote{" +
                "note=" + note +
                ", currency='" + currency + '\'' +
                '}';
    }
}
