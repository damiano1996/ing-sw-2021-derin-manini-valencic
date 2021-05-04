package it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates;

import it.polimi.ingsw.psp26.application.messages.SessionMessage;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.Turn;
import it.polimi.ingsw.psp26.controller.phases.phasestates.turns.turnstates.endgamecheckers.EndMatchCheckerTurnState;
import it.polimi.ingsw.psp26.model.Player;
import it.polimi.ingsw.psp26.model.personalboard.VaticanReportSection;

import java.util.ArrayList;
import java.util.List;

public class CheckVaticanReportTurnState extends TurnState {
    public CheckVaticanReportTurnState(Turn turn) {
        super(turn);
    }

    @Override
    public void play(SessionMessage message) {
        super.play(message);

        activateVaticanReport(turn.getTurnPlayer());
        // next state is...

        turn.changeState(new EndMatchCheckerTurnState(turn));
        turn.play(message);
    }



    private boolean isPlayerInVaticanSection(Player player, VaticanReportSection section){

        if(player.getPersonalBoard().getFaithTrack().getFaithPoints() > section.getStartSection())
            return true;

        return false;
    }

    private boolean firstPlayerInPopeSpace(Player player){
        if(player.getPersonalBoard().getFaithTrack().getFaithPoints() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection()) return true;
        return false;

    }

    private boolean blackCrossInPopeSpace(Player player){
        if(player.getPersonalBoard().getFaithTrack().getBlackCrossPosition() >=
                player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[getFirstActiveSectionIndex()].getEndSection()) return true;
        return false;

    }

    private void activateVaticanReport(Player currentPlayer){
        if(firstPlayerInPopeSpace(currentPlayer) || blackCrossInPopeSpace(currentPlayer)){

            int sectionActivated = getFirstActiveSectionIndex();

            for(Player player: turn.getMatchController().getMatch().getPlayers()){

                VaticanReportSection[] playerSections = player.getPersonalBoard().getFaithTrack().getVaticanReportSections();

                if(isPlayerInVaticanSection(player, playerSections[sectionActivated]))
                    playerSections[sectionActivated].activatePopesFavorTile();
            }
        }
    }

    private int getFirstActiveSectionIndex(){

        int sectionNumber = 0;
        VaticanReportSection activeSection = turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[sectionNumber];

        for(Player player: turn.getMatchController().getMatch().getPlayers()){
            for(int i = 0; i < player.getPersonalBoard().getFaithTrack().getVaticanReportSections().length ; i++){
               if(player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].isPopesFavorTileActive()
                       && player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].getStartSection()
                       > activeSection.getStartSection()){

                   activeSection = player.getPersonalBoard().getFaithTrack().getVaticanReportSections()[i];
                   sectionNumber = i + 1;
               }

            }
        }

        if(sectionNumber >= getBlackCrossSectionIndex())
            return sectionNumber;
        else
            return getBlackCrossSectionIndex();

    }

    private int getBlackCrossSectionIndex(){

        int blackCrossIndex = 0;

        for(int i = 0; i < turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections().length ; i++) {

            if (turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getBlackCrossPosition() >=
                    turn.getTurnPlayer().getPersonalBoard().getFaithTrack().getVaticanReportSections()[i].getEndSection()) {
                blackCrossIndex = i + 1;

            }

        }

        return blackCrossIndex;
    }

}
