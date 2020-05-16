package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class ReceivingNote {
    @Id
    private int id;

    @OneToOne(cascade=CascadeType.PERSIST, fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    @JoinColumn(name = "id")
    private ProviderOrder providerOrder;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
    @NotNull
    @PastOrPresent
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Staff staff;

    @OneToMany(mappedBy = "receivingNote", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<ReceivingNoteDetail> receivingNoteDetails = new ArrayList<>();

    public ReceivingNote() { }

    public ReceivingNote(@NotNull @PastOrPresent Date date, @NotNull Staff staff) {
        this.date = date;
        this.staff = staff;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public List<ReceivingNoteDetail> getReceivingNoteDetails() {
        return receivingNoteDetails;
    }

    public void setReceivingNoteDetails(List<ReceivingNoteDetail> receivingNoteDetails) {
        this.receivingNoteDetails = receivingNoteDetails;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public Staff getStaff() { return staff; }

    public void setStaff(Staff staff) { this.staff = staff; }

    public void setProviderOrder(ProviderOrder providerOrder) {
        this.providerOrder = providerOrder;
        for (ProviderOrderDetail od : providerOrder.getProviderOrderDetails()) {
            receivingNoteDetails.add(new ReceivingNoteDetail(od));
        }
    }
}
