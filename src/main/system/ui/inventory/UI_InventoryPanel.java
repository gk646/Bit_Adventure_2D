package main.system.ui.inventory;

import gameworld.player.Player;
import gameworld.world.objects.drops.DRP_DroppedItem;
import gameworld.world.objects.items.ITEM;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.MainGame;
import main.system.ui.Colors;
import main.system.ui.FonT;
import main.system.ui.talentpane.TalentNode;

import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class UI_InventoryPanel {
    private static final int SLOT_SIZE = 45;
    public final Rectangle combatStatsHitBox;
    private static final int BAG_SLOTS = 15;
    private static final int CHAR_SLOTS = 10;
    public final UI_InventorySlot[] char_Slots;
    public final ArrayList<UI_InventorySlot> bag_Slots = new ArrayList<>();
    public final UI_InventorySlot[] bagEquipSlots;
    public final Rectangle wholeCharWindow;
    public final Rectangle wholeBagWindow;
    private final MainGame mg;
    private final DecimalFormat df = new DecimalFormat("#.##");
    private final Rectangle bagPanelCloser;
    public final Rectangle bagEquipSlotsBox;
    private final Rectangle charPanelMover;
    private final Rectangle bagPanelMover;
    public final Rectangle effectsHitBox;
    public boolean showCombatStats = true;
    private final Point previousMousePosition = new Point(300, 300);
    private int charPanelX = 300, grabbedIndexChar = -1, grabbedIndexBag = -1;
    private int charPanelY = 300;
    private final Point lastCharPosition = new Point(charPanelX, charPanelY);
    private int bagPanelX = 1_400;
    private int bagPanelY = 600;
    private final Point lastBagPosition = new Point(bagPanelX, bagPanelY);
    private ITEM grabbedITEM;
    private boolean node_focused;
    public boolean showBagEquipSlots;
    private final Image bag = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/resources/ui/inventory/bag.png")));

    public UI_InventoryPanel(MainGame mainGame) {
        mg = mainGame;
        char_Slots = new UI_InventorySlot[CHAR_SLOTS];
        bagEquipSlots = new UI_InventorySlot[4];
        grabbedITEM = null;
        createCharSlots();
        createBagSlots();
        bagEquipSlotsBox = new Rectangle(charPanelX, charPanelY, 24, 24);
        charPanelMover = new Rectangle(charPanelX - 40, charPanelY - 75, 438, 25);
        bagPanelMover = new Rectangle(bagPanelX, bagPanelY, 365, 50);
        bagPanelCloser = new Rectangle(bagPanelX, bagPanelY, 30, 30);
        wholeCharWindow = new Rectangle(charPanelX - 30, charPanelY - 15, 445, 615);
        wholeBagWindow = new Rectangle(bagPanelX, bagPanelY, 365, 410);
        combatStatsHitBox = new Rectangle(bagPanelX, bagPanelY, 107, 15);
        effectsHitBox = new Rectangle(bagPanelX, bagPanelY, 80, 15);
        hideCharCollision();
        hideBagCollision();
    }

    public void drawCharacterWindow(GraphicsContext gc) {
        gc.setFont(mg.ui.maruMonica);
        drawCharPanel(gc, charPanelX, charPanelY);
        lastCharPosition.x = charPanelX;
        lastCharPosition.y = charPanelY;
    }

    public void drawBagWindow(GraphicsContext gc) {
        drawBagPanel(gc, bagPanelX, bagPanelY);
        lastBagPosition.x = bagPanelX;
        lastBagPosition.y = bagPanelY;
    }

    public void drawCharTooltip(GraphicsContext g2) {
        if (grabbedITEM == null && !mg.inputH.mouse1Pressed) {
            for (UI_InventorySlot invSlot : char_Slots) {
                if (invSlot.item != null && invSlot.toolTipTimer >= 30) {
                    getTooltip(g2, invSlot, mg.inputH.lastMousePosition.x, mg.inputH.lastMousePosition.y);
                }
                if (invSlot.boundBox.contains(mg.inputH.lastMousePosition)) {
                    invSlot.toolTipTimer++;
                    break;
                } else {
                    invSlot.toolTipTimer = 0;
                }
            }
        }
    }

    public void drawBagTooltip(GraphicsContext gc) {
        if (grabbedITEM == null && !mg.inputH.mouse1Pressed) {
            for (UI_InventorySlot bagSlot : bag_Slots) {
                if (bagSlot.item != null && bagSlot.toolTipTimer >= 30) {
                    getTooltip(gc, bagSlot, mg.inputH.lastMousePosition.x, mg.inputH.lastMousePosition.y);
                    if (mg.inputH.shift_pressed) {
                        for (UI_InventorySlot slot : char_Slots) {
                            if (slot.item != null && slot.item.type == bagSlot.item.type) {
                                getTooltip(gc, slot, (int) (mg.inputH.lastMousePosition.x - MainGame.SCREEN_HEIGHT * 0.232), (mg.inputH.lastMousePosition.y));
                            }
                        }
                    }
                }
                if (bagSlot.boundBox.contains(mg.inputH.lastMousePosition)) {
                    bagSlot.toolTipTimer++;
                    break;
                } else {
                    bagSlot.toolTipTimer = 0;
                }
            }
        }
    }

    private void drawCharPanel(GraphicsContext gc, int startX, int startY) {
        drawCharacterBackground(gc, startX, startY);
        drawCharacterSlots(gc, startX, startY);
    }

    private void drawBagPanel(GraphicsContext gc, int startX, int startY) {
        drawBagBackground(gc, startX, startY);
        drawBagSlots(gc, startX, (int) (startY + MainGame.SCREEN_HEIGHT * 0.007f));
    }

    private void getTooltip(GraphicsContext gc, UI_InventorySlot invSlot, int startX, int startY) {
        //BACKGROUND
        gc.setFill(Colors.LightGrey);
        gc.fillRoundRect(startX - (MainGame.SCREEN_HEIGHT * 0.238), startY - (MainGame.SCREEN_HEIGHT * 0.314), MainGame.SCREEN_HEIGHT * 0.23, MainGame.SCREEN_HEIGHT * 0.324f, 15, 15);
        //OUTLINE
        setRarityColor(gc, invSlot);
        gc.setLineWidth(1);
        gc.strokeRoundRect(startX - (MainGame.SCREEN_HEIGHT * 0.235), startY - (MainGame.SCREEN_HEIGHT * 0.311), MainGame.SCREEN_HEIGHT * 0.224, MainGame.SCREEN_HEIGHT * 0.318f, 15, 15);
        //NAME
        setRarityColor(gc, invSlot);
        gc.setFont(FonT.minecraftRegular20);
        gc.fillText(invSlot.item.name, startX - MainGame.SCREEN_HEIGHT * 0.229f, startY - MainGame.SCREEN_HEIGHT * 0.268f);
        //Quality
        applyQualityColor(invSlot, gc);
        gc.setFont(FonT.minecraftItalic15);
        if (invSlot.item.quality < 100) {
            gc.fillText(invSlot.item.quality + "%", startX - MainGame.SCREEN_HEIGHT * 0.039_8f, startY - MainGame.SCREEN_HEIGHT * 0.299f);
        } else {
            gc.fillText(invSlot.item.quality + "%", startX - MainGame.SCREEN_HEIGHT * 0.047_3f, startY - MainGame.SCREEN_HEIGHT * 0.299f);
        }
        //STATS
        gc.setFill(Colors.darkBackground);
        if (!(invSlot.item.type == 'M')) {
            gc.setFont(FonT.minecraftItalic15);
            gc.fillText("INT: " + invSlot.item.intellect, startX - MainGame.SCREEN_HEIGHT * 0.230f, startY - MainGame.SCREEN_HEIGHT * 0.236f);
            gc.fillText("VIT: " + invSlot.item.vitality, startX - MainGame.SCREEN_HEIGHT * 0.230f, startY - MainGame.SCREEN_HEIGHT * 0.222f);
            gc.fillText("WIS: " + invSlot.item.wisdom, startX - MainGame.SCREEN_HEIGHT * 0.230f, startY - MainGame.SCREEN_HEIGHT * 0.208f);

            gc.fillText("AGI: " + invSlot.item.agility, startX - MainGame.SCREEN_HEIGHT * 0.160f, startY - MainGame.SCREEN_HEIGHT * 0.236f);
            gc.fillText("LUC: " + invSlot.item.luck, startX - MainGame.SCREEN_HEIGHT * 0.160f, startY - MainGame.SCREEN_HEIGHT * 0.222f);
            gc.fillText("CHA: " + invSlot.item.charisma, startX - MainGame.SCREEN_HEIGHT * 0.160f, startY - MainGame.SCREEN_HEIGHT * 0.208f);

            gc.fillText("END: " + invSlot.item.endurance, startX - MainGame.SCREEN_HEIGHT * 0.086f, startY - MainGame.SCREEN_HEIGHT * 0.236f);
            gc.fillText("STR: " + invSlot.item.strength, startX - MainGame.SCREEN_HEIGHT * 0.086f, startY - MainGame.SCREEN_HEIGHT * 0.222f);
            gc.fillText("FOC: " + invSlot.item.focus, startX - MainGame.SCREEN_HEIGHT * 0.086f, startY - MainGame.SCREEN_HEIGHT * 0.208f);

      /*  INT - Int
        WIS - Wis
        VIT - Vit
        AGI - Agi
        LUC - Luc
        CHA - Cha
        END - End
        STR - Str
        FOC - Foc
        MainGame.SCREEN_HEIGHT*0.8f
        x / 1080
       */
            //EFFECTS
            gc.fillText("Effects: ", startX - MainGame.SCREEN_HEIGHT * 0.229f, startY - MainGame.SCREEN_HEIGHT * 0.130f);
        }
        //DESCRIPTION
        gc.setFont(FonT.minecraftItalic12);
        int stringY = (int) (startY - MainGame.SCREEN_HEIGHT * 0.065f);
        for (String string : invSlot.item.description.split("\n")) {
            gc.fillText(string, startX - MainGame.SCREEN_HEIGHT * 0.231f, stringY += MainGame.SCREEN_HEIGHT * 0.010f);
        }

        gc.setFont(FonT.minecraftItalic14);
        //TYPE
        printItemType(gc, invSlot);


        //LEVEL
        gc.fillText("ilvl: " + invSlot.item.level, startX - 248, startY - 322);
        //Durability
        gc.fillText("D: " + invSlot.item.durability, startX - 251, startY + 3);
        //ID
        gc.fillText("ID: " + String.format("%04d", invSlot.item.i_id) + invSlot.item.type, startX - 78, startY + 3);
    }

    private void setRarityColor(GraphicsContext gc, UI_InventorySlot slot) {
        if (slot.item != null) {
            if (slot.item.rarity == 1) {
                gc.setStroke(Colors.NormalGrey);
                gc.setFill(Colors.NormalGrey);
            } else if (slot.item.rarity == 2) {
                gc.setStroke(Colors.rareColor);
                gc.setFill(Colors.rareColor);
            } else if (slot.item.rarity == 3) {
                gc.setStroke(Colors.epicColor);
                gc.setFill(Colors.epicColor);
            } else if (slot.item.rarity == 4) {
                gc.setStroke(Colors.legendaryColor);
                gc.setFill(Colors.legendaryColor);
            } else if (slot.item.rarity == 5) {
                gc.setStroke(Colors.setItem);
                gc.setFill(Colors.setItem);
            } else if (slot.item.rarity == 10) {
                gc.setStroke(Colors.legendaryColor);
                gc.setFill(Colors.legendaryColor);
            }
        } else {
            gc.setStroke(Colors.darkBackground);
            gc.setFill(Colors.darkBackground);
        }
    }

    private void printItemType(GraphicsContext gc, UI_InventorySlot slot) {
        int xPosition = (int) (mg.inputH.lastMousePosition.x - MainGame.SCREEN_HEIGHT * 0.148f);
        int yPosition = (int) (mg.inputH.lastMousePosition.y + MainGame.SCREEN_HEIGHT * 0.002_7f);
        switch (slot.item.type) {
            case 'H' -> gc.fillText("Helm", xPosition, yPosition);
            case 'C' -> gc.fillText("Chest", xPosition, yPosition);
            case 'P' -> gc.fillText("Pants", xPosition, yPosition);
            case 'B' -> gc.fillText("Boots", xPosition - MainGame.SCREEN_HEIGHT * 0.004, yPosition);
            case 'A' -> gc.fillText("Amulet", xPosition - MainGame.SCREEN_HEIGHT * 0.009f, yPosition);
            case 'R' -> gc.fillText("Ring", xPosition, yPosition);
            case 'T' -> gc.fillText("Relic", xPosition - MainGame.SCREEN_HEIGHT * 0.004, yPosition);
            case 'W' -> gc.fillText("One-Handed", xPosition - MainGame.SCREEN_HEIGHT * 0.017f, yPosition);
            case '2' -> gc.fillText("Two-Handed", xPosition - MainGame.SCREEN_HEIGHT * 0.019f, yPosition);
            case 'O' -> gc.fillText("Offhand", xPosition - MainGame.SCREEN_HEIGHT * 0.004, yPosition);
            default -> gc.fillText("Misc", xPosition, yPosition);
        }
    }

    public void drawDragAndDrop(GraphicsContext gc) {
        if (grabbedITEM != null) {
            gc.drawImage(grabbedITEM.icon, mg.inputH.lastMousePosition.x - SLOT_SIZE / 2.0f, mg.inputH.lastMousePosition.y - SLOT_SIZE / 2.0f, SLOT_SIZE, SLOT_SIZE);
        }
        if (grabbedITEM == null && mg.showChar) {
            for (int i = 0; i < char_Slots.length; i++) {
                if (char_Slots[i].boundBox.contains(mg.inputH.lastMousePosition) && char_Slots[i].item != null) {
                    if (mg.inputH.X_pressed) {
                        mg.WORLD_DROPS.add(new DRP_DroppedItem(mg, (int) (Player.worldX - 50), (int) Player.worldY, char_Slots[i].item));
                        char_Slots[i].item = null;
                    } else if (mg.inputH.mouse1Pressed) {
                        if (mg.inputH.shift_pressed && mg.showBag) {
                            for (UI_InventorySlot slot : bag_Slots) {
                                if (slot.item == null) {
                                    slot.item = char_Slots[i].item;
                                    char_Slots[i].item = null;
                                    mg.player.updateEquippedItems();
                                    break;
                                }
                            }
                            continue;
                        }
                        char_Slots[i].grabbed = true;
                        grabbedITEM = char_Slots[i].item;
                        grabbedIndexChar = i;
                        char_Slots[i].item = null;
                    }
                    mg.player.updateEquippedItems();
                }
            }
        }
        if (grabbedITEM == null && mg.showBag) {
            for (int i = 0; i < bag_Slots.size(); i++) {
                if (bag_Slots.get(i).boundBox.contains(mg.inputH.lastMousePosition) && bag_Slots.get(i).item != null) {
                    if (mg.inputH.X_pressed) {
                        mg.WORLD_DROPS.add(new DRP_DroppedItem(mg, (int) (Player.worldX - 50), (int) Player.worldY, bag_Slots.get(i).item));
                        bag_Slots.get(i).item = null;
                    } else if (mg.inputH.mouse1Pressed) {
                        if (mg.inputH.shift_pressed && mg.showChar) {
                            for (UI_InventorySlot slot : char_Slots) {
                                if (slot.item == null && slot.type.equals(String.valueOf(bag_Slots.get(i).item.type))) {
                                    slot.item = bag_Slots.get(i).item;
                                    bag_Slots.get(i).item = null;
                                    mg.player.updateEquippedItems();
                                    break;
                                } else if (slot.type.contains(String.valueOf(bag_Slots.get(i).item.type))) {
                                    ITEM placeholder = slot.item;
                                    slot.item = bag_Slots.get(i).item;
                                    bag_Slots.get(i).item = placeholder;
                                    mg.player.updateEquippedItems();
                                    mg.inputH.mouse1Pressed = false;
                                    break;
                                }
                            }
                            break;
                        }
                        bag_Slots.get(i).grabbed = true;
                        grabbedITEM = bag_Slots.get(i).item;
                        grabbedIndexBag = i;
                        bag_Slots.get(i).item = null;
                    }
                }
            }
        }
        if (grabbedITEM != null && !mg.inputH.mouse1Pressed) {
            if (mg.showChar) {
                for (UI_InventorySlot invSlot : char_Slots) {
                    if (invSlot.boundBox.contains(mg.inputH.lastMousePosition) && invSlot.type.contains(String.valueOf(grabbedITEM.type))) {
                        if (invSlot.item != null) {
                            if (grabbedIndexChar != -1) {
                                char_Slots[grabbedIndexChar].item = invSlot.item;
                            }
                            if (grabbedIndexBag != -1) {
                                bag_Slots.get(grabbedIndexBag).item = invSlot.item;
                            }
                        }
                        invSlot.item = grabbedITEM;
                        mg.player.updateEquippedItems();
                        grabbedITEM = null;
                    }
                    invSlot.grabbed = false;
                }
            }
            if (mg.showBag) {
                for (UI_InventorySlot bagSlot : bag_Slots) {
                    if (bagSlot.boundBox.contains(mg.inputH.lastMousePosition)) {
                        if (bagSlot.item != null) {
                            if (grabbedIndexChar != -1 && char_Slots[grabbedIndexChar].type.equals(String.valueOf(grabbedITEM.type))) {
                                char_Slots[grabbedIndexChar].item = bagSlot.item;
                            } else {
                                break;
                            }
                            if (grabbedIndexBag != -1) {
                                bag_Slots.get(grabbedIndexBag).item = bagSlot.item;
                            }
                        }
                        mg.player.updateEquippedItems();
                        bagSlot.item = grabbedITEM;
                        grabbedITEM = null;
                    }
                    bagSlot.grabbed = false;
                }
            }
            if (grabbedIndexChar != -1 && grabbedITEM != null) {
                char_Slots[grabbedIndexChar].item = grabbedITEM;
            }
            if (grabbedIndexBag != -1 && grabbedITEM != null) {
                bag_Slots.get(grabbedIndexBag).item = grabbedITEM;
            }
            grabbedIndexChar = -1;
            grabbedIndexBag = -1;
            if (grabbedITEM != null) {
                grabbedITEM = null;
            }
        }
    }

    public void interactWithWindows() {
        if (char_Slots[8].item != null && char_Slots[8].item.type == '2') {
            char_Slots[9].type = ",";
        } else {
            char_Slots[9].type = "O";
        }
        node_focused = false;
        if (mg.inputH.mouse1Pressed && charPanelMover.contains(mg.inputH.lastMousePosition)) {
            charPanelX += mg.inputH.lastMousePosition.x - previousMousePosition.x;
            charPanelY += mg.inputH.lastMousePosition.y - previousMousePosition.y;
            charPanelMover.x = charPanelX - 40;
            charPanelMover.y = charPanelY - 75;
        } else if (mg.inputH.mouse1Pressed && bagPanelMover.contains(mg.inputH.lastMousePosition)) {
            bagPanelX += mg.inputH.lastMousePosition.x - previousMousePosition.x;
            bagPanelY += mg.inputH.lastMousePosition.y - previousMousePosition.y;
            bagPanelMover.x = bagPanelX;
            bagPanelMover.y = bagPanelY - 10;
        } else if (mg.talentP.wholeTalentWindow.contains(mg.inputH.lastMousePosition)) {
            for (TalentNode node : mg.talentP.talent_Nodes) {
                if (node != null) {
                    if (node.boundBox.contains(mg.inputH.lastMousePosition)) {
                        if (mg.inputH.mouse1Pressed) {
                            if (mg.talentP.checkValidTalent(node) && mg.talentP.pointsToSpend > 0 && !node.activated) {
                                node.activated = true;
                                mg.talentP.spendTalentPoint();
                            }
                        }
                        node_focused = true;
                    }
                }
            }
            if (!node_focused && mg.inputH.mouse1Pressed) {
                mg.talentP.talentPanelX += mg.inputH.lastMousePosition.x - previousMousePosition.x;
                mg.talentP.talentPanelY += mg.inputH.lastMousePosition.y - previousMousePosition.y;
            }
        }
        previousMousePosition.x = mg.inputH.lastMousePosition.x;
        previousMousePosition.y = mg.inputH.lastMousePosition.y;
    }

    private void drawCharacterBackground(GraphicsContext gc, int startX, int startY) {
        //inventory background
        //big background
        wholeCharWindow.x = startX - 47;
        wholeCharWindow.y = startY - 78;
        gc.setFill(Colors.LightGrey);
        gc.fillRoundRect(startX - 50, startY - 80, 450, 620, 35, 35);
        //outline
        gc.setStroke(Colors.darkBackground);
        gc.setLineWidth(5);
        gc.strokeRoundRect(startX - 45, startY - 75, 440, 620 - 10, 30, 30);
        gc.setFill(Colors.mediumLightGrey);
        gc.fillRoundRect(startX - 42, startY - 75, 434, 22, 15, 15);
        gc.setStroke(Colors.darkBackground);
        gc.strokeRoundRect(startX - 42, startY - 75, 434, 22, 15, 15);
        gc.setFill(Colors.darkBackground);
        gc.setFont(FonT.minecraftBold13);
        gc.fillText("Character", startX + 138, startY - 61);
        gc.fillText("No Title", startX + 147, startY);
        gc.fillText("Level " + mg.player.level, 135 + startX, 20 + startY);

        //character image outline
        gc.setLineWidth(2);
        gc.strokeRoundRect(75 + startX, startY + 40, 200, 250, 25, 25);

        //game world.player image
        gc.drawImage(mg.player.entityImage1, startX + 135, startY + 135, 60, 120);
        //Stats Text
        gc.strokeRoundRect(startX - 14, startY + 375, 187, 150, 15, 15);
        gc.strokeRoundRect(startX + 177, startY + 375, 187, 150, 15, 15);
        gc.fillText("Base  Stats", startX - 5, startY + 370);

        //stats
        gc.fillText("Intelligence: " + mg.player.intellect, startX - 5, startY + 393);
        gc.fillText("Wisdom: " + mg.player.wisdom, startX - 5, startY + 408);
        gc.fillText("Vitality: " + mg.player.vitality, startX - 5, startY + 423);
        gc.fillText("Agility: " + mg.player.agility, startX - 5, startY + 438);

        gc.fillText("Luck: " + mg.player.luck, startX - 5, startY + 453);
        gc.fillText("Charisma: " + mg.player.charisma, startX - 5, startY + 468);
        gc.fillText("Endurance: " + mg.player.endurance, startX - 5, startY + 483);
        gc.fillText("Strength: " + mg.player.strength, startX - 5, startY + 498);
        gc.fillText("Focus: " + mg.player.focus, startX - 5, startY + 513);

        // second panel stats
        gc.fillText("Combat Stats", startX + 185, startY + 372);
        gc.fillText("Effects", startX + 292, startY + 372);

        gc.strokeRoundRect(startX + 180, startY + 360, 107, 15, 10, 10);
        gc.strokeRoundRect(startX + 287, startY + 360, 68, 15, 10, 10);
        combatStatsHitBox.x = startX + 180;
        combatStatsHitBox.y = startY + 360;
        effectsHitBox.x = startX + 287;
        effectsHitBox.y = startY + 360;
        if (showCombatStats) {
            drawCombatStats(gc, startX, startY);
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(startX + 180, startY + 360, 107, 15, 10, 10);
            gc.setFill(Colors.darkBackground);
            gc.strokeRoundRect(startX + 180, startY + 360, 107, 15, 10, 10);
            gc.fillText("Combat Stats", startX + 185, startY + 372);
        } else {
            drawEffects(gc, startX, startY);
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(startX + 287, startY + 360, 68, 15, 10, 10);
            gc.setFill(Colors.darkBackground);
            gc.strokeRoundRect(startX + 287, startY + 360, 68, 15, 10, 10);
            gc.fillText("Effects", startX + 292, startY + 372);
        }
    }

    private void drawCombatStats(GraphicsContext gc, int startX, int startY) {
        gc.setFill(Colors.darkBackground);
        gc.fillText("Max-Health: " + mg.player.maxHealth, startX + 182, startY + 393);
        gc.fillText("Max-Mana: " + mg.player.maxMana, startX + 182, startY + 408);
        gc.fillText("ManaREG: " + df.format(mg.player.manaRegeneration * 60) + "/s", startX + 182, startY + 423);
        gc.fillText("HealthREG: " + df.format(mg.player.healthRegeneration * 60) + "/s", startX + 182, startY + 438);
        gc.fillText("Armour: " + mg.player.armour, startX + 182, startY + 453);
        gc.fillText("Critchance: " + mg.player.critChance, startX + 182, startY + 468);
        gc.fillText("Resist %: " + mg.player.resistChance, startX + 182, startY + 483);
        gc.fillText("Carry-weight: " + mg.player.carryWeight, startX + 182, startY + 498);
        gc.fillText("MovementSpeed: " + mg.player.playerMovementSpeed, startX + 182, startY + 512);
    }

    private void drawEffects(GraphicsContext gc, int startX, int startY) {
        gc.fillText("hello", startX + 182, startY + 420);
    }

    private void drawCharacterSlots(GraphicsContext gc, int startX, int startY) {
        gc.setLineWidth(2);
        for (int i = 0; i <= 3; i++) {
            char_Slots[i].boundBox.x = 21 + startX;
            char_Slots[i].boundBox.y = (i * 50 + 65 + startY);
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(21 + startX, i * 50 + 65 + startY, 45, 45, 20, 20);
            setRarityColor(gc, char_Slots[i]);
            char_Slots[i].drawSlot(gc, 21 + startX, (i * 50 + 65 + startY));
            if (char_Slots[i].item != null && !char_Slots[i].grabbed) {
                char_Slots[i].drawIcon(gc, 21 + startX, ((i * 50) + 65 + startY), SLOT_SIZE);
            }
        }
        for (int i = 4; i <= 7; i++) {
            char_Slots[i].boundBox.x = 283 + startX;
            char_Slots[i].boundBox.y = (((i - 4) * 50) + 65 + startY);
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(283 + startX, ((i - 4) * 50) + 65 + startY, 45, 45, 20, 20);
            setRarityColor(gc, char_Slots[i]);
            char_Slots[i].drawSlot(gc, 283 + startX, (((i - 4) * 50) + 65 + startY));
            if (char_Slots[i].item != null && !char_Slots[i].grabbed) {
                char_Slots[i].drawIcon(gc, 283 + startX, ((i - 4) * 50) + 65 + startY, SLOT_SIZE);
            }
        }
        for (int i = 8; i <= 9; i++) {
            char_Slots[i].boundBox.x = ((i - 8) * 57) + 128 + startX;
            char_Slots[i].boundBox.y = 297 + startY;
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(((i - 8) * 57) + 128 + startX, 297 + startY, 45, 45, 20, 20);
            setRarityColor(gc, char_Slots[i]);
            char_Slots[i].drawSlot(gc, ((i - 8) * 57) + 128 + startX, 297 + startY);
            if (char_Slots[i].item != null && !char_Slots[i].grabbed) {
                char_Slots[i].drawIcon(gc, ((i - 8) * 57) + 128 + startX, 297 + startY, SLOT_SIZE);
            }
        }
    }

    private void drawBagBackground(GraphicsContext gc, int startX, int startY) {
        wholeBagWindow.x = startX;
        wholeBagWindow.y = startY;

        //background
        if (showBagEquipSlots) {
            bagEquipSlotsBox.x = startX + 11;
            bagEquipSlotsBox.y = startY + 1;
            gc.setFill(Colors.LightGrey);
            gc.fillRoundRect(startX, startY - 30, 365, 440, 25, 25);
            //background header
            gc.setFont(FonT.minecraftBold14);
            gc.setFill(Colors.mediumLightGrey);
            gc.fillRoundRect(startX + 5, startY - 25, 355, 20, 15, 15);
            gc.setLineWidth(3);
            gc.setStroke(Colors.darkBackground);
            //header outline
            gc.strokeRoundRect(startX + 5, startY - 25, 355, 20, 15, 15);
            //outline
            gc.strokeRoundRect(startX + 5, startY - 25, 355, 430, 15, 15);
            gc.setLineWidth(2);
            //feature pane
            gc.setFill(Colors.darkBackground);
            gc.fillText("Bags", startX + 160, startY - 11);
            gc.strokeRoundRect(startX + 10, startY, 25, 25, 5, 5);
            gc.drawImage(bag, startX + 11, startY + 1);
            for (int i = 0; i < 4; i++) {
                bagEquipSlots[i].boundBox.x = i * 26 + startX + 11;
                bagEquipSlots[i].boundBox.y = startY + 26;
                gc.setFill(Colors.mediumVeryLight);
                gc.fillRoundRect(i * 26 + startX + 11, startY + 26, 25, 25, 15, 15);
                setRarityColor(gc, bagEquipSlots[i]);
                bagEquipSlots[i].drawSlot(gc, i * 26 + startX + 11, startY + 26);
                if (bagEquipSlots[i] != null && !bagEquipSlots[i].grabbed) {
                    bagEquipSlots[i].drawIcon(gc, i * 26 + startX + 11, startY + 26, 25);
                }
            }
        } else {
            bagEquipSlotsBox.x = startX + 11;
            bagEquipSlotsBox.y = startY + 31;
            gc.setFill(Colors.LightGrey);
            gc.fillRoundRect(startX, startY, 365, 410, 25, 25);
            //background header
            gc.setFont(FonT.minecraftBold14);
            gc.setFill(Colors.mediumLightGrey);
            gc.fillRoundRect(startX + 5, startY + 5, 355, 20, 15, 15);

            gc.setLineWidth(3);
            gc.setStroke(Colors.darkBackground);
            //header outline
            gc.strokeRoundRect(startX + 5, startY + 5, 355, 20, 15, 15);
            //outline
            gc.strokeRoundRect(startX + 5, startY + 5, 355, 400, 15, 15);
            gc.setLineWidth(2);
            //feature pane
            // gc.strokeRoundRect(startX + 5, startY + 31, 355, 18, 15, 15);
            gc.setFill(Colors.darkBackground);
            gc.fillText("Bags", startX + 160, startY + 19);
            gc.strokeRoundRect(startX + 10, startY + 30, 25, 25, 5, 5);
            gc.drawImage(bag, startX + 11, startY + 31);
        }
    }

    private void drawBagSlots(GraphicsContext gc, int startX, int startY) {
        int i = 0;
        int y;
        gc.setLineWidth(2);
        for (UI_InventorySlot slot : bag_Slots) {
            y = 50 * (i / 7);
            slot.boundBox.x = i % 7 * 50 + startX + 10;
            slot.boundBox.y = 60 + y + startY;
            gc.setFill(Colors.mediumVeryLight);
            gc.fillRoundRect(i % 7 * 50 + 10 + startX, 60 + y + startY, 45, 45, 20, 20);
            setRarityColor(gc, slot);
            slot.drawSlot(gc, i % 7 * 50 + startX + 10, 60 + y + startY);
            if (slot.item != null && !slot.grabbed) {
                slot.drawIcon(gc, i % 7 * 50 + startX + 10, 60 + y + startY, SLOT_SIZE);
            }
            i++;
        }
    }

    public void hideCharCollision() {
        wholeCharWindow.x = -1_000;
        wholeCharWindow.y = -1_000;
    }

    private void applyQualityColor(UI_InventorySlot invSlot, GraphicsContext gc) {
        if (invSlot.item.quality < 90) {
            gc.setFill(Colors.NormalGrey);
        }
        if (invSlot.item.quality >= 90) {
            gc.setFill(Colors.mediumQuality);
        }
        if (invSlot.item.quality == 100) {
            gc.setFill(Colors.highQuality);
        }
    }

    public void hideBagCollision() {
        wholeBagWindow.x = -1_000;
        wholeBagWindow.y = -1_000;
    }

    public void resetCharCollision() {
        wholeCharWindow.x = lastCharPosition.x;
        wholeCharWindow.y = lastCharPosition.y;
    }

    public void resetBagCollision() {

        wholeBagWindow.x = lastBagPosition.x;
        wholeBagWindow.y = lastBagPosition.y;
    }

    private void createCharSlots() {
        for (int i = 0; i <= 3; i++) {
            char_Slots[i] = new UI_InventorySlot(null, 40 + 260, (i * 50) + 310);
        }
        for (int i = 4; i <= 7; i++) {
            char_Slots[i] = new UI_InventorySlot(null, 40 + 270 + 260, ((i - 4) * 50) + 310);
        }
        for (int i = 8; i <= 9; i++) {
            char_Slots[i] = new UI_InventorySlot(null, ((i - 8) * 50) + 160 + 260, 550);
        }
        char_Slots[0].type = "H";
        char_Slots[1].type = "C";
        char_Slots[2].type = "P";
        char_Slots[3].type = "B";
        char_Slots[4].type = "A";
        char_Slots[5].type = "R";
        char_Slots[6].type = "R";
        char_Slots[7].type = "T";
        char_Slots[8].type = "W2";
        char_Slots[9].type = "O";
    }

    private void createBagSlots() {
        for (int i = 0; i <= 15; i++) {
            bag_Slots.add(new UI_InventorySlot(null, (i * 50) + 810, 330));
        }
    }

    public void addBagSlots(int size) {
        bag_Slots.add(new UI_InventorySlot(null, 1, 1));
    }
}