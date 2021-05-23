package it.polimi.ingsw.psp26.application.messages;

public enum MessageType {

    NOTIFICATION_UPDATE,
    MODEL_UPDATE,
    HEARTBEAT,
    DEATH,

    GENERAL_MESSAGE,
    ERROR_MESSAGE,

    // Initialize game:
    SET_NICKNAME,
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
    //LEADER_ACTIVATED, // to display the activated leader

    //Normal actions:
    CHOICE_NORMAL_ACTION, // to display to the client the normal action options

    ACTIVATE_PRODUCTION, // action
    CHOICE_PRODUCTIONS_TO_ACTIVATE, // to display to the client the card to activate
    MARKET_RESOURCE, // action

    BUY_CARD, // action
    CHOICE_CARD_TO_BUY, // to display the cards available to be bought
    CHOICE_DEVELOPMENT_CARD_SLOT_POSITION, // to display the position of the card

    CHOICE_ROW_COLUMN, // to display the column or the row

    // End game:
    SEVENTH_CARD_DRAWN,
    FINAL_TILE_POSITION,
    NO_MORE_COLUMN_DEVELOPMENT_CARDS,
    BLACK_CROSS_FINAL_POSITION,

    // Display model components
    PLAYER_MODEL,
    MARKET_TRAY_MODEL,
    DEVELOPMENT_GRID_MODEL,

    CHOICE_RESOURCE_FROM_WAREHOUSE,
    CHOICE_RESOURCE_FROM_RESOURCE_SUPPLY,
    PLACE_IN_WAREHOUSE,

    // Waiting screen
    START_WAITING,
    STOP_WAITING,

    // The Player select to undo from the current selection
    QUIT_OPTION_SELECTED,

    OPPONENT_TURN,
    LORENZO_PLAY,

    //EndGame Result
    ENDGAME_RESULT
}
