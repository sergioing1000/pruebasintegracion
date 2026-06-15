package edu.unisabana.tyvs.registry.application.usecase;

import edu.unisabana.tyvs.registry.domain.model.Gender;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FakeRepositoryTest {

    @Test
    public void shouldRegisterPersonUsingFakeRepository() throws Exception {

        FakeRepository repository = new FakeRepository();

        Registry registry = new Registry(repository);

        Person person = new Person(
                "Carlos",
                100,
                30,
                Gender.MALE,
                true);

        RegisterResult result =
                registry.registerVoter(person);

        assertEquals(RegisterResult.VALID, result);
        assertEquals(1, repository.count());
    }

    @Test
    public void shouldDetectDuplicatedPersonUsingFakeRepository() throws Exception {

        FakeRepository repository = new FakeRepository();

        Registry registry = new Registry(repository);

        Person person = new Person(
                "Carlos",
                100,
                30,
                Gender.MALE,
                true);

        registry.registerVoter(person);

        RegisterResult secondAttempt =
                registry.registerVoter(person);

        assertEquals(RegisterResult.DUPLICATED, secondAttempt);
        assertEquals(1, repository.count());
    }

    @Test
    public void shouldDeleteAllRecords() throws Exception {

        FakeRepository repository = new FakeRepository();

        repository.save(
                new Person(
                        "Carlos",
                        100,
                        30,
                        Gender.MALE,
                        true));

        repository.save(
                new Person(
                        "Ana",
                        101,
                        25,
                        Gender.FEMALE,
                        true));

        assertEquals(2, repository.count());

        repository.deleteAll();

        assertEquals(0, repository.count());
    }
}