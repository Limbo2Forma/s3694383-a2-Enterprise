package service;

import config.GlobalVar;
import model.*;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ProviderOrderService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ProviderOrder> getAllOrders(int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from ProviderOrder");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public ProviderOrder getOrderById(int orderId){
        return sessionFactory.getCurrentSession().get(ProviderOrder.class, orderId);
    }

    public int addOrder(ProviderOrder providerOrder){
        for (ProviderOrderDetail providerOrderDetail : providerOrder.getProviderOrderDetails())
            providerOrderDetail.setProviderOrder(providerOrder);
        sessionFactory.getCurrentSession().save(providerOrder);
        return providerOrder.getId();
    }

    public void updateOrder(ProviderOrder providerOrder){
        ProviderOrder temp = getOrderById(providerOrder.getId());
        temp.setDate(providerOrder.getDate());
        temp.setProvider(providerOrder.getProvider());
        temp.setStaff(providerOrder.getStaff());
        sessionFactory.getCurrentSession().update(temp);
    }

    public void deleteOrder(int orderId){
        ProviderOrder providerOrder = getOrderById(orderId);
        ReceivingNote receivingNote = sessionFactory.getCurrentSession().get(ReceivingNote.class, orderId);
        sessionFactory.getCurrentSession().delete(providerOrder);
        sessionFactory.getCurrentSession().delete(receivingNote);
    }

    public List<ProviderOrder> getOrdersByProvider(int providerId, int page){
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from ProviderOrder as p where p.provider.id = :providerId");
        query.setParameter("providerId", providerId);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<ProviderOrder> getOrdersByDate(Date date, int page){
        Query query = sessionFactory.getCurrentSession().createQuery(
                        "from ProviderOrder as p where p.date = :date");
        query.setParameter("date", date);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<ProviderOrder> getOrdersFromTo(Date fromDate, Date toDate, int page){
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from ProviderOrder as p where p.date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<ProviderOrderDetail> getOrderDetailByOrderId(int orderId) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "from ProviderOrderDetail as p where p.providerOrder.id =:orderId");
        query.setParameter("orderId",orderId);
        return query.list();
    }

    public int addOrderDetail(ProviderOrderDetail providerOrderDetail){
        Product p = sessionFactory.getCurrentSession().get(Product.class, providerOrderDetail.getProduct().getId());
        p.setCurrentQuantity(p.getCurrentQuantity() + providerOrderDetail.getQuantity());

        sessionFactory.getCurrentSession().save(providerOrderDetail);

        ReceivingNote note = sessionFactory.getCurrentSession()
                .get(ReceivingNote.class, providerOrderDetail.getProviderOrder().getId());

        if (note != null){
            ReceivingNoteDetail noteDetail = new ReceivingNoteDetail();

            noteDetail.setProduct(providerOrderDetail.getProduct());
            noteDetail.setQuantity(providerOrderDetail.getQuantity());
            noteDetail.setId(providerOrderDetail.getId());
            noteDetail.setReceivingNote(note);

            List<ReceivingNoteDetail> noteDetailList = note.getReceivingNoteDetails();
            noteDetailList.add(noteDetail);
            note.setReceivingNoteDetails(noteDetailList);

            sessionFactory.getCurrentSession().update(note);
        }

        return providerOrderDetail.getId();
    }

    public void updateOrderDetail(ProviderOrderDetail providerOrderDetail){
        ProviderOrderDetail detail = sessionFactory.getCurrentSession()
                .get(ProviderOrderDetail.class, providerOrderDetail.getId());
        Product product = detail.getProduct();
        product.setCurrentQuantity(product.getCurrentQuantity() - detail.getQuantity());

        Product p = sessionFactory.getCurrentSession().get(Product.class, providerOrderDetail.getProduct().getId());
        p.setCurrentQuantity(p.getCurrentQuantity() + providerOrderDetail.getQuantity());

        detail.setPrice(providerOrderDetail.getPrice());
        detail.setProduct(providerOrderDetail.getProduct());
        detail.setQuantity(providerOrderDetail.getQuantity());

        sessionFactory.getCurrentSession().update(detail);

        ReceivingNoteDetail noteDetail = sessionFactory.getCurrentSession()
                .get(ReceivingNoteDetail.class, providerOrderDetail.getId());
        if (noteDetail != null){
            noteDetail.setProduct(providerOrderDetail.getProduct());
            noteDetail.setQuantity(providerOrderDetail.getQuantity());
            sessionFactory.getCurrentSession().update(noteDetail);
        }

    }

    public int deleteOrderDetail(int orderDetailId){
        ProviderOrderDetail oldDetail = sessionFactory.getCurrentSession().get(ProviderOrderDetail.class, orderDetailId);
        Product product = oldDetail.getProduct();
        product.setCurrentQuantity(product.getCurrentQuantity() - oldDetail.getQuantity());

        ProviderOrder order = oldDetail.getProviderOrder();
        int detailFrom = order.getId();
        List<ProviderOrderDetail> updatedList = order.getProviderOrderDetails();
        updatedList.remove(oldDetail);
        order.setProviderOrderDetails(updatedList);

        ReceivingNote note = sessionFactory.getCurrentSession()
                .get(ReceivingNote.class, oldDetail.getProviderOrder().getId());

        if (note != null) {
            ReceivingNoteDetail noteDetail = sessionFactory.getCurrentSession()
                    .get(ReceivingNoteDetail.class, orderDetailId);
            List<ReceivingNoteDetail> noteDetailList = note.getReceivingNoteDetails();
            noteDetailList.remove(noteDetail);
            note.setReceivingNoteDetails(noteDetailList);

            sessionFactory.getCurrentSession().update(note);
            sessionFactory.getCurrentSession().delete(noteDetail);
        }

        sessionFactory.getCurrentSession().update(order);
        sessionFactory.getCurrentSession().delete(oldDetail);

        return detailFrom;
    }
}
