package service;

import model.Provider;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProviderService {
    private SessionFactory sessionFactory;
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Provider> getAllProviders(){
        return sessionFactory.getCurrentSession().createQuery("from Provider").list();
    }

    public Provider getProviderById(int providerId){
        return sessionFactory.getCurrentSession().get(Provider.class, providerId);
    }

    public int addProvider(Provider provider){
        sessionFactory.getCurrentSession().save(provider);
        return provider.getId();
    }

    public void updateProvider(Provider provider){
        sessionFactory.getCurrentSession().update(provider);
    }

    public void deleteProvider(int providerId){
        Provider provider = getProviderById(providerId);
        sessionFactory.getCurrentSession().delete(provider);
    }

    public List<Provider> getProviderByName(String name){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Provider as p where p.name like :providerName ");
        query.setParameter("providerName", "%" + name + "%");
        return query.list();
    }

    public List<Provider> getProviderByAddress(String address){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Provider as p where p.address like :address ");
        query.setParameter("address", "%" + address + "%");
        return query.list();
    }

    public List<Provider> getProviderByPhone(String phone){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Provider as p where p.phone like :phone ");
        query.setParameter("phone", "%" + phone + "%");
        return query.list();
    }
}
