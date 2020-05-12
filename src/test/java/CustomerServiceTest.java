//import model.Customer;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import service.CustomerService;
//
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.junit.jupiter.api.Assertions.*;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {Application.class, ServiceConfig.class})
//public class CustomerServicesTest {
//
//    @Autowired
//    @Qualifier("userService")
//    CustomerService customerService ;
//
//    private Customer user = new Customer("customer 1","linh", "user", "linh@gmail.com");
//    private Customer userModified = new Customer("uyawgfsdsdg","LinhVo", "user", "linh@gmail.com");
//
//    @BeforeEach
//    void setup(){    }
//
//    @Test
//    public void addCustomer() {
//        customerService = new CustomerService();
////                await().atMost(5, SECONDS).until(() -> userServices != null);
//        System.out.println(customerService);
//
//        try {
//            Customer actualUser = cu.addUser(user);
//            assertEquals(user.getUsername(),actualUser.getUsername());
//            System.out.println(userServices.getUserById(user.getuId()));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        userServices=null;
//    }
////
////    @Test
////    public void updateUser() throws InterruptedException {
////        userServices=new UserServices();
//////                await().atMost(5, SECONDS).until(() -> userServices != null);
////        System.out.println(userServices);
////        String actualUserId = userServices.updateUser(userModified);
////        Thread.sleep(300);
////        try {
////            User actualUser =userServices.getUserById(actualUserId);
////            Thread.sleep(300);
////            assertEquals(userModified.getUsername(),actualUser.getUsername());
////            assertEquals(userModified.getEmail(),actualUser.getEmail());
////        } catch (ExecutionException e) {
////            e.printStackTrace();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////        userServices=null;
////    }
////
////    @Test
////    public void  deleteUser(){
////        userServices = new UserServices();
////        userServices.deleteUser(user.getuId());
////        try {
////            Thread.sleep(100);
////            User nullUser = userServices.getUserById(user.getuId());
////            Thread.sleep(100);
////            assertNull(nullUser,"deleted");
////        } catch (ExecutionException e) {
////            e.printStackTrace();
////            fail();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////            fail();
////        }
////    }
//}