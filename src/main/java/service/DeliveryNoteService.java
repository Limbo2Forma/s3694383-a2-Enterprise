package service;

import config.GlobalVar;
import model.*;
import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class DeliveryNoteService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<DeliveryNote> getAllDeliveryNotes(int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from DeliveryNote");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public DeliveryNote getDeliveryNoteById(int deliveryNoteId){
        return sessionFactory.getCurrentSession().get(DeliveryNote.class, deliveryNoteId);
    }

    private void checkProductQuantity(DeliveryNoteDetail deliveryNoteDetail) throws Exception {
        Product product = sessionFactory.getCurrentSession().get(Product.class, deliveryNoteDetail.getProduct().getId());
        int remainQuantity = product.getCurrentQuantity() - deliveryNoteDetail.getQuantity();
        if (remainQuantity < 0) throw new Exception("Quantity in storage is not enough to deliver");
        product.setCurrentQuantity(remainQuantity);
        sessionFactory.getCurrentSession().update(product);
    }

    public int addDeliveryNote(DeliveryNote deliveryNote) throws Exception {
        for (DeliveryNoteDetail r : deliveryNote.getDeliveryNoteDetails()) {
            checkProductQuantity(r);
            r.setDeliveryNote(deliveryNote);
        }
        sessionFactory.getCurrentSession().save(deliveryNote);
        return deliveryNote.getId();
    }

    public void updateDeliveryNote(DeliveryNote deliveryNote){
        DeliveryNote temp = getDeliveryNoteById(deliveryNote.getId());
        temp.setDate(deliveryNote.getDate());
        temp.setStaff(deliveryNote.getStaff());
        sessionFactory.getCurrentSession().update(temp);
    }

    public void deleteDeliveryNote(int deliveryNoteId){
        DeliveryNote deliveryNote = getDeliveryNoteById(deliveryNoteId);
        Invoice invoice = sessionFactory.getCurrentSession().get(Invoice.class, deliveryNoteId);
        sessionFactory.getCurrentSession().delete(invoice);
        sessionFactory.getCurrentSession().delete(deliveryNote);
    }

    public List<DeliveryNote> getDeliveryNotesByDate(Date date, int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from DeliveryNote as p where p.date = :date");
        query.setParameter("date",date);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<DeliveryNote> getDeliveryNotesFromTo(Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from DeliveryNote as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<DeliveryNoteDetail> getDeliveryNoteDetailByDeliveryNoteId(int deliveryNoteId) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from DeliveryNoteDetail as p where p.deliveryNote.id =:deliveryNoteId");
        query.setParameter("deliveryNoteId",deliveryNoteId);
        return query.list();
    }

    public int addDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail) throws Exception {
        checkProductQuantity(deliveryNoteDetail);

        sessionFactory.getCurrentSession().save(deliveryNoteDetail);
        Invoice invoice = sessionFactory.getCurrentSession().get(Invoice.class, deliveryNoteDetail.getDeliveryNote().getId());
        if (invoice != null){
            InvoiceDetail invoiceDetail = new InvoiceDetail(deliveryNoteDetail);
            invoiceDetail.setInvoice(invoice);
            invoice.getInvoiceDetails().add(invoiceDetail);
            invoice.calculateTotalPrice();
            sessionFactory.getCurrentSession().update(invoice);
        }
        return deliveryNoteDetail.getId();
    }

    public void updateDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail) throws Exception{
        DeliveryNoteDetail note = sessionFactory.getCurrentSession().get(DeliveryNoteDetail.class, deliveryNoteDetail.getId());
        Product product = note.getProduct();
        product.setCurrentQuantity(product.getCurrentQuantity() - note.getQuantity());

        note.setProduct(deliveryNoteDetail.getProduct());
        note.setQuantity(deliveryNoteDetail.getQuantity());
        checkProductQuantity(note);

        sessionFactory.getCurrentSession().update(note);

        InvoiceDetail invoiceDetail = sessionFactory.getCurrentSession().get(InvoiceDetail.class, deliveryNoteDetail.getId());
        if (invoiceDetail != null){
            Invoice updatedInvoice = invoiceDetail.getInvoice();
            invoiceDetail.setDeliveryNoteDetail(note);
            updatedInvoice.calculateTotalPrice();
            sessionFactory.getCurrentSession().update(invoiceDetail);
        }
    }

    public int deleteDeliveryNoteDetail(int deliveryNoteDetailId){
        DeliveryNoteDetail detail = sessionFactory.getCurrentSession().get(DeliveryNoteDetail.class, deliveryNoteDetailId);
        Invoice invoice = sessionFactory.getCurrentSession().get(Invoice.class, detail.getDeliveryNote().getId());
        if (invoice != null) {
            InvoiceDetail invoiceDetail = sessionFactory.getCurrentSession().get(InvoiceDetail.class, deliveryNoteDetailId);
            invoice.getInvoiceDetails().remove(invoiceDetail);
            sessionFactory.getCurrentSession().update(invoice);
        }

        Product product = detail.getProduct();
        product.setCurrentQuantity(product.getCurrentQuantity() + detail.getQuantity());
        sessionFactory.getCurrentSession().update(product);

        DeliveryNote note = detail.getDeliveryNote();
        int detailFrom = note.getId();
        note.getDeliveryNoteDetails().remove(detail);

        sessionFactory.getCurrentSession().update(note);

        return detailFrom;
    }
}
