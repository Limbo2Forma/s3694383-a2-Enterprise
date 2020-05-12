package controller;

import config.GlobalVar;
import model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.UtilitiesStore;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/utilities")
public class UtilitiesController {

    private UtilitiesStore utilitiesStore;
    @Autowired
    public void setUtilitiesStore(UtilitiesStore utilitiesStore) {
        this.utilitiesStore = utilitiesStore;
        utilitiesStore.refreshDatabase();
    }

    @GetMapping(path = "/revenue/from/{from}/to/{to}")
    public Long revenueFromTo(
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return utilitiesStore.revenueFromTo(dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/revenue/customer/{customerId}/from/{from}/to/{to}")
    public Long revenueCustomerFromTo(
            @PathVariable int customerId,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return utilitiesStore.revenueCustomerFromTo(customerId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/revenue/staff/{staffId}/from/{from}/to/{to}")
    public Long revenueStaffFromTo(
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return utilitiesStore.revenueStaffFromTo(staffId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/revenue/customer/{customerId}/staff/{staffId}/from/{from}/to/{to}")
    public Long revenueCustomerStaffFromTo(
            @PathVariable int customerId,
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return utilitiesStore.revenueCustomerStaffFromTo(customerId,staffId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/warehouse={date}/p={page}")
    public List<Inventory> getInventoriesByDate(@PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String date,
                                                @PathVariable int page){
        try {
            Date temp = GlobalVar.dateFormatter.parse(date);
            return utilitiesStore.getInventoriesByDate(temp, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
