package controller;

import config.GlobalVar;
import model.*;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class MockData {
    // Customer mock mvc data
    public static final Customer customer = new Customer(1, "customer 1","address 1","0123576829"
            ,"fax 1", "email1@email.com","contact me 1");
    public static final List<Customer> customerList = Arrays.asList(customer, new Customer(2, "customer 12",
            "address 12","01232226829","fax 2", "email12@email.com","contact me 22"));
    // Provider mock mvc data
    public static final Provider provider = new Provider(2, "provider 12","address 12","01232226829",
            "fax 2", "email12@email.com","contact me 22");
    public static final List<Provider> providerList = Arrays.asList(new Provider(1, "provider 1","address 1",
                    "0123576829","fax 1", "email1@email.com","contact me 1"),provider);
    // Staff mock mvc data
    public static final Staff staff = new Staff(3,"staff 123","address staff 123","62341124","semail123@email.com");
    public static final List<Staff> staffList = Arrays.asList(
            new Staff(1,"staff 1","address staff 1","01231231375","semail@email.com"),
            new Staff(2,"staff 2","address staff 2","23523135","semail2@email.com"), staff);
    // Category mock mvc data
    public static ProductCategory category = new ProductCategory(1,"category 1");
    public static List<ProductCategory> categoryList = Arrays.asList(category, new ProductCategory(2,"category 2 cat"));
    // Product mock mvc data
    public static final Product product = new Product(2,"product name 1","model 1","brand 1",
            "company 1", "description 1",category,100);
    public static final List<Product> productList = Arrays.asList(new Product(1,"product name 345",
            "model 5r", "brain brand","company 1", "description 4:sumting",categoryList.get(1),
            20), product, new Product(3,"product name 451","new model 1","brand 2",
            "company com", "description 5:nothing",category,40)
    );
    // Order mock mvc data
    public static ProviderOrder providerOrder;
    public static List<ProviderOrder> providerOrderList;
    // Receiving Note mock mvc data
    public static ReceivingNote receivingNote;
    public static List<ReceivingNote> receivingNoteList;
    // Delivery Note mock mvc data
    public static DeliveryNote deliveryNote;
    public static List<DeliveryNote> deliveryNoteList;
    // Invoice mock mvc data
    public static Invoice invoice;
    public static List<Invoice> invoiceList;
    static {
        try {
            // fill Order mock mvc data
            List<ProviderOrderDetail> providerOrderDetailList = Arrays.asList(
                    new ProviderOrderDetail(productList.get(0), 100, 100),
                    new ProviderOrderDetail(productList.get(1), 11, 100),
                    new ProviderOrderDetail(productList.get(2), 20, 100),
                    new ProviderOrderDetail(productList.get(2), 20, 100),
                    new ProviderOrderDetail(productList.get(0), 100, 100),
                    new ProviderOrderDetail(productList.get(1), 11, 100)
            );
            providerOrder = new ProviderOrder(1, GlobalVar.dateFormatter.parse("28-01-2019")
                    ,staff,provider, Arrays.asList(
                    providerOrderDetailList.get(0),providerOrderDetailList.get(1),providerOrderDetailList.get(2)
            ));
            providerOrderList = Arrays.asList(providerOrder,
                    new ProviderOrder(2, GlobalVar.dateFormatter.parse("28-01-2019")
                            ,staff,provider, Arrays.asList(providerOrderDetailList.get(3)
                    )),
                    new ProviderOrder(3, GlobalVar.dateFormatter.parse("28-01-2019")
                            ,staffList.get(1),provider, Arrays.asList(
                            providerOrderDetailList.get(4),providerOrderDetailList.get(5)
                    ))
            );

            // fill Receiving Note mock mvc data
            receivingNote = new ReceivingNote(GlobalVar.dateFormatter.parse("28-01-2019"),staff);
            receivingNote.setId(1);
            receivingNote.setReceivingNoteDetails(Arrays.asList(
                    new ReceivingNoteDetail(providerOrderDetailList.get(0)),
                    new ReceivingNoteDetail(providerOrderDetailList.get(1)),
                    new ReceivingNoteDetail(providerOrderDetailList.get(2))
            ));
            ReceivingNote receivingNote2 = new ReceivingNote(GlobalVar.dateFormatter.parse("28-01-2019"),staff);
            receivingNote2.setId(2);
            receivingNote2.setReceivingNoteDetails(Arrays.asList(
                    new ReceivingNoteDetail(providerOrderDetailList.get(3))
            ));
            ReceivingNote receivingNote3 = new ReceivingNote(GlobalVar.dateFormatter.parse("28-01-2019"),staff);
            receivingNote3.setId(3);
            receivingNote3.setReceivingNoteDetails(Arrays.asList(
                    new ReceivingNoteDetail(providerOrderDetailList.get(4)),
                    new ReceivingNoteDetail(providerOrderDetailList.get(5))
            ));
            receivingNoteList = Arrays.asList(receivingNote, receivingNote2, receivingNote3);

            // fill Delivery Note mock mvc data
            List<DeliveryNoteDetail> deliveryNoteDetailList = Arrays.asList(
                    new DeliveryNoteDetail(productList.get(0), 100),
                    new DeliveryNoteDetail(productList.get(1), 11),
                    new DeliveryNoteDetail(productList.get(2), 20),
                    new DeliveryNoteDetail(productList.get(2), 200),
                    new DeliveryNoteDetail(productList.get(0), 1),
                    new DeliveryNoteDetail(productList.get(1), 111)
            );
            deliveryNote = new DeliveryNote(1, GlobalVar.dateFormatter.parse("28-01-2019"),staff, Arrays.asList(
                    deliveryNoteDetailList.get(0), deliveryNoteDetailList.get(1), deliveryNoteDetailList.get(2)
            ));
            deliveryNoteList = Arrays.asList(deliveryNote,
                    new DeliveryNote(2, GlobalVar.dateFormatter.parse("28-01-2019"),staff, Arrays.asList(
                            deliveryNoteDetailList.get(3)
                    )),
                    new DeliveryNote(3, GlobalVar.dateFormatter.parse("28-01-2019"),staffList.get(1), Arrays.asList(
                            deliveryNoteDetailList.get(4), deliveryNoteDetailList.get(5)
                    ))
            );

            // fill Invoice mock mvc data
            invoice = new Invoice(GlobalVar.dateFormatter.parse("28-01-2019"),staff,customer);
            invoice.setId(1);
            invoice.setInvoiceDetails(Arrays.asList(
                    new InvoiceDetail(deliveryNoteDetailList.get(0)),
                    new InvoiceDetail(deliveryNoteDetailList.get(1)),
                    new InvoiceDetail(deliveryNoteDetailList.get(2))
            ));
            Invoice invoice2 = new Invoice(GlobalVar.dateFormatter.parse("28-01-2019"),staff,customer);
            invoice2.setId(2);
            invoice2.setInvoiceDetails(Arrays.asList(
                    new InvoiceDetail(deliveryNoteDetailList.get(3))
            ));
            Invoice invoice3 = new Invoice(GlobalVar.dateFormatter.parse("28-01-2019"),staff,customer);
            invoice3.setId(3);
            invoice3.setInvoiceDetails(Arrays.asList(
                    new InvoiceDetail(deliveryNoteDetailList.get(4)),
                    new InvoiceDetail(deliveryNoteDetailList.get(5))
            ));
            invoiceList = Arrays.asList(invoice, invoice2, invoice3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final List<Inventory> inventories = Arrays.asList(
            new Inventory(productList.get(0),12),
            new Inventory(productList.get(1),24),
            new Inventory(productList.get(2),36)
    );
}
