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

    public List<Staff> getAllStaffs(){
        return staffService.getAllStaffs();
    }

    public Staff getStaffById(int staffId){
        return staffService.getStaffById(staffId);
    }

    public int addStaff(Staff staff){ return staffService.addStaff(staff); }

    public void updateStaff(Staff staff){
        staffService.updateStaff(staff);
    }

    public void deleteStaff(int staffId){ staffService.deleteStaff(staffId); }

    public List<Staff> getStaffByName(String staffName){ return staffService.getStaffByName(staffName); }

    public List<Staff> getStaffByAddress(String address){ return staffService.getStaffByAddress(address); }

    public List<Staff> getStaffByPhone(String phone){ return staffService.getStaffByPhone(phone); }
}
