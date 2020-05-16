package controller;

import config.GlobalVar;
import model.Invoice;
import model.InvoiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import store.InvoiceStore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/invoices")
public class InvoiceController {
    private InvoiceStore invoiceStore;
    @Autowired
    public void setInvoiceStore(InvoiceStore invoiceStore) {
        this.invoiceStore = invoiceStore;
    }

    @GetMapping(path="/p={page}")
    public List<Invoice> getAllInvoices(@PathVariable int page) {
        return invoiceStore.getAllInvoices(page);
    }

    @GetMapping(path = "/{invoiceId}")
    public Invoice getInvoiceById(@PathVariable int invoiceId){ return invoiceStore.getInvoiceById(invoiceId); }

    @PostMapping(path = "/{importedNoteId}")
    public int addInvoiceWithImportedNote(@RequestBody Invoice invoice, @PathVariable int importedNoteId) {
        return invoiceStore.addInvoiceWithImportedNote(invoice, importedNoteId);
    }

    @DeleteMapping(path = "/{invoiceId}")
    public String deleteInvoice(@PathVariable int invoiceId){
        invoiceStore.deleteInvoice(invoiceId);
        return "deleted Invoice and its details with id: " + invoiceId;
    }

    @PutMapping(path = "")
    public String updateInvoice(@RequestBody Invoice invoice){
        invoiceStore.updateInvoice(invoice);
        return "updated Invoice and its details with id: " + invoice.getId();
    }

    @GetMapping(path = "/customer={customerId}/p={page}")
    public List<Invoice> getInvoiceByCustomer(@PathVariable int customerId, @PathVariable int page){
        return invoiceStore.getInvoicesByCustomer(customerId, page);
    }

    @GetMapping(path = "/date={date}/p={page}")
    public List<Invoice> getInvoiceByDate(@PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String date,
                                          @PathVariable int page){
        try {
            Date date_temp = GlobalVar.dateFormatter.parse(date);
            return invoiceStore.getInvoicesByDate(date_temp, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/from={from}/to={to}/p={page}")
    public List<Invoice> getInvoiceFromTo(
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return invoiceStore.getInvoicesFromTo(dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/customer={customerId}/from={from}/to={to}/p={page}")
    public List<Invoice> getInvoiceCustomerFromTo(
            @PathVariable int customerId,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return invoiceStore.getInvoicesCustomerFromTo(customerId,dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/staff={staffId}/from={from}/to={to}/p={page}")
    public List<Invoice> getInvoiceStaffFromTo(
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return invoiceStore.getInvoicesStaffFromTo(staffId,dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/customer={customerId}/staff={staffId}/from={from}/to={to}/p={page}")
    public List<Invoice> getInvoiceCustomerStaffFromTo(
            @PathVariable int customerId,
            @PathVariable int staffId,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String from,
            @PathVariable @DateTimeFormat(pattern = GlobalVar.dateFormat) String to,
            @PathVariable int page){
        try {
            Date dateFrom = GlobalVar.dateFormatter.parse(from);
            Date dateTo = GlobalVar.dateFormatter.parse(to);
            return invoiceStore.getInvoicesCustomerStaffFromTo(customerId,staffId,dateFrom, dateTo, page);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping(path = "/detail/{invoiceId}")
    public List<InvoiceDetail> getInvoiceDetailByInvoiceId(@PathVariable  int invoiceId){
        return invoiceStore.getInvoiceDetailByInvoiceId(invoiceId);
    }

    @PutMapping(path = "/detail/price")
    public String updateInvoiceDetailPrice(@RequestBody InvoiceDetail invoiceDetail){
        invoiceStore.updateInvoiceDetailPrice(invoiceDetail);
        return "updated Invoice Detail id " + invoiceDetail.getId() + " with price: " + invoiceDetail.getPrice();
    }
}
