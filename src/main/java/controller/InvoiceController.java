package controller;

import model.Invoice;
import model.InvoiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.InvoiceStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/invoices")
public class InvoiceController {
    private final String dateFormat = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

    private InvoiceStore invoiceStore;
    @Autowired
    public void setInvoiceStore(InvoiceStore invoiceStore) {
        this.invoiceStore = invoiceStore;
    }

    @GetMapping(path="")
    public List<Invoice> getAllInvoices() {
        return invoiceStore.getAllInvoices();
    }

    @GetMapping(path = "/{invoiceId}")
    public Invoice getInvoiceById(@PathVariable int invoiceId){ return invoiceStore.getInvoiceById(invoiceId); }

    @PostMapping(path = "/{importedNoteId}")
    public int addInvoiceWithImportedNote(@RequestBody Invoice invoice, @PathVariable int importedNoteId) {
        return invoiceStore.addInvoiceWithImportedNote(invoice, importedNoteId);
    }

    @DeleteMapping(path = "/{invoiceId}")
    public void deleteInvoice(@PathVariable int invoiceId){
        invoiceStore.deleteInvoice(invoiceId);
    }

    @PutMapping(path = "")
    public void updateInvoice(@RequestBody Invoice invoice){
        invoiceStore.updateInvoice(invoice);
    }

    @GetMapping(path = "/customer/{customerId}")
    public List<Invoice> getInvoiceByCustomer(@PathVariable int customerId){ return invoiceStore.getInvoicesByCustomer(customerId); }

    @GetMapping(path = "/date/{date}")
    public List<Invoice> getInvoiceByDate(@PathVariable @DateTimeFormat(pattern = dateFormat) String date){
        try {
            Date date_temp = simpleDateFormat.parse(date);
            return invoiceStore.getInvoicesByDate(date_temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/from/{from}/to/{to}")
    public List<Invoice> getInvoiceFromTo(
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return invoiceStore.getInvoicesFromTo(dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/customer/{customerId}/from/{from}/to/{to}")
    public List<Invoice> getInvoiceCustomerFromTo(
            @PathVariable int customerId,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return invoiceStore.getInvoicesCustomerFromTo(customerId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/staff/{staffId}/from/{from}/to/{to}")
    public List<Invoice> getInvoiceStaffFromTo(
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return invoiceStore.getInvoicesStaffFromTo(staffId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/customer/{customerId}/staff/{staffId}/from/{from}/to/{to}")
    public List<Invoice> getInvoiceCustomerStaffFromTo(
            @PathVariable int customerId,
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = dateFormat) String to){
        try {
            Date dateFrom = simpleDateFormat.parse(from);
            Date dateTo = simpleDateFormat.parse(to);
            return invoiceStore.getInvoicesCustomerStaffFromTo(customerId,staffId,dateFrom, dateTo);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/detail/{invoiceId}")
    public List<InvoiceDetail> getInvoiceDetailByInvoiceId(@PathVariable  int invoiceId){
        return invoiceStore.getInvoiceDetailByInvoiceId(invoiceId);
    }

    @PostMapping(path = "/detail")
    public int addInvoiceDetail(@RequestBody InvoiceDetail providerInvoiceDetail){
        return invoiceStore.addInvoiceDetail(providerInvoiceDetail);
    }

    @PutMapping(path = "/detail")
    public void updateInvoiceDetail(@RequestBody InvoiceDetail providerInvoiceDetail){
        invoiceStore.updateInvoiceDetail(providerInvoiceDetail);
    }

    @DeleteMapping(path = "/detail/{invoiceDetailId}")
    public void deleteInvoiceDetail(@PathVariable int invoiceDetailId){ invoiceStore.deleteInvoiceDetail(invoiceDetailId); }
}
