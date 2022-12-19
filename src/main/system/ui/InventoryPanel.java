package main.system.ui;

import gameworld.Item;
import main.MainGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class InventoryPanel {
    private static final int SLOT_SIZE = 45;
    private static final int BAG_SLOTS = 15;
    private static final int CHAR_SLOTS = 10;
    public final Color lightBackground = new Color(192, 203, 220), lightBackgroundAlpha = new Color(0xCAC0CBDC, true), darkBackground = new Color(90, 105, 136);
    private final InventorySlot[] bag_Slots;
    public int charPanelX = 300, charPanelY = 300, bagPanelX = 1400, bagPanelY = 600;
    public MainGame mg;
    public InventorySlot[] char_Slots;
    public Point previousMousePosition = new Point(300, 300);
    Item grabbedItem;
    InventorySlot grabbedSlot;
    public Rectangle charPanelCloser, bagPanelCloser, charPanelMover, bagPanelMover;

    public InventoryPanel(MainGame mainGame) {
        this.mg = mainGame;
        bag_Slots = new InventorySlot[BAG_SLOTS];
        char_Slots = new InventorySlot[CHAR_SLOTS];
        this.grabbedItem = null;
        createCharSlots();
        createBagSlots();
        char_Slots[1].setItem(new ARM_chest02());
        charPanelMover = new Rectangle(charPanelX, charPanelY, 500, 50);
        charPanelCloser = new Rectangle(charPanelX, charPanelY, 30, 30);
        bagPanelMover = new Rectangle(bagPanelX, bagPanelY, 410, 50);
        bagPanelCloser = new Rectangle(bagPanelX, bagPanelY, 30, 30);
    }

    //todo add moveable windows and recenter button
    public void drawCharacterWindow(Graphics2D g2) {
        g2.setFont(mg.ui.maruMonica);
        interactWithWindows();
        drawDragandDrop();
        drawBagPanel(g2, bagPanelX, bagPanelY);
        drawCharPanel(g2, charPanelX, charPanelY);

    }

    public void drawBagWindow(Graphics2D g2) {
        g2.setFont(mg.ui.maruMonica);
        interactWithWindows();
        drawDragandDrop();
        drawBagPanel(g2, bagPanelX, bagPanelY);
    }

    public void drawCharPanel(Graphics2D g2, int startX, int startY) {
        drawCharacterBackground(g2, startX, startY);
        drawCharcterSlots(g2, startX, startY);
    }

    public void drawBagPanel(Graphics2D g2, int startX, int startY) {
        drawBagBackground(g2, startX, startY);
        drawBagSlots(g2, startX, startY);
    }

    public int drawDragandDrop() {
        if (grabbedItem == null && mg.mouseHandler.mouse1Pressed) {
            for (InventorySlot invSlot : char_Slots) {
                if (invSlot.boundBox.contains(mg.mouseHandler.mouse1Position) && invSlot.item != null) {
                    invSlot.grabbed = true;
                    grabbedSlot = invSlot;
                    grabbedItem = invSlot.item;
                    return 1;
                }
            }
            for (InventorySlot bagSlot : bag_Slots) {
                if (bagSlot.boundBox.contains(mg.mouseHandler.mouse1Position) && bagSlot.item != null) {
                    bagSlot.grabbed = true;
                    grabbedSlot = bagSlot;
                    grabbedItem = bagSlot.item;
                    return 1;
                }
            }
        }
        if (grabbedItem != null && !mg.mouseHandler.mouse1Pressed) {
            for (InventorySlot invSlot : char_Slots) {
                if (invSlot.boundBox.contains(mg.motionHandler.lastMousePosition) && invSlot != grabbedSlot) {
                    invSlot.setItem(grabbedItem);
                    grabbedSlot.item = null;
                    grabbedItem = null;
                    grabbedSlot = null;
                    return 1;
                }
            }
            for (InventorySlot bagSlot : bag_Slots) {
                if (bagSlot.boundBox.contains(mg.motionHandler.lastMousePosition) && bagSlot != grabbedSlot) {
                    bagSlot.setItem(grabbedItem);
                    grabbedSlot.item = null;
                    grabbedItem = null;
                    grabbedSlot = null;
                    return 1;
                }
            }
            grabbedSlot.grabbed = false;
            grabbedItem = null;
        }
        if (mg.mouseHandler.mouse2Pressed) {
            grabbedSlot.grabbed = false;
            grabbedItem = null;
        }
        return 0;
    }

    public void interactWithWindows() {
        if (mg.mouseHandler.mouse1Pressed && charPanelMover.contains(mg.motionHandler.lastMousePosition)) {
            charPanelX += mg.motionHandler.lastMousePosition.x - previousMousePosition.x;
            charPanelY += mg.motionHandler.lastMousePosition.y - previousMousePosition.y;
        } else if (mg.mouseHandler.mouse1Pressed && bagPanelMover.contains(mg.motionHandler.lastMousePosition)) {
            bagPanelX += mg.motionHandler.lastMousePosition.x - previousMousePosition.x;
            bagPanelY += mg.motionHandler.lastMousePosition.y - previousMousePosition.y;
        } else if (mg.mouseHandler.mouse1Pressed && charPanelCloser.contains(mg.motionHandler.lastMousePosition)) {
            //close char
        } else if (mg.mouseHandler.mouse1Pressed && bagPanelCloser.contains(mg.motionHandler.lastMousePosition)) {
            //close bag
        }
        previousMousePosition = mg.motionHandler.lastMousePosition;
    }

    public void drawCharacterBackground(Graphics2D g2, int startX, int startY) {
        //inventory background
        //big background
        g2.setColor(new Color(192, 203, 220, 220));
        g2.fillRoundRect(startX - 50, startY - 65, 500, 600, 25, 25);
        //outline
        g2.setColor(darkBackground);
        g2.drawRoundRect(startX, startY, 400, 550, 30, 30);
        //window mover
        g2.setColor(darkBackground);
        g2.drawRoundRect(startX - 50, startY - 65 + 2, 500, 30, 15, 15);
        charPanelMover.x = startX - 50;
        charPanelMover.y = startY - 65 + 2 - 10;
        //window close button
        g2.setColor(Color.gray);
        g2.drawRoundRect(startX + 500 - 50 - 30, startY - 65 + 2, 30, 30, 5, 5);
        charPanelCloser.x = startX - 50 + 500 - 30;
        charPanelCloser.y = startY - 65 + 2;
        //character image outline
        g2.setColor(darkBackground);
        g2.drawRoundRect(100 + startX, startY + 90, 200, 250, 25, 25);
        //player image
        g2.drawImage(mg.player.entityImage1, startX + 120, startY + 150, 150, 150, null);
        //Stats Text
        g2.setColor(darkBackground);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
        g2.drawString("Mage: Level" + mg.player.level, 120 + startX, 40 + startY);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
        g2.drawString("Health:" + mg.player.maxHealth, startX + 110, startY + 420);
    }

    public void drawCharcterSlots(Graphics2D g2, int startX, int startY) {
        //Character Slots
        for (int i = 0; i <= 3; i++) {
            char_Slots[i].boundBox.x = 40 + startX;
            char_Slots[i].boundBox.y = (i * 50 + 130 + startY);
            char_Slots[i].drawSlot(g2, 40 + startX, (i * 50 + 130 + startY));
            if (char_Slots[i].item != null && char_Slots[i].grabbed) {
                char_Slots[i].drawIcon(g2, mg.motionHandler.lastMousePosition.x - SLOT_SIZE / 2, mg.motionHandler.lastMousePosition.y - SLOT_SIZE / 2, SLOT_SIZE);
            } else if (char_Slots[i].item != null) {
                char_Slots[i].drawIcon(g2, 40 + startX, ((i * 50) + 130 + startY), SLOT_SIZE);
            }
        }
        for (int i = 4; i <= 7; i++) {
            char_Slots[i].boundBox.x = 40 + 270 + startX;
            char_Slots[i].boundBox.y = (((i - 4) * 50) + 130 + startY);
            char_Slots[i].drawSlot(g2, 40 + 270 + startX, (((i - 4) * 50) + 130 + startY));
            if (char_Slots[i].item != null && char_Slots[i].grabbed) {
                char_Slots[i].drawIcon(g2, mg.motionHandler.lastMousePosition.x - SLOT_SIZE / 2, mg.motionHandler.lastMousePosition.y - SLOT_SIZE / 2, SLOT_SIZE);
            } else if (char_Slots[i].item != null) {
                char_Slots[i].drawIcon(g2, 40 + 270 + startX, ((i - 4) * 50) + 130 + startY, SLOT_SIZE);
            }
        }
        for (int i = 8; i <= 9; i++) {
            char_Slots[i].boundBox.x = ((i - 8) * 50) + 140 + startX;
            char_Slots[i].boundBox.y = 110 + 240 + startY;
            char_Slots[i].drawSlot(g2, ((i - 8) * 50) + 140 + startX, 110 + 240 + startY);
            if (char_Slots[i].item != null && char_Slots[i].grabbed) {
                char_Slots[i].drawIcon(g2, mg.motionHandler.lastMousePosition.x - SLOT_SIZE / 2, mg.motionHandler.lastMousePosition.y - SLOT_SIZE / 2, SLOT_SIZE);
            } else if (char_Slots[i].item != null) {
                char_Slots[i].drawIcon(g2, ((i - 8) * 50) + 140 + startX, 110 + 240 + startY, SLOT_SIZE);
            }
        }

    }

    public void drawBagBackground(Graphics2D g2, int startX, int startY) {
        //big background
        g2.setColor(new Color(192, 203, 220, 220));
        g2.fillRoundRect(startX, startY, 310, 410, 15, 15);
        //outline
        g2.setColor(darkBackground);
        g2.drawRoundRect(startX + 5, startY + 5, 300, 400, 25, 25);
        //window mover
        g2.setColor(darkBackground);
        g2.fillRoundRect(startX, startY, 310, 30, 15, 15);
        bagPanelMover.x = startX;
        bagPanelMover.y = startY - 10;
        //window close button
        g2.setColor(Color.red);
        g2.fillRoundRect(startX + 310 - 30, startY, 30, 30, 15, 15);
        bagPanelCloser.x = startX + 310;
        bagPanelCloser.y = startY;
    }

    public void drawBagSlots(Graphics2D g2, int startX, int startY) {
        for (int i = 0; i < bag_Slots.length; i++) {
            if (i <= 7) {
                bag_Slots[i].boundBox.x = i * 50 + startX + 20;
                bag_Slots[i].boundBox.y = 40 + startY;
                bag_Slots[i].drawSlot(g2, i * 50 + startX + 20, 40 + startY);
                if (bag_Slots[i].item != null && bag_Slots[i].grabbed) {
                    bag_Slots[i].drawIcon(g2, mg.motionHandler.lastMousePosition.x - SLOT_SIZE / 2, mg.motionHandler.lastMousePosition.y - SLOT_SIZE / 2, SLOT_SIZE);
                } else if (bag_Slots[i].item != null) {
                    bag_Slots[i].drawIcon(g2, i * 50 + startX + 20, 40 + startY, SLOT_SIZE);
                }
            } else if (i <= 14) {
                bag_Slots[i].boundBox.x = (i - 7) * 50 + startX + 20;
                bag_Slots[i].boundBox.y = 90 + startY;
                bag_Slots[i].drawSlot(g2, (i - 7) * 50 + startX + 20, 90 + startY);
                if (bag_Slots[i].item != null && bag_Slots[i].grabbed) {
                    bag_Slots[i].drawIcon(g2, mg.motionHandler.lastMousePosition.x - SLOT_SIZE / 2, mg.motionHandler.lastMousePosition.y - SLOT_SIZE / 2, SLOT_SIZE);
                } else if (bag_Slots[i].item != null) {
                    bag_Slots[i].drawIcon(g2, (i - 7) * 50 + startX + 20, 90 + startY, SLOT_SIZE);
                }
            }
        }
    }

    private void createCharSlots() {
        for (int i = 0; i <= 3; i++) {
            char_Slots[i] = new InventorySlot(null, 40 + 260, (i * 50) + 130 + 200);
        }
        for (int i = 4; i <= 7; i++) {
            char_Slots[i] = new InventorySlot(null, 40 + 270 + 260, ((i - 4) * 50) + 130 + 200);
        }
        for (int i = 8; i <= 9; i++) {
            char_Slots[i] = new InventorySlot(null, ((i - 8) * 50) + 140 + 260, 110 + 240 + 200);
        }
    }


    private void createBagSlots() {
        for (int i = 0; i <= 9; i++) {
            bag_Slots[i] = new InventorySlot(null, (i * 50) + 40 + 770, 130 + 200);
        }
        for (int i = 10; i < 15; i++) {
            bag_Slots[i] = new InventorySlot(null, ((i - 10) * 50) + 40 + 770, 130 + 200 + 50);

        }
    }
}