package ru.otus.crm.model;

import javax.persistence.*;

@Table(name = "address")
@Entity
public class Address implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "any_street")
    private String anyStreet;

//    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Client client;

    public Address() {
    }

    public Address(Long id, String anyStreet) {
        this.id = id;
        this.anyStreet = anyStreet;
    }

    @Override
    protected Address clone() throws CloneNotSupportedException {
        return new Address(id, anyStreet);
    }
}
