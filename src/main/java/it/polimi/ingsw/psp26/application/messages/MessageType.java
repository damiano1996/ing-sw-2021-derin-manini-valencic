package it.polimi.ingsw.psp26.application.messages;

public enum MessageType {

    GENERAL_MESSAGE,

    // Initialize game:
    MAX_NUMBER_OF_PLAYERS,
    ADD_PLAYER,

    // Leader actions:
    DISCARD_LEADER,
    ACTIVATE_LEADER,
    SKIP_LEADER_ACTION,

    // Leader action activation:
    LEADER_CHOSEN,

    // End game:
    SEVENTH_CARD_DRAWN,
    FINAL_TILE_POSITION,
    NO_MORE_COLUMN_DEVELOPMENT_CARDS,
    BLACK_CROSS_FINAL_POSITION;
}
