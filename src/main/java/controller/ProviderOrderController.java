package controller;

import model.ProviderOrder;
import model.ProviderOrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.ProviderOrderStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/orders")
public class ProviderOrderController {
    private final String dateFormat = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    private ProviderOrderStore providerOrderStore;
    @Autowired
    public void setProviderOrderStore(ProviderOrderStore providerOrderStore) { this.providerOrderStore = providerOrderStore; }

    @GetMapping(path="")
    public List<ProviderOrder> getAllOrders() {
        return providerOrderStore.getAllOrders();
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

    @GetMapping(path = "/provider/{providerId}")
    public List<ProviderOrder> getOrderByProvider(@PathVariable int providerId){ return providerOrderStore.getOrdersByProvider(providerId); }

    @GetMapping(path = "/date/{date}")
    public List<ProviderOrder> getOrderByDate(@PathVariable @DateTimeFormat(pattern = dateFormat) String date){
        try {
            Date formattedDate = simpleDateFormat.parse(date);
            return providerOrderStore.getOrdersByDate(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/from/{from}/to/{to}")
    public List<ProviderOrder> getOrderFromTo(
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return providerOrderStore.getOrdersFromTo(dateFrom, dateTo);
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
