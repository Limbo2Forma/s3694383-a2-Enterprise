package store;

import model.DeliveryNote;
import model.DeliveryNoteDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.DeliveryNoteService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class DeliveryNoteStore {
    private DeliveryNoteService deliveryNoteService;
    @Autowired
    public void setDeliveryNoteService(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    public List<DeliveryNote> getAllDeliveryNotes(){
        return deliveryNoteService.getAllDeliveryNotes();
    }

    public DeliveryNote getDeliveryNoteById(int deliveryNoteId){
        return deliveryNoteService.getDeliveryNoteById(deliveryNoteId);
    }

    public int addDeliveryNote(DeliveryNote deliveryNote){
        return deliveryNoteService.addDeliveryNote(deliveryNote);
    }

    public void updateDeliveryNote(DeliveryNote deliveryNote){
        deliveryNoteService.updateDeliveryNote(deliveryNote);
    }

    public void deleteDeliveryNote(int deliveryNoteId){
        deliveryNoteService.deleteDeliveryNote(deliveryNoteId);
    }

    public List<DeliveryNote> getDeliveryNotesByDate(Date date){
        return deliveryNoteService.getDeliveryNotesByDate(date);
    }

    public List<DeliveryNote> getDeliveryNotesFromTo(Date fromDate, Date toDate){
        return deliveryNoteService.getDeliveryNotesFromTo(fromDate, toDate);
    }

    public List<DeliveryNoteDetail> getDeliveryNoteDetailByDeliveryNoteId(int deliveryNoteId){
        return deliveryNoteService.getDeliveryNoteDetailByDeliveryNoteId(deliveryNoteId);
    }

    public int addDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail){
        return deliveryNoteService.addDeliveryNoteDetail(deliveryNoteDetail);
    }

    public void updateDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail){
        deliveryNoteService.updateDeliveryNoteDetail(deliveryNoteDetail);
    }

    public void deleteDeliveryNoteDetail(int deliveryNoteDetailId){
        deliveryNoteService.deleteDeliveryNoteDetail(deliveryNoteDetailId);
    }
}
