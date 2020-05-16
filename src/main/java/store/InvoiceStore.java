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

    public List<Invoice> getAllInvoices(int page){
        return invoiceService.getAllInvoices(page);
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

    public List<Invoice> getInvoicesByCustomer(int customerId, int page){ return invoiceService.getInvoicesByCustomer(customerId, page); }

    public List<Invoice> getInvoicesByDate(Date date, int page){
        return invoiceService.getInvoicesByDate(date, page);
    }

    public List<Invoice> getInvoicesFromTo(Date fromDate, Date toDate, int page){
        return invoiceService.getInvoicesFromTo(fromDate, toDate, page);
    }

    public List<Invoice> getInvoicesCustomerFromTo(int customerId, Date fromDate, Date toDate, int page){
        return invoiceService.getInvoicesCustomerFromTo(customerId,fromDate, toDate, page);
    }

    public List<Invoice> getInvoicesStaffFromTo(int staffId, Date fromDate, Date toDate, int page){
        return invoiceService.getInvoicesStaffFromTo(staffId, fromDate, toDate, page);
    }

    public List<Invoice> getInvoicesCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate, int page){
        return invoiceService.getInvoicesCustomerStaffFromTo(customerId, staffId, fromDate, toDate, page);
    }

    public List<InvoiceDetail> getInvoiceDetailByInvoiceId(int invoiceId){
        return invoiceService.getInvoiceDetailByInvoiceId(invoiceId);
    }

    public void updateInvoiceDetailPrice(InvoiceDetail invoiceDetail){
        invoiceService.updateInvoiceDetailPrice(invoiceDetail);
    }
}
