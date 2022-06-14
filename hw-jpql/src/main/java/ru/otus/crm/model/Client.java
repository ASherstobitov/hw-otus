package ru.otus.crm.model;


import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "client",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    private List<Phone> phones;


    public Client(Client client) {
        this(client.getId(), client.getName(), client.getAddress().clone(), client.getPhones());
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;

        if (this.phones != null && !this.phones.isEmpty()) {
            this.phones.forEach(phone -> phone.setClient(this));
        }
    }

    public Client(String name) {
        this.id = null;
        this.name = name;
    }

    public Client(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client() {

    }


    @Override
    public Client clone() {
            Client client = null;
        try {
            client = (Client)super.clone();
        } catch (CloneNotSupportedException e) {
            client = new Client(this.id, this.name);
        }

        if (this.address != null) {
            client.address = this.address.clone();
        }

        client.phones  = Optional.ofNullable(this.phones)
                .stream()
                .flatMap(List::stream)
                .map(Phone::clone)
                .toList();


        return client;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

        Optional.ofNullable(this.phones)
                .stream()
                .flatMap(List::stream)
                .forEach(e -> e.setClient(this));
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;

        Client client = (Client) o;

        if (getId() != null ? !getId().equals(client.getId()) : client.getId() != null) return false;
        return getName() != null ? getName().equals(client.getName()) : client.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
