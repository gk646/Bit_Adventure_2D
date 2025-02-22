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

package gameworld.entities.monsters;

import gameworld.entities.ENTITY;
import gameworld.entities.loadinghelper.EntityPreloader;
import gameworld.player.Player;
import gameworld.player.abilities.enemies.PRJ_AttackCone;
import javafx.scene.canvas.GraphicsContext;
import main.MainGame;
import main.system.enums.Zone;

import java.awt.Point;
import java.awt.Rectangle;


public class ENT_SkeletonShield extends ENTITY {
    private boolean attack1, attack2, attack3;

    /**
     * Standard enemy  / hits you when he's close
     *
     * @param level  the level / also sets hp of the enemy
     * @param worldX coordinates X
     * @param worldY coordinates Y
     */
    public ENT_SkeletonShield(MainGame mg, int worldX, int worldY, int level, Zone zone) {
        this.mg = mg;
        this.animation = EntityPreloader.skeletonShield;
        this.zone = zone;
        //Setting default values
        this.maxHealth = (9 + level) * (level + level - 1);
        if (level == 1) {
            maxHealth = 5;
        }
        this.health = maxHealth;
        this.worldX = worldX;
        this.worldY = worldY;
        movementSpeed = 2;
        this.level = level;
        direction = "updownleftright";
        this.entityHeight = 48;
        this.entityWidth = 48;
        this.collisionBox = new Rectangle(3, 3, 42, 42);
        this.onPath = false;
        this.searchTicks = 60;
        screenX = (int) (worldX - Player.worldX + Player.screenX);
        screenY = (int) (worldY - Player.worldY + Player.screenY);
    }

    @Override
    public void update() {
        super.update();
        if (collidingWithPlayer && !onPath && !attack2 && !attack1) {
            int length = mg.PROJECTILES.size();
            Point position = new Point((int) worldX, (int) worldY);
            for (int i = 0; i < length; i++) {
                if (position.distance(mg.PROJECTILES.get(i).worldPos) < 300) {
                    attack2 = true;
                }
            }
            if (Math.random() < 0.33f) {
                mg.PROJECTILES.add(new PRJ_AttackCone((int) worldX, (int) worldY, 75, 48, 48, 0, 0, 2 * level));
                attack1 = true;
            }
            //animation.playRandomSoundFromXToIndex(0, 3);
            spriteCounter = 0;
            collidingWithPlayer = false;
        }
        onPath = !attack2 && !attack1;
        if (onPath) {
            getNearestPlayer();
            searchPath(goalCol, goalRow, 16);
        }
        hitDelay++;
        searchTicks++;
    }


    @Override
    public void draw(GraphicsContext gc) {
        screenX = (int) (worldX - Player.worldX + Player.screenX);
        screenY = (int) (worldY - Player.worldY + Player.screenY);
        if (dead) {
            drawDeath(gc);
        } else if (attack1) {
            drawAttack1(gc);
        } else if (attack2) {
            drawAttack2(gc);
        } else {
            if (onPath) {
                drawWalk(gc);
            } else {
                drawIdle(gc);
            }
        }
        spriteCounter++;
        drawBuffsAndDeBuffs(gc);
    }


    private void drawIdle(GraphicsContext gc) {
        switch (spriteCounter % 120 / 30) {
            case 0 -> gc.drawImage(animation.idle.get(0), screenX - 57, screenY - 48);
            case 1 -> gc.drawImage(animation.idle.get(1), screenX - 57, screenY - 48);
            case 2 -> gc.drawImage(animation.idle.get(2), screenX - 57, screenY - 48);
            case 3 -> gc.drawImage(animation.idle.get(3), screenX - 57, screenY - 48);
        }
    }

    private void drawWalk(GraphicsContext gc) {
        switch (spriteCounter % 80 / 20) {
            case 0 -> gc.drawImage(animation.walk.get(0), screenX - 57, screenY - 48);
            case 1 -> gc.drawImage(animation.walk.get(1), screenX - 57, screenY - 48);
            case 2 -> gc.drawImage(animation.walk.get(2), screenX - 57, screenY - 48);
            case 3 -> gc.drawImage(animation.walk.get(3), screenX - 57, screenY - 48);
        }
    }

    private void drawAttack1(GraphicsContext gc) {
        switch (spriteCounter % 225 / 25) {
            case 0 -> gc.drawImage(animation.attack1.get(0), screenX - 57, screenY - 48);
            case 1 -> gc.drawImage(animation.attack1.get(1), screenX - 57, screenY - 48);
            case 2 -> gc.drawImage(animation.attack1.get(2), screenX - 57, screenY - 48);
            case 3 -> gc.drawImage(animation.attack1.get(3), screenX - 57, screenY - 48);
            case 4 -> gc.drawImage(animation.attack1.get(4), screenX - 57, screenY - 48);
            case 5 -> gc.drawImage(animation.attack1.get(5), screenX - 57, screenY - 48);
            case 6 -> gc.drawImage(animation.attack1.get(6), screenX - 57, screenY - 48);
            case 7 -> gc.drawImage(animation.attack1.get(7), screenX - 57, screenY - 48);
            case 8 -> attack1 = false;
        }
    }

    private void drawAttack2(GraphicsContext gc) {
        switch (spriteCounter % 150 / 30) {
            case 0 -> gc.drawImage(animation.special.get(0), screenX - 57, screenY - 48);
            case 1 -> gc.drawImage(animation.special.get(1), screenX - 57, screenY - 48);
            case 2 -> gc.drawImage(animation.special.get(2), screenX - 57, screenY - 48);
            case 3 -> gc.drawImage(animation.special.get(3), screenX - 57, screenY - 48);
            case 4 -> attack2 = false;
        }
    }


    private void drawDeath(GraphicsContext gc) {
        switch (spriteCounter % 175 / 35) {
            case 0 -> gc.drawImage(animation.dead.get(0), screenX - 32, screenY - 52);
            case 1 -> gc.drawImage(animation.dead.get(1), screenX - 32, screenY - 52);
            case 2 -> gc.drawImage(animation.dead.get(2), screenX - 32, screenY - 52);
            case 3 -> gc.drawImage(animation.dead.get(3), screenX - 32, screenY - 52);
            case 4 -> AfterAnimationDead = true;
        }
    }

    @Override
    public void playGetHitSound() {
        if (System.currentTimeMillis() - timeSinceLastDamageSound >= 3_500) {
            timeSinceLastDamageSound = System.currentTimeMillis();
            //animation.playGetHitSound(4);
        }
    }
}

