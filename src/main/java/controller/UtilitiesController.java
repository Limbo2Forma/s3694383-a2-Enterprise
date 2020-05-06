package controller;

import model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.UtilitiesStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/utilities")
public class UtilitiesController {
    private final String dateFormat = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    private UtilitiesStore utilitiesStore;
    @Autowired
    public void setUtilitiesStore(UtilitiesStore utilitiesStore) { this.utilitiesStore = utilitiesStore; }

    @GetMapping(path = "/revenue/from/{from}/to/{to}")
    public Long revenueFromTo(
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return utilitiesStore.revenueFromTo(dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/revenue/customer/{customerId}/from/{from}/to/{to}")
    public Long revenueCustomerFromTo(
            @PathVariable int customerId,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return utilitiesStore.revenueCustomerFromTo(customerId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/revenue/staff/{staffId}/from/{from}/to/{to}")
    public Long revenueStaffFromTo(
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
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
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return utilitiesStore.revenueCustomerStaffFromTo(customerId,staffId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }
    @GetMapping(path = "/warehouse/{date}")
    public List<Inventory> getInventoriesByDate(
            @PathVariable @DateTimeFormat(pattern = dateFormat) String date){
        try {
            Date temp = simpleDateFormat.parse(date);
            return utilitiesStore.getInventoriesByDate(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
