package controller;

import config.GlobalVar;
import model.ReceivingNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.ReceivingNoteStore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/receivingNotes")
public class ReceivingNoteController {
    private ReceivingNoteStore receivingNoteStore;
    @Autowired
    public void setReceivingNoteStore(ReceivingNoteStore receivingNoteStore) {
        this.receivingNoteStore = receivingNoteStore;
    }

    @GetMapping(path="/p={page}")
    public List<ReceivingNote> getAllReceivingNotes(@PathVariable int page) {
        return receivingNoteStore.getAllReceivingNotes(page);
    }

    @GetMapping(path = "/{receivingNoteId}")
    public ReceivingNote getReceivingNoteById(@PathVariable int receivingNoteId){ return receivingNoteStore.getReceivingNoteById(receivingNoteId); }

    @PostMapping(path = "/{importedOrderId}")
    public int addReceivingNoteImportedOrder(@RequestBody ReceivingNote receivingNote, @PathVariable int importedOrderId) {
        return receivingNoteStore.addReceivingNoteWithImportedOrder(receivingNote, importedOrderId);
    }
    @DeleteMapping(path = "/{receivingNoteId}")
    public String deleteReceivingNote(@PathVariable int receivingNoteId){
        receivingNoteStore.deleteReceivingNote(receivingNoteId);
        return "deleted Receiving Note and its details with id: " + receivingNoteId;
    }
    @PutMapping(path = "")
    public String updateReceivingNote(@RequestBody ReceivingNote receivingNote){
        receivingNoteStore.updateReceivingNote(receivingNote);
        return "updated Receiving Note and its details with id: " + receivingNote.getId();
    }
    @GetMapping(path = "/date={date}/p={page}")
    public List<ReceivingNote> getReceivingNoteByDate(@PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String date,
                                                      @PathVariable int page){
        try {
            Date date_temp = GlobalVar.dateFormatter.parse(date);
            return receivingNoteStore.getReceivingNotesByDate(date_temp, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    @GetMapping(path = "/from={from}/to={to}/p={page}")
    public List<ReceivingNote> getReceivingNoteFromTo(
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return receivingNoteStore.getReceivingNotesFromTo(dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
