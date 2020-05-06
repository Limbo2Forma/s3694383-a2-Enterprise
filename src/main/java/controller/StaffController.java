package controller;

import model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import store.StaffStore;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/staffs")
public class StaffController {
    private StaffStore staffStore;
    @Autowired
    public void setStaffStore(StaffStore staffStore) { this.staffStore = staffStore; }

    @GetMapping(path="")
    public List<Staff> getAllStaffs() {
        return staffStore.getAllStaffs();
    }

    @GetMapping(path = "/{staffId}")
    public Staff getStaffById(@PathVariable int staffId){ return staffStore.getStaffById(staffId); }

    @PostMapping(path = "")
    public int addStaff(@RequestBody Staff staff){ return staffStore.addStaff(staff); }

    @DeleteMapping(path = "/{staffId}")
    public void deleteStaff(@PathVariable int staffId){
        staffStore.deleteStaff(staffId);
    }

    @PutMapping(path = "")
    public void updateStaff(@RequestBody Staff staff){
        staffStore.updateStaff(staff);
    }

    @GetMapping(path ="/name/{name}")
    public List<Staff> getStaffByName(@PathVariable String name){ return staffStore.getStaffByName(name); }

    @GetMapping(path ="/address/{address}")
    public List<Staff> getStaffByAddress(@PathVariable String address){ return staffStore.getStaffByAddress(address); }

    @GetMapping(path ="/phone/{phone}")
    public List<Staff> getStaffByPhone(@PathVariable String phone){ return staffStore.getStaffByPhone(phone); }
}
