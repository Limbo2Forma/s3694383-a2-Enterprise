package store;

import model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.UtilitiesService;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class UtilitiesStore {
    private UtilitiesService utilitiesService;
    @Autowired
    public void setUtilitiesService(UtilitiesService utilitiesService) {
        this.utilitiesService = utilitiesService;
    }

    public Long revenueFromTo(Date fromDate, Date toDate) {
        return utilitiesService.revenueFromTo(fromDate, toDate);
    }

    public Long revenueCustomerFromTo(int customerId, Date fromDate, Date toDate) {
        return utilitiesService.revenueCustomerFromTo(customerId, fromDate, toDate);
    }

    public Long revenueStaffFromTo(int staffId, Date fromDate, Date toDate) {
        return utilitiesService.revenueStaffFromTo(staffId, fromDate, toDate);
    }

    public Long revenueCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate) {
        return utilitiesService.revenueCustomerStaffFromTo(customerId, staffId, fromDate, toDate);
    }

    public List<Inventory> getInventoriesByDate(Date date, int page) {
        return utilitiesService.getInventoriesByDate(date, page);
    }

    public void refreshDatabase() { utilitiesService.refreshDatabase(); }
}
