package store;

import model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.StaffService;

import java.util.List;

@Transactional
@Service
public class StaffStore {
    private StaffService staffService;
    @Autowired
    public void setStaffService(StaffService staffService) {
        this.staffService = staffService;
    }

    public List<Staff> getAllStaffs(int page){
        return staffService.getAllStaffs(page);
    }

    public Staff getStaffById(int staffId){
        return staffService.getStaffById(staffId);
    }

    public int addStaff(Staff staff){ return staffService.addStaff(staff); }

    public void updateStaff(Staff staff){
        staffService.updateStaff(staff);
    }

    public void deleteStaff(int staffId){ staffService.deleteStaff(staffId); }

    public List<Staff> getStaffByName(String staffName, int page){ return staffService.getStaffByName(staffName, page); }

    public List<Staff> getStaffByAddress(String address, int page){ return staffService.getStaffByAddress(address, page); }

    public List<Staff> getStaffByPhone(String phone, int page){ return staffService.getStaffByPhone(phone, page); }
}
