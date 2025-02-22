/*
 * MIT License
 *
 * Copyright (c) 2023 gk646
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gameworld.world.objects.drops;

import gameworld.player.Player;
import gameworld.world.objects.DROP;
import gameworld.world.objects.items.ITEM;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.MainGame;
import main.system.enums.Zone;
import main.system.rendering.WorldRender;
import main.system.ui.Effects;

import java.awt.Point;

public class DRP_ChestItem extends DROP {

    private final int startX;
    private final int startY;
    private final int controlX;
    private final int controlY;
    private final int endX;
    private final int endY;
    private final Image icon;
    private float arcPosition = 0;


    public DRP_ChestItem(MainGame mg, int worldX, int worldY, Zone zone, int level, boolean left) {
        this.item = mg.dropManager.getGuaranteedRandomItem(level);
        this.zone = zone;
        this.icon = item.icon;
        blockPickup = true;
        this.worldPos = new Point(worldX - 16, worldY - 16);
        if (!left && (Math.random() > 0.5f && !mg.wRender.tileStorage[WorldRender.worldData[(worldPos.x + 40) / 48][worldPos.y / 48]].collision)) {
            this.startX = worldPos.x;
            this.startY = worldPos.y;
            controlX = worldPos.x + 25;
            controlY = worldPos.y - 100;
            endX = worldPos.x + 50;
            endY = worldPos.y;
        } else {
            this.startX = worldPos.x;
            this.startY = worldPos.y;
            controlX = worldPos.x - 25;
            controlY = worldPos.y - 100;
            endX = worldPos.x - 50;
            endY = worldPos.y;
        }
        this.size = 32;
    }

    public DRP_ChestItem(MainGame mg, int worldX, int worldY, Zone zone, boolean left, ITEM item) {
        this.item = item;
        this.zone = zone;
        this.icon = item.icon;
        blockPickup = true;
        this.worldPos = new Point(worldX - 16, worldY - 16);
        if (!left && (Math.random() > 0.5f && !mg.wRender.tileStorage[WorldRender.worldData[(worldPos.x + 40) / 48][worldPos.y / 48]].collision)) {
            this.startX = worldPos.x;
            this.startY = worldPos.y;
            controlX = worldPos.x + 25;
            controlY = worldPos.y - 100;
            endX = worldPos.x + 50;
            endY = worldPos.y;
        } else {
            this.startX = worldPos.x;
            this.startY = worldPos.y;
            controlX = worldPos.x - 25;
            controlY = worldPos.y - 100;
            endX = worldPos.x - 50;
            endY = worldPos.y;
        }
        this.size = 32;
    }


    /**
     * @param gc drawing
     */
    @Override
    public void draw(GraphicsContext gc) {
        setRarityEffect(gc);
        if (arcPosition <= 1.0) {
            worldPos.x = (int) ((1 - arcPosition) * (1 - arcPosition) * startX + 2 * (1 - arcPosition) * arcPosition * controlX + arcPosition * arcPosition * endX);
            worldPos.y = (int) ((1 - arcPosition) * (1 - arcPosition) * startY + 2 * (1 - arcPosition) * arcPosition * controlY + arcPosition * arcPosition * endY);
            worldPos.x = Math.min(Math.max(0, worldPos.x), WorldRender.worldData.length * 48);
            worldPos.y = Math.min(Math.max(0, worldPos.y), WorldRender.worldData.length * 48);
            gc.drawImage(icon, worldPos.x - Player.worldX + Player.screenX, worldPos.y - Player.worldY + Player.screenY, 32, 32);
            arcPosition += 0.01f;
        } else {
            blockPickup = false;
            gc.drawImage(icon, worldPos.x - Player.worldX + Player.screenX, worldPos.y - Player.worldY + Player.screenY, 32, 32);
        }
        gc.setEffect(null);
    }

    private void setRarityEffect(GraphicsContext gc) {
        if (item.rarity == 1) {
            gc.setEffect(Effects.rarity_1glow);
        } else if (item.rarity == 2) {
            gc.setEffect(Effects.rarity_2glow);
        } else if (item.rarity == 3) {
            gc.setEffect(Effects.rarity_3glow);
        } else if (item.rarity == 4 || item.rarity == 10) {
            gc.setEffect(Effects.rarity_4glow);
        } else if (item.rarity == 5) {
            gc.setEffect(Effects.rarity_5glow);
        }
    }

    /**
     *
     */
    @Override
    public void update() {

    }
}
