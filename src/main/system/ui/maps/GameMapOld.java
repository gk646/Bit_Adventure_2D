package main.system.ui.maps;

import gameworld.PRJ_Control;
import gameworld.entities.ENTITY;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.MainGame;
import main.system.WorldRender;
import main.system.ui.Colors;
import main.system.ui.FonT;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GameMapOld {
    public final Rectangle mapMover;
    private final MainGame mg;
    private final int mapPanelX = 175;
    private final int mapPanelY = 75;
    private final Point previousMousePosition = new Point(500, 500);
    public float zoom = 5;
    public float deltaZoom = 0;
    int yOffset = 0;
    int xOffset = 0;
    private boolean followPlayer = true;
    private Image mapImage;
    private float xTile;
    private float yTile;

    /**
     * The big ingame map when you press "M"
     *
     * @param mg Main-game reference
     */
    public GameMapOld(MainGame mg) {
        this.mg = mg;
        this.mapMover = new Rectangle(mapPanelX, mapPanelY, 1_570, 940);
        xTile = mg.playerX;
        yTile = mg.playerY;
        hideMapCollision();
        dragMap();
    }

    public void draw(GraphicsContext gc) {
        drawGameMapBackground(gc);
        gc.drawImage(mapImage, 175, 80);
        drawTop(gc);
    }

    public void dragMap() {
        if (followPlayer) {
            xTile = mg.playerX;
            yTile = mg.playerY;
        }
        if (mapMover.contains(mg.inputH.lastMousePosition) && mg.inputH.mouse1Pressed) {
            followPlayer = false;
            xTile += (previousMousePosition.x - mg.inputH.lastMousePosition.x) / zoom;
            yTile += (previousMousePosition.y - mg.inputH.lastMousePosition.y) / zoom;
        }
        if (mg.inputH.mouse2Pressed) {
            followPlayer = true;
        }
        previousMousePosition.x = mg.inputH.lastMousePosition.x;
        previousMousePosition.y = mg.inputH.lastMousePosition.y;
    }

    private void getOffset(int zoom) {

        if (zoom == 1) {
            yOffset = 0;
            xOffset = 0;
        } else if (zoom == 2) {
            yOffset = 0;
            xOffset = 1;
        } else if (zoom == 3) {
            yOffset = 1;
            xOffset = 1;
        } else if (zoom == 4) {
            yOffset = 2;
            xOffset = 3;
        } else if (zoom == 5) {
            yOffset = 0;
            xOffset = 0;
        } else if (zoom == 6) {
            yOffset = 4;
            xOffset = 1;
        } else if (zoom == 7) {
            yOffset = 6;
            xOffset = 6;
        } else if (zoom == 8) {
            yOffset = 2;
            xOffset = 7;
        } else if (zoom == 9) {
            yOffset = 7;
            xOffset = 7;
        } else if (zoom == 10) {
            yOffset = 0;
            xOffset = 5;
        } else if (zoom == 11) {
            yOffset = 3;
            xOffset = 7;
        } else if (zoom == 12) {
            yOffset = 10;
            xOffset = 7;
        } else if (zoom == 13) {
            yOffset = 11;
            xOffset = 8;
        } else if (zoom == 14) {
            yOffset = 6;
            xOffset = 13;
        }
    }

    public void getImage() {
        int yTile_i = (int) yTile;
        int xTile_i = (int) xTile;
        int zoom_i = (int) zoom;
        getOffset(zoom_i);
        BufferedImage image = new BufferedImage(1_570, 935, BufferedImage.TYPE_INT_ARGB);
        int yTile_iOffset, xTile_iOffset, entityX, entityY;
        for (int y = 0; y < (940 / zoom_i) + 1; y++) {
            for (int x = 0; x < (1_570 / zoom_i) + 1; x++) {
                yTile_iOffset = (int) Math.max(Math.min(yTile_i - (940.0f / (zoom_i * 2)) + y, mg.wRender.worldSize.x - 1), 0);
                xTile_iOffset = (int) Math.max(Math.min(xTile_i - (1_570.0f / (zoom_i * 2)) + x, mg.wRender.worldSize.x - 1), 0);
                if (WorldRender.tileStorage[WorldRender.worldData[xTile_iOffset][yTile_iOffset]].collision) {
                    for (float i = y * zoom_i; i < y * zoom_i + zoom_i; i++) {
                        for (float b = x * zoom_i; b < x * zoom_i + zoom_i; b++) {
                            if (i < 935 && b < 1_570 && i > 0 && b > 0) {
                                image.setRGB((int) b, (int) i, 0xD05A6988);
                            }
                        }
                    }
                } else {
                    for (float i = y * zoom_i; i < y * zoom_i + zoom_i; i++) {
                        for (float b = x * zoom_i; b < x * zoom_i + zoom_i; b++) {
                            if (i < 935 && b < 1570 && i > 0 && b > 0) {
                                image.setRGB((int) b, (int) i, 0xD063_C74D);
                            }
                        }
                    }
                }
            }
        }

        int y = 470 + yOffset + (mg.playerY - yTile_i) * zoom_i;
        int x = 785 + xOffset + (mg.playerX - xTile_i) * zoom_i;
        System.out.println(yOffset + "  " + zoom);
        for (int i = y; i < y + zoom_i; i++) {
            for (int b = x; b < x + zoom_i; b++) {
                if (i < 935 && b < 1_570 && i > 0 && b > 0) {
                    image.setRGB(b, i, 0xD000_99DB);
                }
            }
        }
        synchronized (mg.PROXIMITY_ENTITIES) {
            for (gameworld.entities.ENTITY entity : mg.PROXIMITY_ENTITIES) {
                entityX = (entity.worldX + 24) / 48;
                entityY = (entity.worldY + 24) / 48;
                y = 470 + yOffset + (entityY - yTile_i) * zoom_i;
                x = 785 + xOffset + (entityX - xTile_i) * zoom_i;
                for (float i = y; i < y + zoom_i; i++) {
                    for (float b = x; b < x + zoom_i; b++) {
                        if (i < 935 && b < 1570 && i > 0 && b > 0) {
                            image.setRGB((int) b, (int) i, 0xD0FF_0044);
                        }
                    }
                }
            }
        }

        synchronized (mg.PROJECTILES) {
            for (PRJ_Control PRJControl : mg.PROJECTILES) {
                entityX = (int) ((PRJControl.worldPos.x + 24) / 48);
                entityY = (int) ((PRJControl.worldPos.y + 24) / 48);
                if ((entityX - xTile_i) < 157 && xTile_i - entityX <= 157 && (entityY - yTile_i) <= 93 && yTile_i - entityY < 93) {
                    y = 465 + yOffset + (entityY - yTile_i) * zoom_i;
                    x = 785 + xOffset + (entityX - xTile_i) * zoom_i;
                    for (float i = y; i < y + 2; i++) {
                        for (float b = x; b < x + 2; b++) {
                            image.setRGB((int) b, (int) i, 0xD0FF_0044);
                        }
                    }
                }
            }
        }
        for (ENTITY entity : mg.npcControl.NPC_Active) {
            entityX = (entity.worldX + 24) / 48;
            entityY = (entity.worldY + 24) / 48;
            y = 465 + yOffset + (entityY - yTile_i) * zoom_i;
            x = 785 + xOffset + (entityX - xTile_i) * zoom_i;
            if ((entityX - xTile_i) < 157 && xTile_i - entityX <= 157 && (entityY - yTile_i) <= 93 && yTile_i - entityY < 93) {
                for (float i = y; i < y + zoom_i; i++) {
                    for (float b = x; b < x + zoom_i; b++) {
                        if (i < 935 && b < 1570 && i > 0 && b > 0) {
                            image.setRGB((int) b, (int) i, 0xD012_4E89);
                        }
                    }
                }
            }
        }
        mapImage = SwingFXUtils.toFXImage(image, null);
    }

    private void drawGameMapBackground(GraphicsContext gc) {
        gc.setFill(Colors.LightGreyAlpha);
        gc.fillRoundRect(175, 75, 1_570, 940, 25, 25);
    }

    private void drawTop(GraphicsContext gc) {
        gc.setStroke(Colors.darkBackground);
        gc.setLineWidth(5);
        gc.strokeRoundRect(175, 70, 1_570, 945, 15, 15);
        gc.strokeRoundRect(175, 70, 1_570, 15, 15, 15);
        gc.setLineWidth(1);
        gc.setFill(Colors.mediumVeryLight);
        gc.fillRoundRect(175, 70, 1_570, 15, 10, 10);
        gc.setFill(Colors.darkBackground);
        gc.setFont(FonT.minecraftBold13);
        gc.fillText("World Map", 872, 82);
    }


    public void hideMapCollision() {
        mapMover.y = -1_100;
    }

    public void resetMapCollision() {
        mapMover.y = 75;
    }
}
