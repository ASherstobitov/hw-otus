package ru.otus.crm.model;

import javax.persistence.*;

@Table(name = "address")
@Entity
public class Address implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "any_street")
    private String anyStreet;

    public Address() {
    }



    public Address(Long id, String anyStreet) {
        this.id = id;
        this.anyStreet = anyStreet;
    }

    public Address(String anyStreet) {
        this.anyStreet = anyStreet;
    }

    @Override
    protected Address clone() {
        Address address = null;
        try {
            return (Address)super.clone();
        } catch (CloneNotSupportedException e) {
            return new Address(this.id, this.anyStreet);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        return anyStreet != null ? anyStreet.equals(address.anyStreet) : address.anyStreet == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (anyStreet != null ? anyStreet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", anyStreet='" + anyStreet + '\'' +
                '}';
    }

}
