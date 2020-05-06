package service;

import model.*;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ReceivingNoteService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ReceivingNote> getAllReceivingNotes(){
        return sessionFactory.getCurrentSession().createQuery("from ReceivingNote").list();
    }

    public ReceivingNote getReceivingNoteById(int receivingNoteId){
        return sessionFactory.getCurrentSession().get(ReceivingNote.class, receivingNoteId);
    }

    public int addReceivingNoteWithImportedOrder(ReceivingNote receivingNote, int importedOrderId){
        List<ProviderOrderDetail> orderDetails = sessionFactory.getCurrentSession()
                .get(ProviderOrder.class, importedOrderId).getProviderOrderDetails();
        List<ReceivingNoteDetail> noteDetailList = new ArrayList<>();

        for (ProviderOrderDetail od : orderDetails) {
            ReceivingNoteDetail r = new ReceivingNoteDetail();
            r.setProduct(od.getProduct());
            r.setQuantity(od.getQuantity());
            r.setId(od.getId());
            r.setReceivingNote(receivingNote);
            noteDetailList.add(r);
        }

        receivingNote.setReceivingNoteDetails(noteDetailList);
        receivingNote.setId(importedOrderId);

        sessionFactory.getCurrentSession().save(receivingNote);
        return receivingNote.getId();
    }

    public void updateReceivingNote(ReceivingNote receivingNote){
        ReceivingNote temp = getReceivingNoteById(receivingNote.getId());
        temp.setDate(receivingNote.getDate());
        temp.setStaff(receivingNote.getStaff());
        sessionFactory.getCurrentSession().update(receivingNote);
    }

    public void deleteReceivingNote(int receivingNoteId){
        ReceivingNote receivingNote = getReceivingNoteById(receivingNoteId);
        sessionFactory.getCurrentSession().delete(receivingNote);
    }

    public List<ReceivingNote> getReceivingNotesByDate(Date date){
        Query query = sessionFactory.getCurrentSession().createQuery("from ReceivingNote as p where p.date = :date");
        query.setParameter("date",date);
        return query.list();
    }

    public List<ReceivingNote> getReceivingNotesFromTo(Date fromDate, Date toDate){
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from ReceivingNote as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return query.list();
    }
}
