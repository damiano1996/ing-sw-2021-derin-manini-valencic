package it.polimi.ingsw.psp26.application.messages;

public enum MessageType {

    GENERAL_MESSAGE,

    // Initialize game:
    MAX_NUMBER_OF_PLAYERS,
    ADD_PLAYER,

    // Leader actions:
    CHOICE_LEADER_ACTION, // to display to the client the leader action options
    DISCARD_LEADER, // action
    ACTIVATE_LEADER, // action
    SKIP_LEADER_ACTION,
    CHOICE_LEADER_TO_ACTIVATE_OR_DISCARD, // to display to the client the leader cards
    LEADER_ACTIVATED, // to display the activated leader

    //Normal actions:
    CHOICE_NORMAL_ACTION, // to display to the client the normal action options
    ACTIVATE_PRODUCTION, // action
    CHOICE_RESOURCE_IN_RESOURCE_OUT, //to display to the client the resource to use in base power
    MARKET_RESOURCE, // action
    BUY_CARD, // action
    RESOURCE_CHOSEN, // resource chosen to use in base power

    // Leader action activation:
    LEADER_CHOSEN,

    // End game:
    SEVENTH_CARD_DRAWN,
    FINAL_TILE_POSITION,
    NO_MORE_COLUMN_DEVELOPMENT_CARDS,
    BLACK_CROSS_FINAL_POSITION;
}
