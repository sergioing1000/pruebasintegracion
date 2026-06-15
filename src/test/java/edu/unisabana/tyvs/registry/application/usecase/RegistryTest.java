package edu.unisabana.tyvs.registry.application.usecase;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Gender;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;
import edu.unisabana.tyvs.registry.infrastructure.persistence.RegistryRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegistryTest {

    private RegistryRepositoryPort repo;
    private Registry registry;

    @Before
    public void setup() throws Exception {

        String jdbc = "jdbc:h2:mem:regdb;DB_CLOSE_DELAY=-1";

        repo = new RegistryRepository(jdbc);

        repo.initSchema();
        repo.deleteAll();

        registry = new Registry(repo);
    }

    @Test
    public void shouldRegisterValidPerson() throws Exception {

        Person person =
                new Person("Ana", 100, 30, Gender.FEMALE, true);

        RegisterResult result =
                registry.registerVoter(person);

        assertEquals(RegisterResult.VALID, result);
        assertTrue(repo.existsById(100));
    }

    @Test
    public void shouldRejectDuplicatePerson() throws Exception {

        Person p1 =
                new Person("Ana", 100, 30, Gender.FEMALE, true);

        Person p2 =
                new Person("Ana2", 100, 40, Gender.FEMALE, true);

        assertEquals(
                RegisterResult.VALID,
                registry.registerVoter(p1));

        assertEquals(
                RegisterResult.DUPLICATED,
                registry.registerVoter(p2));
    }

    @Test
    public void shouldRejectUnderagePerson() throws Exception {

        Person person =
                new Person("Juan", 101, 17, Gender.MALE, true);

        RegisterResult result =
                registry.registerVoter(person);

        assertEquals(RegisterResult.UNDERAGE, result);
        assertFalse(repo.existsById(101));
    }

    @Test
    public void shouldRejectDeadPerson() throws Exception {

        Person person =
                new Person("Pedro", 102, 35, Gender.MALE, false);

        RegisterResult result =
                registry.registerVoter(person);

        assertEquals(RegisterResult.DEAD, result);
        assertFalse(repo.existsById(102));
    }

    @Test
    public void shouldRejectInvalidId() throws Exception {

        Person person =
                new Person("Laura", -1, 25, Gender.FEMALE, true);

        RegisterResult result =
                registry.registerVoter(person);

        assertEquals(RegisterResult.INVALID, result);
    }

    @Test(expected = Exception.class)
    public void shouldFailWhenDatabaseConnectionIsInvalid() throws Exception {

        RegistryRepository repo =
                new RegistryRepository("jdbc:h2:tcp://invalid-server/regdb");

        repo.initSchema();
    }

}