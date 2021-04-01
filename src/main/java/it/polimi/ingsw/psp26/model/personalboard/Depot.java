package it.polimi.ingsw.psp26.model.personalboard;

import it.polimi.ingsw.psp26.application.Observable;
import it.polimi.ingsw.psp26.application.messages.Message;
import it.polimi.ingsw.psp26.exceptions.CanNotAddResourceToDepotException;
import it.polimi.ingsw.psp26.model.enums.Resource;
import it.polimi.ingsw.psp26.network.server.VirtualView;

import java.util.ArrayList;
import java.util.List;

public class Depot extends Observable<Message> {

    private final int maxNumberOfResources;
    private final List<Resource> resources;


    public Depot(VirtualView virtualView, int maxNumberOfResources) {
        super();
        addObserver(virtualView);

        this.maxNumberOfResources = maxNumberOfResources;
        resources = new ArrayList<>();

        notifyObservers(new Message()); // TODO: to be completed
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

        notifyObservers(new Message()); // TODO: to be completed
    }

    public void addResource(List<Resource> newResources) throws CanNotAddResourceToDepotException {
        if (newResources.size() > (maxNumberOfResources - resources.size()))
            throw new CanNotAddResourceToDepotException();
        for (Resource resource : newResources)
            if (!isAdmissible(resource)) throw new CanNotAddResourceToDepotException();
        resources.addAll(newResources);

        notifyObservers(new Message()); // TODO: to be completed
    }

    public void removeResource() {
        resources.clear();
        notifyObservers(new Message()); // TODO: to be completed
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
