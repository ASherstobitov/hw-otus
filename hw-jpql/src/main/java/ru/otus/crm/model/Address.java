package ru.otus.crm.model;

import javax.persistence.*;

@Table(name = "addresses")
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "any_street")
    private String anyStreet;

    public Address() {
    }


    public Address(String anyStreet) {
        this.id = null;
        this.anyStreet = anyStreet;
    }

    public Address(Long id, String anyStreet) {
        this.id = id;
        this.anyStreet = anyStreet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnyStreet() {
        return anyStreet;
    }

    public void setAnyStreet(String anyStreet) {
        this.anyStreet = anyStreet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (getId() != null ? !getId().equals(address.getId()) : address.getId() != null) return false;
        return getAnyStreet() != null ? getAnyStreet().equals(address.getAnyStreet()) : address.getAnyStreet() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getAnyStreet() != null ? getAnyStreet().hashCode() : 0);
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
