package controller;

import model.ReceivingNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.ReceivingNoteStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/receivingNotes")
public class ReceivingNoteController {
    private final String dateFormat = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    private ReceivingNoteStore receivingNoteStore;
    @Autowired
    public void setReceivingNoteStore(ReceivingNoteStore receivingNoteStore) {
        this.receivingNoteStore = receivingNoteStore;
    }

    @GetMapping(path="")
    public List<ReceivingNote> getAllReceivingNotes() {
        return receivingNoteStore.getAllReceivingNotes();
    }

    @GetMapping(path = "/{receivingNoteId}")
    public ReceivingNote getReceivingNoteById(@PathVariable int receivingNoteId){ return receivingNoteStore.getReceivingNoteById(receivingNoteId); }

    @PostMapping(path = "/{importedOrderId}")
    public int addReceivingNoteImportedOrder(@RequestBody ReceivingNote receivingNote, @PathVariable int importedOrderId) {
        return receivingNoteStore.addReceivingNoteWithImportedOrder(receivingNote, importedOrderId);
    }

    @DeleteMapping(path = "/{receivingNoteId}")
    public void deleteReceivingNote(@PathVariable int receivingNoteId){
        receivingNoteStore.deleteReceivingNote(receivingNoteId);
    }
    @PutMapping(path = "")
    public void updateReceivingNote(@RequestBody ReceivingNote receivingNote){
        receivingNoteStore.updateReceivingNote(receivingNote);
    }
    @GetMapping(path = "/date/{date}")
    public List<ReceivingNote> getReceivingNoteByDate(@PathVariable @DateTimeFormat(pattern = dateFormat) String date){
        try {
            Date date_temp = simpleDateFormat.parse(date);
            return receivingNoteStore.getReceivingNotesByDate(date_temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    @GetMapping(path = "/from/{from}/to/{to}")
    public List<ReceivingNote> getReceivingNoteFromTo(
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return receivingNoteStore.getReceivingNotesFromTo(dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
