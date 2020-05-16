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
public class ProviderOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
    @PastOrPresent
    @NotNull
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Staff staff;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Provider provider;

    @OneToMany(mappedBy = "providerOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private List<ProviderOrderDetail> providerOrderDetails = new ArrayList<>();

    public ProviderOrder() { }

    public ProviderOrder(int id, @PastOrPresent @NotNull Date date, @NotNull Staff staff, @NotNull Provider provider, List<ProviderOrderDetail> providerOrderDetails) {
        this.id = id;
        this.date = date;
        this.staff = staff;
        this.provider = provider;
        this.providerOrderDetails = providerOrderDetails;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public Staff getStaff() { return staff; }

    public void setStaff(Staff staff) { this.staff = staff; }

    public Provider getProvider() { return provider; }

    public void setProvider(Provider provider) { this.provider = provider; }

    public List<ProviderOrderDetail> getProviderOrderDetails() {
        return providerOrderDetails;
    }

    public void setProviderOrderDetails(List<ProviderOrderDetail> providerOrderDetails) { this.providerOrderDetails = providerOrderDetails; }
}
