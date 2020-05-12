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

    @GetMapping(path="/p={page}")
    public List<Staff> getAllStaffs(@PathVariable int page) {
        return staffStore.getAllStaffs(page);
    }

    @GetMapping(path = "/{staffId}")
    public Staff getStaffById(@PathVariable int staffId){ return staffStore.getStaffById(staffId); }

    @PostMapping(path = "")
    public int addStaff(@RequestBody Staff staff){ return staffStore.addStaff(staff); }

    @DeleteMapping(path = "/{staffId}")
    public String deleteStaff(@PathVariable int staffId){
        staffStore.deleteStaff(staffId);
        return "deleted Staff with id: " + staffId;
    }

    @PutMapping(path = "")
    public String updateStaff(@RequestBody Staff staff){
        staffStore.updateStaff(staff);
        return "updated Staff with id: " + staff.getId();
    }

    @GetMapping(path ="/name={name}/p={page}")
    public List<Staff> getStaffByName(@PathVariable String name, @PathVariable int page){
        return staffStore.getStaffByName(name, page);
    }

    @GetMapping(path ="/address={address}/p={page}")
    public List<Staff> getStaffByAddress(@PathVariable String address, @PathVariable int page){
        return staffStore.getStaffByAddress(address, page);
    }

    @GetMapping(path ="/phone={phone}/p={page}")
    public List<Staff> getStaffByPhone(@PathVariable String phone, @PathVariable int page){
        return staffStore.getStaffByPhone(phone, page);
    }
}
