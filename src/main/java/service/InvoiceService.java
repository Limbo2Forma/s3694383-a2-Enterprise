package service;

import config.GlobalVar;
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

    public List<Invoice> getAllInvoices(int page){
        Query query =  sessionFactory.getCurrentSession().createQuery("from Invoice");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public Invoice getInvoiceById(int invoiceId){
        return sessionFactory.getCurrentSession().get(Invoice.class, invoiceId);
    }

    public int addInvoiceWithImportedNote(Invoice invoice, int importedNoteId){
        List<DeliveryNoteDetail> noteDetails = sessionFactory.getCurrentSession()
                .get(DeliveryNote.class, importedNoteId).getDeliveryNoteDetails();
        List<InvoiceDetail> invoiceDetailList = new ArrayList<>();


        for (DeliveryNoteDetail nd : noteDetails)
            invoiceDetailList.add(new InvoiceDetail(nd.getId(),invoice,nd.getProduct(),nd.getQuantity()));

        invoice.setInvoiceDetails(invoiceDetailList);
        invoice.setId(importedNoteId);

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

    public List<Invoice> getInvoicesByCustomer(int customerId, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where p.customer.id = :customerId");
        query.setParameter("customerId", customerId);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Invoice> getInvoicesByDate(Date date, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where p.date = :date");
        query.setParameter("date", date);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Invoice> getInvoicesFromTo(Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from Invoice as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Invoice> getInvoicesCustomerFromTo(int customerId, Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where (p.date between :fromDate and :toDate) and p.customer.id = :customerId");
        query.setParameter("customerId", customerId);
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Invoice> getInvoicesStaffFromTo(int staffId, Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where (p.date between :fromDate and :toDate) and p.staff.id = :staffId");
        query.setParameter("staffId", staffId);
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Invoice> getInvoicesCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Invoice as p where (p.date between :fromDate and :toDate) and (p.customer.id = :customerId) and (p.staff.id = :staffId)");
        query.setParameter("customerId", customerId);
        query.setParameter("staffId", staffId);
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<InvoiceDetail> getInvoiceDetailByInvoiceId(int invoiceId) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from InvoiceDetail as p where p.invoice.id =:invoiceId");
        query.setParameter("invoiceId",invoiceId);
        return query.list();
    }
}
