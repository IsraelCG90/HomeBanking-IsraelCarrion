package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dto.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    List<ClientDTO> getAllClientsDto();

    Client getClientById(Long id);

    ClientDTO getClientDtoById(Long id);

    boolean existsClientByEmail(String email);

    void saveClient(Client client);

    Client findClientByEmail(String email);

}
