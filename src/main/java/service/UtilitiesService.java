package service;

import model.Inventory;
import model.Product;
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
public class UtilitiesService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Long revenueFromTo(Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        return (Long) query.getSingleResult();
    }

    public Long revenueCustomerFromTo(int customerId, Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where (date between :fromDate and :toDate) and customer.id = :customerId");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setParameter("customerId",customerId);
        return (Long) query.getSingleResult();
    }

    public Long revenueStaffFromTo(int staffId, Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where (date between :fromDate and :toDate) and staff.id = :staffId");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setParameter("staffId",staffId);
        return (Long) query.getSingleResult();
    }

    public Long revenueCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where (date between :fromDate and :toDate) " +
                        "and customer.id = :customerId and staff.id = :staffId");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setParameter("customerId",customerId);
        query.setParameter("staffId",staffId);
        return (Long) query.getSingleResult();
    }

    public long getQuantityUpToDate(Product product, Date date){
        Query orderQuery = sessionFactory.getCurrentSession().createQuery(
                "select sum(quantity) from ReceivingNoteDetail where receivingNote.date <= :date and product.id = :productId");
        orderQuery.setParameter("date", date);
        orderQuery.setParameter("productId", product.getId());
        long quantity = (long) orderQuery.getSingleResult();
        Query invoiceQuery = sessionFactory.getCurrentSession().createQuery(
                "select sum(quantity) from DeliveryNoteDetail where deliveryNote.date <= :date and product.id = :productId");
        invoiceQuery.setParameter("date", date);
        invoiceQuery.setParameter("productId", product.getId());

        return (quantity - (long) invoiceQuery.getSingleResult());
    }

    public List<Inventory> getInventoriesByDate(Date date){
        List<Product> products = sessionFactory.getCurrentSession().createQuery("from Product").list();
        List<Inventory> inventories = new ArrayList<>();
        for (Product p: products){
            long quantity = getQuantityUpToDate(p, date);
            inventories.add(new Inventory(p, quantity));
        }
        return inventories;
    }
}
