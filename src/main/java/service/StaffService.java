package service;

import config.GlobalVar;
import model.Staff;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class StaffService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Staff> getAllStaffs(int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from Staff");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public Staff getStaffById(int staffId){
        return sessionFactory.getCurrentSession().get(Staff.class, staffId);
    }

    public int addStaff(Staff staff){
        sessionFactory.getCurrentSession().save(staff);
        return staff.getId();
    }

    public void updateStaff(Staff staff){
        sessionFactory.getCurrentSession().update(staff);
    }

    public void deleteStaff(int staffId){
        Staff staff = getStaffById(staffId);
        sessionFactory.getCurrentSession().delete(staff);
    }

    public List<Staff> getStaffByName(String name, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Staff as p where p.name like :staffName ");
        query.setParameter("staffName", "%" + name + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Staff> getStaffByAddress(String address, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Staff as p where p.address like :address ");
        query.setParameter("address", "%" + address + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Staff> getStaffByPhone(String phone, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Staff as p where p.phone like :phone ");
        query.setParameter("phone", "%" + phone + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }
}
