package edu.unisabana.tyvs.registry.application.usecase;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Person;

import java.util.HashMap;
import java.util.Map;

public class FakeRepository implements RegistryRepositoryPort {

    private final Map<Integer, Person> database = new HashMap<>();

    @Override
    public void initSchema() throws Exception {
        // No se necesita hacer nada para una BD en memoria
    }

    @Override
    public void deleteAll() throws Exception {
        database.clear();
    }

    @Override
    public boolean existsById(int id) throws Exception {
        return database.containsKey(id);
    }

    @Override
    public void save(Person person) throws Exception {
        database.put(person.getId(), person);
    }

    public int count() {
        return database.size();
    }
}