package it.polimi.ingsw.psp26.application.messages;

public enum MessageType {

    GENERAL_MESSAGE,

    // Initialize game:
    MULTI_OR_SINGLE_PLAYER_MODE,
    SINGLE_PLAYER_MODE,
    TWO_PLAYERS_MODE,
    THREE_PLAYERS_MODE,
    FOUR_PLAYERS_MODE,
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
    CHOICE_RESOURCE_POSITION, // to display the position and resource
    MARKET_NEXT, // move to next turnPhase
    GRAB_RESOURCES,// Grab all resources from a depot
    CHOICE_ORGANIZATION_MOVE, // to display the moves possible about the organization
    RESOURCE_POSITION_CHOSEN, // resource e position chosen
    POSITION_CHOSEN, // position chosen of the bought card
    RESOURCE_CHOSEN, // resource chosen to use in base power
    CHOICE_POSITION, // to display the position of the card

    // Leader action activation:
    LEADER_CHOSEN,

    // End game:
    SEVENTH_CARD_DRAWN,
    FINAL_TILE_POSITION,
    NO_MORE_COLUMN_DEVELOPMENT_CARDS,
    BLACK_CROSS_FINAL_POSITION,

    // Display model components
    PERSONAL_BOARD;
}
