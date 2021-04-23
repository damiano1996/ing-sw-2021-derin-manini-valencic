package it.polimi.ingsw.psp26.application.messages;

public enum MessageType {

    GENERAL_MESSAGE,
    ERROR_MESSAGE,

    // Initialize game:
    MULTI_OR_SINGLE_PLAYER_MODE,
    SINGLE_PLAYER_MODE,
    TWO_PLAYERS_MODE,
    THREE_PLAYERS_MODE,
    FOUR_PLAYERS_MODE,
    ADD_PLAYER,
    CHOICE_LEADERS,

    // Leader actions:
    CHOICE_LEADER_ACTION, // to display to the client the leader action options
    DISCARD_LEADER, // action
    ACTIVATE_LEADER, // action
    SKIP_LEADER_ACTION,
    LEADER_ACTIVATED, // to display the activated leader

    //Normal actions:
    CHOICE_NORMAL_ACTION, // to display to the client the normal action options

    ACTIVATE_PRODUCTION, // action
    CHOICE_CARDS_TO_ACTIVATE, // to display to the client the card to activate
    MARKET_RESOURCE, // action

    BUY_CARD, // action
    CHOICE_CARD_TO_BUY, // to display the cards available to be bought
    CHOICE_POSITION, // to display the position of the card

    CHOICE_ROW_COLUMN, // to display the column or the row

    // End game:
    SEVENTH_CARD_DRAWN,
    FINAL_TILE_POSITION,
    NO_MORE_COLUMN_DEVELOPMENT_CARDS,
    BLACK_CROSS_FINAL_POSITION,

    // Display model components
    PERSONAL_BOARD,

    // tmp
    CHOICE_RESOURCE,
    PLACE_IN_WAREHOUSE,
}
