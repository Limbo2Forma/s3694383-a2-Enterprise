package model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class DeliveryNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @PastOrPresent
    @NotNull
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Staff staff;

    @OneToMany(mappedBy = "deliveryNote", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private List<DeliveryNoteDetail> deliveryNoteDetails = new ArrayList<>();

    public DeliveryNote() { }

    public DeliveryNote(int id,@NotNull @PastOrPresent Date date, @NotNull Staff staff, List<DeliveryNoteDetail> deliveryNoteDetails) {
        this.id = id;
        this.date = date;
        this.staff = staff;
        this.deliveryNoteDetails = deliveryNoteDetails;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public Staff getStaff() { return staff; }

    public void setStaff(Staff staff) { this.staff = staff; }

    public List<DeliveryNoteDetail> getDeliveryNoteDetails() { return deliveryNoteDetails; }
}
