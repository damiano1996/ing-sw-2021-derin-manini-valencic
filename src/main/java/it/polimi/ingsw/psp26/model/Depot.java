package it.polimi.ingsw.psp26.model;

import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;

import java.util.ArrayList;
import java.util.List;

public class Depot {

    private final int maxNumberOfResources;
    private List<Resource> resources;


    public Depot(int maxNumberOfResources) {
        this.maxNumberOfResources = maxNumberOfResources;
        resources = new ArrayList<>();
    }

    public int getMaxNumberOfResources() {
        return maxNumberOfResources;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void addResource(Resource resource) throws CanNotAddResourceToDepotException {
        if (resources.size() == maxNumberOfResources) throw new CanNotAddResourceToDepotException();
        if (isAdmissible(resource)) resources.add(resource);
        else throw new CanNotAddResourceToDepotException();
    }

    /**
     * checks if a resource could be added to a specified depot
     *
     * @param resource resource to be added to the depot
     * @return true if the resource can be added, false in the other case
     */
    private boolean isAdmissible(Resource resource) {
        return (resources.size() == 0 || resources.contains(resource));
    }


    public boolean equals(Depot depot) {
        if (this.maxNumberOfResources != depot.getMaxNumberOfResources()) return false;
        for (int i = 0; i < this.resources.size(); i++) {
            if (this.resources.get(i) != depot.getResources().get(i)) return false;
        }
        return true;
    }
}
