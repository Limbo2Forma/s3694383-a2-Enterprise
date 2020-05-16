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

    public List<DeliveryNote> getAllDeliveryNotes(int page){
        return deliveryNoteService.getAllDeliveryNotes(page);
    }

    public DeliveryNote getDeliveryNoteById(int deliveryNoteId){
        return deliveryNoteService.getDeliveryNoteById(deliveryNoteId);
    }

    public int addDeliveryNote(DeliveryNote deliveryNote) throws Exception{
        return deliveryNoteService.addDeliveryNote(deliveryNote);
    }

    public void updateDeliveryNote(DeliveryNote deliveryNote){
        deliveryNoteService.updateDeliveryNote(deliveryNote);
    }

    public void deleteDeliveryNote(int deliveryNoteId){
        deliveryNoteService.deleteDeliveryNote(deliveryNoteId);
    }

    public List<DeliveryNote> getDeliveryNotesByDate(Date date,int page){
        return deliveryNoteService.getDeliveryNotesByDate(date, page);
    }

    public List<DeliveryNote> getDeliveryNotesFromTo(Date fromDate, Date toDate,int page){
        return deliveryNoteService.getDeliveryNotesFromTo(fromDate, toDate, page);
    }

    public List<DeliveryNoteDetail> getDeliveryNoteDetailByDeliveryNoteId(int deliveryNoteId){
        return deliveryNoteService.getDeliveryNoteDetailByDeliveryNoteId(deliveryNoteId);
    }

    public int addDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail) throws Exception{
        return deliveryNoteService.addDeliveryNoteDetail(deliveryNoteDetail);
    }

    public void updateDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail) throws Exception{
        deliveryNoteService.updateDeliveryNoteDetail(deliveryNoteDetail);
    }

    public int deleteDeliveryNoteDetail(int deliveryNoteDetailId){
        return deliveryNoteService.deleteDeliveryNoteDetail(deliveryNoteDetailId);
    }
}
