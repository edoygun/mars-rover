package com.ercandoygun.marsroverexample.services;

import com.ercandoygun.marsroverexample.model.Rover;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoverMapService {

    Map<Long, Rover> map = new HashMap<>();

    public Set<Rover> findAll() {
        return new HashSet<>(map.values());
    }

    public Rover findById(Long id) {
        return map.get(id);
    }

    public Rover save(Rover rover) {
        if(rover != null) {
            if(rover.getId() == null) {
                rover.setId(getNextId());
            }
            map.put(rover.getId(), rover);
        } else {
            throw new RuntimeException("Object cannot be null!");
        }
        return rover;
    }

    public Long getNextId() {
        Long nextId = null;
        try {
            nextId = Collections.max(map.keySet()) + 1;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }
}
