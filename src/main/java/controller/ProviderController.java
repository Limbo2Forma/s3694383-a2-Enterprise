package controller;

import model.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import store.ProviderStore;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path = "/providers")
public class ProviderController {
    private ProviderStore providerStore;
    @Autowired
    public void setProviderStore(ProviderStore providerStore) { this.providerStore = providerStore; }

    @GetMapping(path="")
    public List<Provider> getAllProviders() { return providerStore.getAllProviders(); }

    @GetMapping(path = "/{providerId}")
    public Provider getProviderById(@PathVariable int providerId){ return providerStore.getProviderById(providerId); }

    @PostMapping(path = "")
    public int addProvider(@RequestBody Provider provider){ return providerStore.addProvider(provider); }

    @DeleteMapping(path = "/{providerId}")
    public String deleteProvider(@PathVariable int providerId){
        providerStore.deleteProvider(providerId);
        return "deleted Provider with id: " + providerId;
    }

    @PutMapping(path = "")
    public String updateProvider(@RequestBody Provider provider){
        providerStore.updateProvider(provider);
        return "updated Provider with id: " + provider.getId();
    }

    @GetMapping(path ="/name/{name}")
    public List<Provider> getProviderByName(@PathVariable String name){ return providerStore.getProviderByName(name); }

    @GetMapping(path ="/address/{address}")
    public List<Provider> getProviderByAddress(@PathVariable String address){ return providerStore.getProviderByAddress(address); }

    @GetMapping(path ="/phone/{phone}")
    public List<Provider> getProviderByPhone(@PathVariable String phone){ return providerStore.getProviderByPhone(phone); }
}
