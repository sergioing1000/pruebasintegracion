package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;

import java.util.HashSet;
import java.util.Set;

public class Registry {

    private final Set<Integer> registeredIds = new HashSet<>();

    public RegisterResult registerVoter(Person person) {

        if (person == null) {
            return RegisterResult.INVALID;
        }

        if (person.getId() <= 0) {
            return RegisterResult.INVALID;
        }

        if (!person.isAlive()) {
            return RegisterResult.DEAD;
        }

        if (person.getAge() > 120) {
            return RegisterResult.INVALID_AGE;
        }

        if (person.getAge() < 18) {
            return RegisterResult.UNDERAGE;
        }

        if (registeredIds.contains(person.getId())) {
            return RegisterResult.DUPLICATED;
        }

        registeredIds.add(person.getId());

        return RegisterResult.VALID;
    }
}