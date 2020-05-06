package store;

import model.Invoice;
import model.InvoiceDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.InvoiceService;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class InvoiceStore {
    private InvoiceService invoiceService;
    @Autowired
    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public List<Invoice> getAllInvoices(){
        return invoiceService.getAllInvoices();
    }

    public Invoice getInvoiceById(int invoiceId){
        return invoiceService.getInvoiceById(invoiceId);
    }

    public int addInvoiceWithImportedNote(Invoice invoice, int importedNoteId){
        return invoiceService.addInvoiceWithImportedNote(invoice, importedNoteId);
    }

    public void updateInvoice(Invoice invoice){
        invoiceService.updateInvoice(invoice);
    }

    public void deleteInvoice(int invoiceId){
        invoiceService.deleteInvoice(invoiceId);
    }

    public List<Invoice> getInvoicesByCustomer(int customerId){ return invoiceService.getInvoicesByCustomer(customerId); }

    public List<Invoice> getInvoicesByDate(Date date){
        return invoiceService.getInvoicesByDate(date);
    }

    public List<Invoice> getInvoicesFromTo(Date fromDate, Date toDate){
        return invoiceService.getInvoicesFromTo(fromDate, toDate);
    }

    public List<Invoice> getInvoicesCustomerFromTo(int customerId, Date fromDate, Date toDate){
        return invoiceService.getInvoicesCustomerFromTo(customerId,fromDate, toDate);
    }

    public List<Invoice> getInvoicesStaffFromTo(int staffId, Date fromDate, Date toDate){
        return invoiceService.getInvoicesStaffFromTo(staffId, fromDate, toDate);
    }

    public List<Invoice> getInvoicesCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate){
        return invoiceService.getInvoicesCustomerStaffFromTo(customerId, staffId, fromDate, toDate);
    }

    public List<InvoiceDetail> getInvoiceDetailByInvoiceId(int invoiceId){
        return invoiceService.getInvoiceDetailByInvoiceId(invoiceId);
    }

    public int addInvoiceDetail(InvoiceDetail invoiceDetail){ return invoiceService.addInvoiceDetail(invoiceDetail); }

    public void updateInvoiceDetail(InvoiceDetail invoiceDetail){ invoiceService.updateInvoiceDetail(invoiceDetail); }

    public void deleteInvoiceDetail(int invoiceDetailId){
        invoiceService.deleteInvoiceDetail(invoiceDetailId);
    }
}
