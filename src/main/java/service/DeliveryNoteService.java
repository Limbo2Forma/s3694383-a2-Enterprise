package service;

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

    public List<DeliveryNote> getAllDeliveryNotes(){
        return sessionFactory.getCurrentSession().createQuery("from DeliveryNote").list();
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

    public int addDeliveryNote(DeliveryNote deliveryNote){
        try {
            for (DeliveryNoteDetail r : deliveryNote.getDeliveryNoteDetails()) {
                checkProductQuantity(r);
                r.setDeliveryNote(deliveryNote);
            }
            sessionFactory.getCurrentSession().save(deliveryNote);
            return deliveryNote.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateDeliveryNote(DeliveryNote deliveryNote){
        DeliveryNote temp = getDeliveryNoteById(deliveryNote.getId());
        temp.setDate(deliveryNote.getDate());
        temp.setStaff(deliveryNote.getStaff());
        sessionFactory.getCurrentSession().update(temp);
    }

    public void deleteDeliveryNote(int deliveryNoteId){
        DeliveryNote deliveryNote = getDeliveryNoteById(deliveryNoteId);
        sessionFactory.getCurrentSession().delete(deliveryNote);
    }

    public List<DeliveryNote> getDeliveryNotesByDate(Date date){
        Query query = sessionFactory.getCurrentSession().createQuery("from DeliveryNote as p where p.date = :date");
        query.setParameter("date",date);
        return query.list();
    }

    public List<DeliveryNote> getDeliveryNotesFromTo(Date fromDate, Date toDate){
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from DeliveryNote as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return query.list();
    }

    public List<DeliveryNoteDetail> getDeliveryNoteDetailByDeliveryNoteId(int deliveryNoteId) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from DeliveryNoteDetail as p where p.deliveryNote.id =:deliveryNoteId");
        query.setParameter("deliveryNoteId",deliveryNoteId);
        return query.list();
    }

    public int addDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail){
        try {
            checkProductQuantity(deliveryNoteDetail);

            sessionFactory.getCurrentSession().save(deliveryNoteDetail);

            Invoice invoice = sessionFactory.getCurrentSession()
                    .get(Invoice.class, deliveryNoteDetail.getDeliveryNote().getId());

            if (invoice != null){

                InvoiceDetail invoiceDetail = new InvoiceDetail();
                int price = deliveryNoteDetail.getProduct().getSinglePrice() * deliveryNoteDetail.getQuantity();
                invoiceDetail.setProduct(deliveryNoteDetail.getProduct());
                invoiceDetail.setQuantity(deliveryNoteDetail.getQuantity());
                invoiceDetail.setId(deliveryNoteDetail.getId());
                invoiceDetail.setPrice(price);
                invoiceDetail.setInvoice(invoice);

                List<InvoiceDetail> detailList = invoice.getInvoiceDetails();
                detailList.add(invoiceDetail);
                invoice.setInvoiceDetails(detailList);
                invoice.setTotalPrice(invoice.getTotalPrice() + price);

                sessionFactory.getCurrentSession().update(invoice);

                return deliveryNoteDetail.getId();
            }
        } catch (Exception e) { e.printStackTrace(); }

        return -1;
    }

    public void updateDeliveryNoteDetail(DeliveryNoteDetail deliveryNoteDetail){
        try {
            DeliveryNoteDetail note = sessionFactory.getCurrentSession()
                    .get(DeliveryNoteDetail.class, deliveryNoteDetail.getId());
            Product product = note.getProduct();
            product.setCurrentQuantity(product.getCurrentQuantity() - note.getQuantity());

            note.setProduct(deliveryNoteDetail.getProduct());
            note.setQuantity(deliveryNoteDetail.getQuantity());
            checkProductQuantity(note);

            sessionFactory.getCurrentSession().update(note);

            InvoiceDetail invoiceDetail = sessionFactory.getCurrentSession()
                    .get(InvoiceDetail.class, deliveryNoteDetail.getId());
            if (invoiceDetail != null){
                int oldPrice = invoiceDetail.getPrice();
                int newPrice = deliveryNoteDetail.getProduct().getSinglePrice() * deliveryNoteDetail.getQuantity();

                invoiceDetail.setProduct(deliveryNoteDetail.getProduct());
                invoiceDetail.setQuantity(deliveryNoteDetail.getQuantity());
                invoiceDetail.setId(deliveryNoteDetail.getId());
                invoiceDetail.setPrice(newPrice);
                Invoice updatedInvoice = invoiceDetail.getInvoice();
                updatedInvoice.setTotalPrice(updatedInvoice.getTotalPrice() + newPrice - oldPrice);

                sessionFactory.getCurrentSession().update(invoiceDetail);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteDeliveryNoteDetail(int deliveryNoteDetailId){

        DeliveryNoteDetail detail = sessionFactory.getCurrentSession().get(DeliveryNoteDetail.class, deliveryNoteDetailId);

        Invoice invoice = sessionFactory.getCurrentSession()
                .get(Invoice.class, detail.getDeliveryNote().getId());

        if (invoice != null) {
            InvoiceDetail invoiceDetail = sessionFactory.getCurrentSession()
                    .get(InvoiceDetail.class, deliveryNoteDetailId);
            List<InvoiceDetail> invoiceDetails = invoice.getInvoiceDetails();

            invoice.setTotalPrice(invoice.getTotalPrice() - invoiceDetail.getPrice());
            invoiceDetails.remove(invoiceDetail);
            invoice.setInvoiceDetails(invoiceDetails);

            sessionFactory.getCurrentSession().update(invoice);
            sessionFactory.getCurrentSession().delete(invoiceDetail);
        }

        Product product = detail.getProduct();
        product.setCurrentQuantity(product.getCurrentQuantity() + detail.getQuantity());
        sessionFactory.getCurrentSession().update(product);

        DeliveryNote note = detail.getDeliveryNote();
        List<DeliveryNoteDetail> updatedList = note.getDeliveryNoteDetails();
        updatedList.remove(detail);
        note.setDeliveryNoteDetails(updatedList);

        sessionFactory.getCurrentSession().update(note);
        sessionFactory.getCurrentSession().delete(detail);
    }
}
