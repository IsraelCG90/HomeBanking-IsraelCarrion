package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
     private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getAllClientsDto() {
        return getAllClients().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClientDtoById(Long id) {
        return new ClientDTO(getClientById(id));
    }

    @Override
    public boolean existsClientByEmail(String email) {
        return clientRepository.existsByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
