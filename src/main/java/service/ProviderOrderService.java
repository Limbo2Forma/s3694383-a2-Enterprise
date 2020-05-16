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
        sessionFactory.getCurrentSession().delete(receivingNote);
        sessionFactory.getCurrentSession().delete(providerOrder);
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
        sessionFactory.getCurrentSession().save(providerOrderDetail);

        ReceivingNote note = sessionFactory.getCurrentSession().get(ReceivingNote.class, providerOrderDetail.getProviderOrder().getId());

        if (note != null){
            ReceivingNoteDetail noteDetail = new ReceivingNoteDetail(providerOrderDetail);
            noteDetail.setReceivingNote(note);
            note.getReceivingNoteDetails().add(noteDetail);

            sessionFactory.getCurrentSession().update(note);
        }

        return providerOrderDetail.getId();
    }

    public void updateOrderDetail(ProviderOrderDetail providerOrderDetail){
        ProviderOrderDetail detail = sessionFactory.getCurrentSession().get(ProviderOrderDetail.class, providerOrderDetail.getId());

        detail.setPrice(providerOrderDetail.getPrice());
        detail.setProduct(providerOrderDetail.getProduct());
        detail.setQuantity(providerOrderDetail.getQuantity());

        sessionFactory.getCurrentSession().update(detail);

        ReceivingNoteDetail noteDetail = sessionFactory.getCurrentSession().get(ReceivingNoteDetail.class, providerOrderDetail.getId());
        if (noteDetail != null){
            Product product = noteDetail.getProduct();
            product.setCurrentQuantity(product.getCurrentQuantity() - noteDetail.getQuantity());
            sessionFactory.getCurrentSession().update(product);

            noteDetail.setProviderOrderDetail(detail);

            sessionFactory.getCurrentSession().update(noteDetail);
        }
    }

    public int deleteOrderDetail(int orderDetailId){
        ProviderOrderDetail oldDetail = sessionFactory.getCurrentSession().get(ProviderOrderDetail.class, orderDetailId);
        ReceivingNote note = sessionFactory.getCurrentSession().get(ReceivingNote.class, oldDetail.getProviderOrder().getId());

        if (note != null) {
            ReceivingNoteDetail noteDetail = sessionFactory.getCurrentSession().get(ReceivingNoteDetail.class, orderDetailId);
            Product product = noteDetail.getProduct();
            product.setCurrentQuantity(product.getCurrentQuantity() - noteDetail.getQuantity());
            sessionFactory.getCurrentSession().update(product);
            note.getReceivingNoteDetails().remove(noteDetail);
            sessionFactory.getCurrentSession().update(note);
        }

        ProviderOrder order = oldDetail.getProviderOrder();
        int detailFrom = order.getId();
        order.getProviderOrderDetails().remove(oldDetail);
        sessionFactory.getCurrentSession().update(order);

        return detailFrom;
    }
}
