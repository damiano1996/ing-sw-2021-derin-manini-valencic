package it.polimi.ingsw.psp26.model;

import java.util.ArrayList;
import java.util.List;

public class Depot {

    private final int maxNumberOfResources;
    private List<Resource> resources;


    public Depot() {
        maxNumberOfResources = 6;
        resources = new ArrayList<>();
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void addResource(Resource resource) {
        if (isAdmissible(resource)) resources.add(resource);
        //else??
    }

    public boolean isAdmissible(Resource resource) {
        return (resources.size() == 0 || resources.contains(resource));
    }
}
