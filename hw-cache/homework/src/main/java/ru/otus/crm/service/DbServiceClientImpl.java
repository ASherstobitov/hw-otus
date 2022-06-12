package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<String, Client> cache = new MyCache();

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;

        HwListener<String, Client> listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                cache.put(String.valueOf(clientId), createdClient);
                log.info("created client: {}", createdClient);
                return createdClient;
            }

            if (client != null) {
                cache.put(String.valueOf(client.getId()), client);
            }


            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            var client = cache.get(String.valueOf(id));

            var clientOptional = Optional.ofNullable(client).or(() -> {

                var tempOptionalClient = dataTemplate.findById(connection, id);

                tempOptionalClient.ifPresent(e -> cache.put(String.valueOf(e.getId()), e));
                return tempOptionalClient;
            });
            log.info("client: {}", clientOptional);

            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {

            var clientList = cache.getValues();

            if (clientList.isEmpty()) {
                clientList = dataTemplate.findAll(connection);
            }

            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
