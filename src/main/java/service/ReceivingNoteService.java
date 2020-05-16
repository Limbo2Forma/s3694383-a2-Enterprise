package service;

import config.GlobalVar;
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

    public List<ReceivingNote> getAllReceivingNotes(int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from ReceivingNote");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public ReceivingNote getReceivingNoteById(int receivingNoteId){
        return sessionFactory.getCurrentSession().get(ReceivingNote.class, receivingNoteId);
    }

    public int addReceivingNoteWithImportedOrder(ReceivingNote receivingNote, int importedOrderId){
        ReceivingNote temp = new ReceivingNote(receivingNote.getDate(),receivingNote.getStaff());
        temp.setProviderOrder(sessionFactory.getCurrentSession().get(ProviderOrder.class, importedOrderId));

        for (ReceivingNoteDetail detail : temp.getReceivingNoteDetails()) detail.setReceivingNote(temp);

        sessionFactory.getCurrentSession().save(temp);
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

    public List<ReceivingNote> getReceivingNotesByDate(Date date, int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from ReceivingNote as p where p.date = :date");
        query.setParameter("date",date);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<ReceivingNote> getReceivingNotesFromTo(Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from ReceivingNote as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }
}
