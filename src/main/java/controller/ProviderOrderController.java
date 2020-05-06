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
    public void deleteOrder(@PathVariable int orderId){
        providerOrderStore.deleteOrder(orderId);
    }

    @PutMapping(path = "")
    public void updateOrder(@RequestBody ProviderOrder providerOrder){
        providerOrderStore.updateOrder(providerOrder);
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
    public void updateOrderDetail(@RequestBody ProviderOrderDetail providerOrderDetail){ providerOrderStore.updateOrderDetail(providerOrderDetail); }

    @DeleteMapping(path = "/detail/{orderDetailId}")
    public void deleteOrderDetail(@PathVariable int orderDetailId){ providerOrderStore.deleteOrderDetail(orderDetailId); }
}
