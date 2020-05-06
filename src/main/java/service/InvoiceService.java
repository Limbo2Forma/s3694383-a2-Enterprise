package service;

import model.*;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class InvoiceService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Invoice> getAllInvoices(){
        return sessionFactory.getCurrentSession().createQuery("from Invoice").list();
    }

    public Invoice getInvoiceById(int invoiceId){
        return sessionFactory.getCurrentSession().get(Invoice.class, invoiceId);
    }

    public int addInvoiceWithImportedNote(Invoice invoice, int importedNoteId){
        List<DeliveryNoteDetail> noteDetails = sessionFactory.getCurrentSession()
                .get(DeliveryNote.class, importedNoteId).getDeliveryNoteDetails();
        List<InvoiceDetail> invoiceDetailList = new ArrayList<>();

        int totalPrice = 0;

        for (DeliveryNoteDetail nd : noteDetails) {
            InvoiceDetail i = new InvoiceDetail();
            int price = nd.getProduct().getSinglePrice() * nd.getQuantity();
            i.setProduct(nd.getProduct());
            i.setQuantity(nd.getQuantity());
            i.setPrice(price);
            i.setInvoice(invoice);
            totalPrice = totalPrice + price;
            invoiceDetailList.add(i);
        }

        invoice.setTotalPrice(totalPrice);
        invoice.setInvoiceDetails(invoiceDetailList);

        sessionFactory.getCurrentSession().save(invoice);
        return invoice.getId();
    }

    public void updateInvoice(Invoice invoice){
        Invoice temp = getInvoiceById(invoice.getId());

        temp.setCustomer(invoice.getCustomer());
        temp.setStaff(invoice.getStaff());
        temp.setDate(invoice.getDate());

        sessionFactory.getCurrentSession().update(temp);
    }

    public void deleteInvoice(int invoiceId){
        Invoice invoice = getInvoiceById(invoiceId);
        sessionFactory.getCurrentSession().delete(invoice);
    }

    public List<Invoice> getInvoicesByCustomer(int customerId){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where p.customer.id = :customerId");
        query.setParameter("customerId", customerId);
        return query.list();
    }

    public List<Invoice> getInvoicesByDate(Date date){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where p.date = :date");
        query.setParameter("date", date);
        return query.list();
    }

    public List<Invoice> getInvoicesFromTo(Date fromDate, Date toDate){
        Query query = sessionFactory.getCurrentSession().createQuery("from Invoice as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return query.list();
    }

    public List<Invoice> getInvoicesCustomerFromTo(int customerId, Date fromDate, Date toDate){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where (p.date between :fromDate and :toDate) and p.customer.id = :customerId");
        query.setParameter("customerId", customerId);
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return query.list();
    }

    public List<Invoice> getInvoicesStaffFromTo(int staffId, Date fromDate, Date toDate){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where (p.date between :fromDate and :toDate) and p.staff.id = :staffId");
        query.setParameter("staffId", staffId);
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return query.list();
    }

    public List<Invoice> getInvoicesCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where (p.date between :fromDate and :toDate) and (p.customer.id = :customerId) and (p.staff.id = :staffId)");
        query.setParameter("customerId", customerId);
        query.setParameter("staffId", staffId);
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return query.list();
    }

    public List<InvoiceDetail> getInvoiceDetailByInvoiceId(int invoiceId) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from InvoiceDetail as p where p.invoice.id =:invoiceId");
        query.setParameter("invoiceId",invoiceId);
        return query.list();
    }

    public int addInvoiceDetail(InvoiceDetail invoiceDetail){
        sessionFactory.getCurrentSession().save(invoiceDetail);
        return invoiceDetail.getId();
    }

    public void updateInvoiceDetail(InvoiceDetail invoiceDetail){
        sessionFactory.getCurrentSession().update(invoiceDetail);
    }

    public void deleteInvoiceDetail(int invoiceDetailId){
        InvoiceDetail invoiceDetail = sessionFactory.getCurrentSession().get(InvoiceDetail.class, invoiceDetailId);
        Invoice order = invoiceDetail.getInvoice();
        List<InvoiceDetail> updatedList = order.getInvoiceDetails();
        updatedList.remove(invoiceDetail);
        order.setInvoiceDetails(updatedList);

        sessionFactory.getCurrentSession().update(order);
        sessionFactory.getCurrentSession().delete(invoiceDetail);
    }
}
