package store;

import model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.ProviderService;

import java.util.List;

@Transactional
@Service
public class ProviderStore {
    private ProviderService providerService;
    @Autowired
    public void setProviderService(ProviderService providerService) { this.providerService = providerService; }

    public List<Provider> getAllProviders(){ return providerService.getAllProviders(); }

    public Provider getProviderById(int providerId){ return providerService.getProviderById(providerId); }

    public int addProvider(Provider provider){ return providerService.addProvider(provider); }

    public void updateProvider(Provider provider){ providerService.updateProvider(provider); }

    public void deleteProvider(int providerId){ providerService.deleteProvider(providerId); }

    public List<Provider> getProviderByName(String providerName){ return providerService.getProviderByName(providerName); }

    public List<Provider> getProviderByAddress(String address){ return providerService.getProviderByAddress(address); }

    public List<Provider> getProviderByPhone(String phone){ return providerService.getProviderByPhone(phone); }
}
