package it.polimi.ingsw.psp26.view.gui.modelcomponents;

import it.polimi.ingsw.psp26.model.actiontokens.ActionToken;
import it.polimi.ingsw.psp26.view.gui.GUIUtils;
import it.polimi.ingsw.psp26.view.gui.effects.LightEffects;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class ActionTokenDrawer extends RatioDrawer {

    private final List<ActionToken> actionTokensToDisplay;

    public ActionTokenDrawer(List<ActionToken> actionTokensToDisplay, int maxWidth) {
        super(maxWidth);

        this.actionTokensToDisplay = actionTokensToDisplay;
    }


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
     * Creates the stack of unused Tokens
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
     * @return A view of the active Token
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
