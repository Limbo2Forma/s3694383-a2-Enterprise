package service;

import config.GlobalVar;
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

    public List<Provider> getAllProviders(int page){
        Query query = sessionFactory.getCurrentSession().createQuery("from Provider");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();   
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

    public List<Provider> getProviderByName(String name, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Provider as p where p.name like :providerName ");
        query.setParameter("providerName", "%" + name + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Provider> getProviderByAddress(String address, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Provider as p where p.address like :address ");
        query.setParameter("address", "%" + address + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }

    public List<Provider> getProviderByPhone(String phone, int page){
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from Provider as p where p.phone like :phone ");
        query.setParameter("phone", "%" + phone + "%");
        query.setFirstResult(page * GlobalVar.pageSize);
        query.setMaxResults(GlobalVar.pageSize);
        return query.list();
    }
}
