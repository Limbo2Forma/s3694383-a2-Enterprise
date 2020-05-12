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

    public List<ReceivingNote> getAllReceivingNotes(int page){
        return receivingNoteService.getAllReceivingNotes(page);
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

    public List<ReceivingNote> getReceivingNotesByDate(Date date, int page){
        return receivingNoteService.getReceivingNotesByDate(date, page);
    }

    public List<ReceivingNote> getReceivingNotesFromTo(Date fromDate, Date toDate, int page){
        return receivingNoteService.getReceivingNotesFromTo(fromDate, toDate, page);
    }
}
