package store;

import model.ReceivingNote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ReceivingNoteService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ReceivingNoteStore {
    private ReceivingNoteService receivingNoteService;
    @Autowired
    public void setReceivingNoteService(ReceivingNoteService receivingNoteService) {
        this.receivingNoteService = receivingNoteService;
    }

    public List<ReceivingNote> getAllReceivingNotes(){
        return receivingNoteService.getAllReceivingNotes();
    }

    public ReceivingNote getReceivingNoteById(int receivingNoteId){
        return receivingNoteService.getReceivingNoteById(receivingNoteId);
    }

    public int addReceivingNoteWithImportedOrder(ReceivingNote receivingNote, int importedOrderId){
        return receivingNoteService.addReceivingNoteWithImportedOrder(receivingNote, importedOrderId);
    }

    public void updateReceivingNote(ReceivingNote receivingNote){
        receivingNoteService.updateReceivingNote(receivingNote);
    }

    public void deleteReceivingNote(int receivingNoteId){
        receivingNoteService.deleteReceivingNote(receivingNoteId);
    }

    public List<ReceivingNote> getReceivingNotesByDate(Date date){
        return receivingNoteService.getReceivingNotesByDate(date);
    }

    public List<ReceivingNote> getReceivingNotesFromTo(Date fromDate, Date toDate){
        return receivingNoteService.getReceivingNotesFromTo(fromDate, toDate);
    }
}
