package gameworld.entitys;

import gameworld.Entity;
import main.MainGame;
import main.system.Utilities;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player2 extends Entity {
    private BufferedImage player2;

    public Player2(MainGame mainGame) {
        super(mainGame);
        //Setting default values
        getPlayerImage();
        worldX = 12200;
        worldY = 12200;
        this.entityHeight = 48;
        this.entityWidth = 48;


        //Handlers
        this.mg = mainGame;
    }

    private void getPlayerImage() {
        player2 = setup("player_2.png");
    }

    private BufferedImage setup(String imagePath) {
        Utilities utilities = new Utilities();
        BufferedImage scaledImage = null;
        try {
            scaledImage = ImageIO.read((Objects.requireNonNull(getClass().getResourceAsStream("/resources/player/" + imagePath))));
            scaledImage = utilities.scaleImage(scaledImage, 48, 48);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledImage;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(player2, worldX - mg.player.worldX + MainGame.SCREEN_WIDTH / 2 - 24, worldY - mg.player.worldY + MainGame.SCREEN_HEIGHT / 2 - 24, 48, 48, null);

    }


}

