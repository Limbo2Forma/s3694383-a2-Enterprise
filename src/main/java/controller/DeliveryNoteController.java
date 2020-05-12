package controller;

import config.GlobalVar;
import model.DeliveryNoteDetail;
import model.DeliveryNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.DeliveryNoteStore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/deliveryNotes")
public class DeliveryNoteController {
    private DeliveryNoteStore deliveryNoteStore;
    @Autowired
    public void setDeliveryNoteStore(DeliveryNoteStore deliveryNoteStore) { this.deliveryNoteStore = deliveryNoteStore; }

    @GetMapping(path="/p={page}")
    public List<DeliveryNote> getAllDeliveryNotes(@PathVariable int page) { return deliveryNoteStore.getAllDeliveryNotes(page); }

    @GetMapping(path = "/{deliveryNoteId}")
    public DeliveryNote getDeliveryNoteById(@PathVariable int deliveryNoteId){ return deliveryNoteStore.getDeliveryNoteById(deliveryNoteId); }

    @PostMapping(path = "")
    public int addDeliveryNote(@RequestBody DeliveryNote deliveryNote){ return deliveryNoteStore.addDeliveryNote(deliveryNote); }

    @DeleteMapping(path = "/{deliveryNoteId}")
    public String deleteDeliveryNote(@PathVariable int deliveryNoteId){
        deliveryNoteStore.deleteDeliveryNote(deliveryNoteId);
        return "deleted Delivery Note and its details with id: " + deliveryNoteId;
    }
    @PutMapping(path = "")
    public String updateDeliveryNote(@RequestBody DeliveryNote deliveryNote){
        deliveryNoteStore.updateDeliveryNote(deliveryNote);
        return "deleted Delivery Note and its details with id: " + deliveryNote.getId();
    }
    @GetMapping(path = "/date={date}/p={page}")
    public List<DeliveryNote> getDeliveryNoteByDate(
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String date,
            @PathVariable int page){
        try {
            Date date_temp = GlobalVar.dateFormatter.parse(date);
            return deliveryNoteStore.getDeliveryNotesByDate(date_temp, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    @GetMapping(path = "/from={from}/to={to}/p={page}")
    public List<DeliveryNote> getDeliveryNoteFromTo(
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return deliveryNoteStore.getDeliveryNotesFromTo(dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/detail/{deliveryNoteId}")
    public List<DeliveryNoteDetail> getDeliveryNoteDetailByDeliveryNoteId(@PathVariable  int deliveryNoteId){
        return deliveryNoteStore.getDeliveryNoteDetailByDeliveryNoteId(deliveryNoteId);
    }

    @PostMapping(path = "/detail")
    public int addDeliveryNoteDetail(@RequestBody DeliveryNoteDetail providerDeliveryNoteDetail){
        return deliveryNoteStore.addDeliveryNoteDetail(providerDeliveryNoteDetail);
    }

    @PutMapping(path = "/detail")
    public String updateDeliveryNoteDetail(@RequestBody DeliveryNoteDetail providerDeliveryNoteDetail){
        deliveryNoteStore.updateDeliveryNoteDetail(providerDeliveryNoteDetail);
        return "updated Delivery Note Detail with id: " + providerDeliveryNoteDetail.getId()
                + " from Delivery Note with id: " + providerDeliveryNoteDetail.getDeliveryNote().getId();
    }

    @DeleteMapping(path = "/detail/{deliveryNoteDetailId}")
    public String deleteDeliveryNoteDetail(@PathVariable int deliveryNoteDetailId){
        int detailFrom = deliveryNoteStore.deleteDeliveryNoteDetail(deliveryNoteDetailId);
        return "updated Delivery Note Detail with id: " + deliveryNoteDetailId
                + " from Delivery Note with id: " + detailFrom;
    }
}
