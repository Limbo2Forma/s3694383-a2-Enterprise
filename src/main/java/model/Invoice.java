package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @PastOrPresent
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Staff staff;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Customer customer;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<InvoiceDetail> invoiceDetails = new ArrayList<>();

    @Column
    @NotNull
    private long totalPrice;

    public Invoice() { }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public List<InvoiceDetail> getInvoiceDetails() { return invoiceDetails; }

    public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) { this.invoiceDetails = invoiceDetails; }

    public Date getDate() { return date; }

    public void setDate(Date date) {this.date = date; }

    public Staff getStaff() { return staff; }

    public void setStaff(Staff staff) { this.staff = staff; }

    public Customer getCustomer() { return customer; }

    public void setCustomer(Customer customer) { this.customer = customer; }

    public long getTotalPrice() { return totalPrice; }

    public void setTotalPrice(long totalPrice) { this.totalPrice = totalPrice; }
}
