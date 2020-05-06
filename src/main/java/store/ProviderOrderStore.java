package store;

import model.ProviderOrder;
import model.ProviderOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.ProviderOrderService;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ProviderOrderStore {
    private ProviderOrderService providerOrderService;
    @Autowired
    public void setProviderOrderService(ProviderOrderService providerOrderService) {
        this.providerOrderService = providerOrderService;
    }

    public List<ProviderOrder> getAllOrders(){
        return providerOrderService.getAllOrders();
    }

    public ProviderOrder getOrderById(int orderId){
        return providerOrderService.getOrderById(orderId);
    }

    public int addOrder(ProviderOrder providerOrder){
        return providerOrderService.addOrder(providerOrder);
    }

    public void updateOrder(ProviderOrder providerOrder){
        providerOrderService.updateOrder(providerOrder);
    }

    public void deleteOrder(int orderId){
        providerOrderService.deleteOrder(orderId);
    }

    public List<ProviderOrder> getOrdersByProvider(int providerId){
        return providerOrderService.getOrdersByProvider(providerId);
    }

    public List<ProviderOrder> getOrdersByDate(Date date){
        return providerOrderService.getOrdersByDate(date);
    }

    public List<ProviderOrder> getOrdersFromTo(Date fromDate, Date toDate){
        return providerOrderService.getOrdersFromTo(fromDate, toDate);
    }

    public List<ProviderOrderDetail> getOrderDetailByOrderId(int orderId) {
        return providerOrderService.getOrderDetailByOrderId(orderId);
    }

    public int addOrderDetail(ProviderOrderDetail providerOrderDetail){
        return providerOrderService.addOrderDetail(providerOrderDetail);
    }

    public void updateOrderDetail(ProviderOrderDetail providerOrderDetail){
        providerOrderService.updateOrderDetail(providerOrderDetail);
    }

    public void deleteOrderDetail(int orderDetailId){
        providerOrderService.deleteOrderDetail(orderDetailId);
    }
}
