package it.polimi.ingsw.Client;

import it.polimi.ingsw.Model.AssistantCard;
import it.polimi.ingsw.Server.ServerToClient.*;

import java.io.IOException;

/** View interface
 * @author Giuseppe Bonanno, Federica Tommasini, Angelo Zagami
 */
public interface View {

    /***
     * Request nickname to player
     * @throws IOException
     */
    void requestNickname() throws IOException;

    /**
     * prints an error
     * @param error error text
     */
    void showError(String error);

    void setUseCharcaterCard();

    void actionValid(String message);

    void chooseMatch(String message) throws IOException;

    void showMessage(String message);

    void requestSetup() throws IOException;

    void matchCreated(MatchCreated msg);

    void playersInfo(PlayersInfo msg);

    void setUpCharacterCard(SetUpCharacterCard msg);

    void setUpSchoolStudent(SetUpSchoolStudent msg);

    void isTurnOfPlayer(String msg);

    void youWin();

    void otherPlayerWins(OtherPlayerWins msg);

    /**
     * shows the assistant cards available in the player's deck
     * @param msg message containing the assets of the cards available 
     */
    void selectAssistantCard(SelectAssistantCard msg);

    void update(UpdateMessage msg);

    void chooseWizard(SelectWizard message) throws IOException;

    void showBoard();

    /**
     * make the player choose between doing an action (that can be either to move a student or to move mother nature
     * depending on the phase of the game) and playing a character card if the match is set to expert mode
     * @param msg message that provide the options
     */
    void chooseOption(ChooseOption msg);

    void playerPlayAssistantCard(int playerID, AssistantCard card);
    VirtualModel getVmodel();
}
