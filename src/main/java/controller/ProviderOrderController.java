package controller;

import config.GlobalVar;
import model.ProviderOrder;
import model.ProviderOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.ProviderOrderStore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/orders")
public class ProviderOrderController {
    private ProviderOrderStore providerOrderStore;
    @Autowired
    public void setProviderOrderStore(ProviderOrderStore providerOrderStore) { this.providerOrderStore = providerOrderStore; }

    @GetMapping(path="/p={page}")
    public List<ProviderOrder> getAllOrders(@PathVariable int page) {
        return providerOrderStore.getAllOrders(page);
    }

    @GetMapping(path = "/{orderId}")
    public ProviderOrder getOrderById(@PathVariable int orderId){ return providerOrderStore.getOrderById(orderId); }

    @PostMapping(path = "")
    public int addOrder(@RequestBody ProviderOrder providerOrder){ return providerOrderStore.addOrder(providerOrder); }

    @DeleteMapping(path = "/{orderId}")
    public String deleteOrder(@PathVariable int orderId){
        providerOrderStore.deleteOrder(orderId);
        return "deleted Order and its details with id: " + orderId;
    }

    @PutMapping(path = "")
    public String updateOrder(@RequestBody ProviderOrder providerOrder){
        providerOrderStore.updateOrder(providerOrder);
        return "updated Order and its details with id: " + providerOrder.getId();
    }

    @GetMapping(path = "/provider={providerId}/p={page}")
    public List<ProviderOrder> getOrderByProvider(@PathVariable int providerId, @PathVariable int page){ 
        return providerOrderStore.getOrdersByProvider(providerId, page); 
    }

    @GetMapping(path = "/date={date}/p={page}")
    public List<ProviderOrder> getOrderByDate(@PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String date,
                                              @PathVariable int page){
        try {
            Date formattedDate = GlobalVar.dateFormatter.parse(date);
            return providerOrderStore.getOrdersByDate(formattedDate, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/from={from}/to={to}/p={page}")
    public List<ProviderOrder> getOrderFromTo(
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return providerOrderStore.getOrdersFromTo(dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/detail/{orderId}")
    public List<ProviderOrderDetail> getOrderDetailByOrderId(@PathVariable  int orderId){ return providerOrderStore.getOrderDetailByOrderId(orderId); }

    @PostMapping(path = "/detail")
    public int addOrderDetail(@RequestBody ProviderOrderDetail providerOrderDetail){ return providerOrderStore.addOrderDetail(providerOrderDetail); }

    @PutMapping(path = "/detail")
    public String updateOrderDetail(@RequestBody ProviderOrderDetail providerOrderDetail){
        providerOrderStore.updateOrderDetail(providerOrderDetail);
        return "updated Order Detail with id: " + providerOrderDetail.getId()
                + " from Order with id: " + providerOrderDetail.getProviderOrder().getId();
    }

    @DeleteMapping(path = "/detail/{orderDetailId}")
    public String deleteOrderDetail(@PathVariable int orderDetailId){
        int detailFrom = providerOrderStore.deleteOrderDetail(orderDetailId);
        return "updated Order Detail with id: " + orderDetailId + " from Order with id: " + detailFrom;
    }
}
