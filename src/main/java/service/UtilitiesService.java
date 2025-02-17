package service;

import config.GlobalVar;
import model.*;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class UtilitiesService {

    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    public Long revenueFromTo(Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where date between :fromDate and :toDate");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        Object result = query.getSingleResult();
        if (result == null) return 0L;
        return (Long) result;
    }

    public Long revenueCustomerFromTo(int customerId, Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where (date between :fromDate and :toDate) and customer.id = :customerId");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setParameter("customerId",customerId);
        Object result = query.getSingleResult();
        if (result == null) return 0L;
        return (Long) result;
    }

    public Long revenueStaffFromTo(int staffId, Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where (date between :fromDate and :toDate) and staff.id = :staffId");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setParameter("staffId",staffId);
        Object result = query.getSingleResult();
        if (result == null) return 0L;
        return (Long) result;
    }

    public Long revenueCustomerStaffFromTo(int customerId, int staffId, Date fromDate, Date toDate) {
        Query query = sessionFactory.getCurrentSession().createQuery(
                "select sum(totalPrice) from Invoice where (date between :fromDate and :toDate) " +
                        "and customer.id = :customerId and staff.id = :staffId");
        query.setParameter("fromDate",fromDate);
        query.setParameter("toDate",toDate);
        query.setParameter("customerId",customerId);
        query.setParameter("staffId",staffId);
        Object result = query.getSingleResult();
        if (result == null) return 0L;
        return (Long) result;
    }

    public long getQuantityUpToDate(Product product, Date date){
        Query receivingQuery = sessionFactory.getCurrentSession().createQuery(
                "select case when (sum(quantity) is null) then 0L else sum(quantity) end from ReceivingNoteDetail " +
                        "where receivingNote.date <= :date and product.id = :productId");
        receivingQuery.setParameter("date", date);
        receivingQuery.setParameter("productId", product.getId());

        Query deliveryQuery = sessionFactory.getCurrentSession().createQuery(
                "select case when (sum(quantity) is null) then 0L else sum(quantity) end from DeliveryNoteDetail " +
                        "where deliveryNote.date <= :date and product.id = :productId");
        deliveryQuery.setParameter("date", date);
        deliveryQuery.setParameter("productId", product.getId());

        return (long) receivingQuery.getSingleResult() - (long) deliveryQuery.getSingleResult();
    }

    public List<Inventory> getInventoriesByDate(Date date,int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from Product");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        List<Product> products =  query.list();
        List<Inventory> inventories = new ArrayList<>();
        for (Product p: products){
            long quantity = getQuantityUpToDate(p, date);
            inventories.add(new Inventory(p, quantity));
        }
        return inventories;
    }

    private CustomerService cs = new CustomerService();
    private ProviderService ps = new ProviderService();
    private StaffService ss = new StaffService();
    private ProductService prs = new ProductService();

    private void populateCustomer(){
        cs.setSessionFactory(this.sessionFactory);
        cs.addCustomer(new Customer(1, "customer 1","address 1","0123576829","fax 1",
                "email1@email.com","contact me 1"));
        cs.addCustomer(new Customer(2, "customer 12","address 12","01232226829","fax 2",
                "email12@email.com","contact me 22"));
        cs.addCustomer(new Customer(3, "customer 32","address 3","01232226829","fax 2",
                "email3@email.com","contact me 3"));
        cs.addCustomer(new Customer(4, "customer 32","address 345","01232226829","fax fax",
                "emailemail@email.com","contact me pls"));
    }
    private void populateProvider(){
        ps.setSessionFactory(this.sessionFactory);
        ps.addProvider(new Provider(1,"provider 1","addressp 1","0123576829","fax 1p",
                "email1p@email.com","contact me 1p"));
        ps.addProvider(new Provider(2,"provider 12","paddress 12","01232226829","faxp 2",
                "emailp12@email.com","contactp me 22"));
        ps.addProvider(new Provider(3,"provider 32","paddress 3","01232226829","faxp 2p",
                "pemail3@email.com","contact mep 3"));
    }
    private void populateStaff(){
        ss.setSessionFactory(this.sessionFactory);
        ss.addStaff(new Staff(1,"staff 1","address staff 1","01231231375","semail@email.com"));
        ss.addStaff(new Staff(2,"staff 2","address staff 2","23523135","semail2@email.com"));
        ss.addStaff(new Staff(3,"staff 123","address staff 123","62341124","semail123@email.com"));
        ss.addStaff(new Staff(4,"staff 34","address staff4 31","234623256","34semail@email.com"));
        ss.addStaff(new Staff(5,"staff 45","address staff5 31","36613533134","45semail@email.com"));
    }
    private void populateProduct(){
        prs.setSessionFactory(this.sessionFactory);

        prs.addProductCategory(new ProductCategory(1,"category 1"));
        prs.addProductCategory(new ProductCategory(2,"category 2 cat"));
        prs.addProductCategory(new ProductCategory(3,"category 3 ctata"));
        System.out.println("*******CATEGORY**"+sessionFactory.getCurrentSession().getTransaction().getStatus().toString());

        prs.addProduct(new Product(1,"product name 1","model 1","brand 1","company 1",
                "description 1",prs.getProductCategoryById(1),100));
        prs.addProduct(new Product(2,"product name 12","model a","new brand 1","company 1a",
                "description 2:lala",prs.getProductCategoryById(2),1));
        prs.addProduct(new Product(3,"product name 23","model b","brain brand","company com",
                "description 3:abaqwe",prs.getProductCategoryById(3),10));
        prs.addProduct(new Product(4,"product name 345","model 5r","brain brand","company 1",
                "description 4:sumting",prs.getProductCategoryById(3),20));
        prs.addProduct(new Product(5,"product name 451","new model 1","brand 2","company com",
                "description 5:nothing",prs.getProductCategoryById(1),40));
    }
    private void populateOrder() throws Exception {
        ProviderOrderService os = new ProviderOrderService();
        os.setSessionFactory(this.sessionFactory);

        os.addOrder(new ProviderOrder(1, GlobalVar.dateFormatter.parse("28-01-2019"),ss.getStaffById(1)
                ,ps.getProviderById(1), Arrays.asList(
                    new ProviderOrderDetail(prs.getProductById(1), 100, 100),
                    new ProviderOrderDetail(prs.getProductById(2), 11, 100),
                    new ProviderOrderDetail(prs.getProductById(3), 20, 100)
                )));
        os.addOrder(new ProviderOrder(2, GlobalVar.dateFormatter.parse("29-01-2019"),ss.getStaffById(2)
                ,ps.getProviderById(1), Arrays.asList(
                    new ProviderOrderDetail(prs.getProductById(4), 1, 11),
                    new ProviderOrderDetail(prs.getProductById(5), 53, 10),
                    new ProviderOrderDetail(prs.getProductById(3), 22, 200)
        )));
        os.addOrder(new ProviderOrder(3, GlobalVar.dateFormatter.parse("28-02-2019"),ss.getStaffById(3)
                ,ps.getProviderById(1), Arrays.asList(
                    new ProviderOrderDetail(prs.getProductById(1), 1000, 1),
                    new ProviderOrderDetail(prs.getProductById(2), 11, 10)
        )));
        os.addOrder(new ProviderOrder(4, GlobalVar.dateFormatter.parse("28-03-2019"),ss.getStaffById(1)
                ,ps.getProviderById(1), Arrays.asList(
                    new ProviderOrderDetail(prs.getProductById(4), 70, 40)
        )));
        os.addOrder(new ProviderOrder(5, GlobalVar.dateFormatter.parse("28-01-2019"),ss.getStaffById(4)
                ,ps.getProviderById(1), Arrays.asList(
                    new ProviderOrderDetail(prs.getProductById(1), 103, 200),
                    new ProviderOrderDetail(prs.getProductById(2), 11, 160),
                    new ProviderOrderDetail(prs.getProductById(3), 23, 110),
                    new ProviderOrderDetail(prs.getProductById(5), 209, 5)
        )));
    }
    private void populateReceivingNote() throws Exception {
        ReceivingNoteService rs = new ReceivingNoteService();
        rs.setSessionFactory(this.sessionFactory);

        rs.addReceivingNoteWithImportedOrder(new ReceivingNote(GlobalVar.dateFormatter.parse("30-01-2019")
                , ss.getStaffById(5)), 1);
        rs.addReceivingNoteWithImportedOrder(new ReceivingNote(GlobalVar.dateFormatter.parse("30-01-2019")
                , ss.getStaffById(4)), 2);
        rs.addReceivingNoteWithImportedOrder(new ReceivingNote(GlobalVar.dateFormatter.parse("01-03-2019")
                , ss.getStaffById(3)), 5);
        rs.addReceivingNoteWithImportedOrder(new ReceivingNote(GlobalVar.dateFormatter.parse("30-01-2019")
                , ss.getStaffById(3)), 3);
    }
    private void populateDeliveryNote() throws Exception {
        DeliveryNoteService ds = new DeliveryNoteService();
        ds.setSessionFactory(this.sessionFactory);

        ds.addDeliveryNote(new DeliveryNote(1, GlobalVar.dateFormatter.parse("02-10-2019"),ss.getStaffById(5),
                Arrays.asList(
                    new DeliveryNoteDetail(prs.getProductById(3), 100),
                    new DeliveryNoteDetail(prs.getProductById(1), 100),
                    new DeliveryNoteDetail(prs.getProductById(2), 100)
        )));
        ds.addDeliveryNote(new DeliveryNote(2, GlobalVar.dateFormatter.parse("09-10-2019"),ss.getStaffById(3),
                Arrays.asList(
                    new DeliveryNoteDetail(prs.getProductById(2), 1),
                    new DeliveryNoteDetail(prs.getProductById(4), 5),
                    new DeliveryNoteDetail(prs.getProductById(3), 10),
                    new DeliveryNoteDetail(prs.getProductById(5), 2)
        )));
        ds.addDeliveryNote(new DeliveryNote(3, GlobalVar.dateFormatter.parse("08-02-2020"),ss.getStaffById(1),
                Arrays.asList(
                    new DeliveryNoteDetail(prs.getProductById(3), 10),
                    new DeliveryNoteDetail(prs.getProductById(5), 10)
        )));
        ds.addDeliveryNote(new DeliveryNote(4, GlobalVar.dateFormatter.parse("08-03-2020"),ss.getStaffById(2),
                Arrays.asList(
                    new DeliveryNoteDetail(prs.getProductById(1), 20)
        )));
        System.out.println("*******DELI**"+sessionFactory.getCurrentSession().getTransaction().getStatus().toString());
    }
    private void populateInvoice() throws Exception {
        InvoiceService is = new InvoiceService();
        is.setSessionFactory(this.sessionFactory);

        is.addInvoiceWithImportedNote(new Invoice(GlobalVar.dateFormatter.parse("30-01-2019")
                , ss.getStaffById(5), cs.getCustomerById(2)), 2);
        is.addInvoiceWithImportedNote(new Invoice(GlobalVar.dateFormatter.parse("30-01-2019")
                , ss.getStaffById(4), cs.getCustomerById(1)), 4);
    }

    public void refreshDatabase(){
        if (sessionFactory.getCurrentSession().createQuery("select 1 from InvoiceDetail").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from Invoice").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from DeliveryNoteDetail").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from DeliveryNote").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from ProviderOrderDetail").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from ProviderOrder").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from ReceivingNoteDetail").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from ReceivingNote").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from Product ").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from ProductCategory ").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from Staff ").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from Customer ").list().isEmpty() &&
            sessionFactory.getCurrentSession().createQuery("select 1 from Provider ").list().isEmpty()
        ) {
            populateCustomer();
            populateStaff();
            populateProvider();
            populateProduct();
            try {
                populateOrder();
                populateReceivingNote();
                populateDeliveryNote();
                populateInvoice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
