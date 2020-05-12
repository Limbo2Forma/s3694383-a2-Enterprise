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

    public List<Provider> getAllProviders(int page){ return providerService.getAllProviders(page); }

    public Provider getProviderById(int providerId){ return providerService.getProviderById(providerId); }

    public int addProvider(Provider provider){ return providerService.addProvider(provider); }

    public void updateProvider(Provider provider){ providerService.updateProvider(provider); }

    public void deleteProvider(int providerId){ providerService.deleteProvider(providerId); }

    public List<Provider> getProviderByName(String providerName, int page){ return providerService.getProviderByName(providerName, page); }

    public List<Provider> getProviderByAddress(String address, int page){ return providerService.getProviderByAddress(address, page); }

    public List<Provider> getProviderByPhone(String phone, int page){ return providerService.getProviderByPhone(phone, page); }
}
