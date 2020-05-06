package model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String address;

    @Column
    @NotBlank
    private String phone;

    @Column
    private String fax;

    @Column
    @NotBlank
    @Email
    private String email;

    @Column
    @NotBlank
    private String contact;

    public Provider() { }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getFax() { return fax; }

    public void setFax(String fax) { this.fax = fax; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }

    public void setContact(String contact) { this.contact = contact; }
}
