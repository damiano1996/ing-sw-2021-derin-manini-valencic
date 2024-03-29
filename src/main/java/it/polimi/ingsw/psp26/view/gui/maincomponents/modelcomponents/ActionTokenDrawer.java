package it.polimi.ingsw.psp26.view.gui.maincomponents.modelcomponents;

import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.view.gui.GUIUtils;
import it.polimi.ingsw.psp26.view.gui.effects.LightEffects;
import it.polimi.ingsw.psp26.view.gui.maincomponents.RatioDrawer;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Class to draw action tokens.
 */
public class ActionTokenDrawer extends RatioDrawer {

    private final List<ActionToken> actionTokensToDisplay;

    /**
     * Class constructor.
     *
     * @param actionTokensToDisplay list of the action tokens
     * @param maxWidth              max width that can be used to draw the root pane
     */
    public ActionTokenDrawer(List<ActionToken> actionTokensToDisplay, int maxWidth) {
        super(maxWidth);

        this.actionTokensToDisplay = actionTokensToDisplay;
    }


    /**
     * Method to draw the pane containing the action tokens.
     * It creates an horizontal box and fills it with the action tokens.
     *
     * @return pane containing the tokens
     */
    @Override
    public Pane draw() {
        HBox hBox = new HBox();

        hBox.setSpacing(50);
        if (actionTokensToDisplay.size() > 1)
            hBox.getChildren().add(createUnusedTokenList());
        hBox.getChildren().add(createVisibleToken());

        return hBox;
    }


    /**
     * Creates the stack of unused Tokens.
     *
     * @return The stack of the unused tokens
     */
    private VBox createUnusedTokenList() {
        VBox tokenStack = new VBox();
        tokenStack.setAlignment(Pos.CENTER);
        tokenStack.setSpacing(-100);

        // Creating the stack of unused Tokens
        for (int i = 0; i < actionTokensToDisplay.size() - 1; i++) {
            Image backTokenImage = GUIUtils.loadImage("punchboard/TokenBack.png", 100);
            backTokenImage = LightEffects.addLightEffects(backTokenImage, ratio);
            ImageView backTokenImageView = GUIUtils.getImageView(backTokenImage, 0, 0);
            tokenStack.getChildren().add(backTokenImageView);
        }

        return tokenStack;
    }


    /**
     * Method creates the visible token.
     * It creates a vertical box and fills it with the token and a text.
     *
     * @return a vertical box
     */
    private VBox createVisibleToken() {
        VBox visibleTokenBox = new VBox();
        visibleTokenBox.setSpacing(-20);
        visibleTokenBox.setAlignment(Pos.CENTER);

        // Creating the visible Token image
        Image visibleTokenImage = GUIUtils.loadImage("punchboard/" + actionTokensToDisplay.get(0).toString() + ".png", 150);
        visibleTokenImage = LightEffects.addLightEffects(visibleTokenImage, ratio);
        ImageView visibleTokenImageview = GUIUtils.getImageView(visibleTokenImage, 0, 0);
        visibleTokenBox.getChildren().add(visibleTokenImageview);

        // Creating bottom text
        Text activeTokenText = new Text("Active Token");
        activeTokenText.setId("title");
        visibleTokenBox.getChildren().add(activeTokenText);

        return visibleTokenBox;
    }

}
