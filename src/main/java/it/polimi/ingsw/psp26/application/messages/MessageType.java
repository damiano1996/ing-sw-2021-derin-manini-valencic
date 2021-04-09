package it.polimi.ingsw.psp26.application.messages;

public enum MessageType {

    GENERAL_MESSAGE,

    // Leader actions
    DISCARD_LEADER,
    ACTIVATE_LEADER,
    SKIP_LEADER_ACTION,

    // Leader action activation:
    LEADER_CHOSEN;
}
