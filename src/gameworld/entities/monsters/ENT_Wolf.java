package gameworld.entities.monsters;

import gameworld.entities.ENTITY;
import gameworld.entities.loadinghelper.ResourceLoaderEntity;
import gameworld.player.Player;
import javafx.scene.canvas.GraphicsContext;
import main.MainGame;
import main.system.enums.Zone;

import java.awt.Rectangle;


public class ENT_Wolf extends ENTITY {
    private boolean attack1, attack2, attack3;

    /**
     * Standard enemy  / hits you when he's close
     *
     * @param level  the level / also sets hp of the enemy
     * @param worldX coordinates X
     * @param worldY coordinates Y
     */
    public ENT_Wolf(MainGame mg, int worldX, int worldY, int level, Zone zone) {
        this.mg = mg;
        this.animation = new ResourceLoaderEntity("enemies/wolf");
        animation.load();
        this.zone = zone;
        //Setting default values
        this.maxHealth = (9 + level) * (level + level - 1);
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
        if (collidingWithPlayer && !onPath && !attack1) {
            attack1 = true;
            //animation.playRandomSoundFromXToIndex(0, 3);
            spriteCounter = 0;
            collidingWithPlayer = false;
        }
        if (!attack1) {
            onPath = true;
            getNearestPlayer();
            searchPath(goalCol, goalRow, 16);
        }
        hitDelay++;
        searchTicks++;
        if (hpBarCounter >= 600) {
            hpBarOn = false;
            hpBarCounter = 0;
        } else if (hpBarOn) {
            hpBarCounter++;
        }
    }


    @Override
    public void draw(GraphicsContext gc) {
        screenX = (int) (worldX - Player.worldX + Player.screenX);
        screenY = (int) (worldY - Player.worldY + Player.screenY);
        if (dead) {
            drawDeath(gc);
        } else if (attack1) {
            drawAttack1(gc);
        } else {
            if (onPath) {
                drawWalk(gc);
            } else {
                drawIdle(gc);
            }
        }
        spriteCounter++;
    }


    private void drawIdle(GraphicsContext gc) {
        switch (spriteCounter % 120 / 30) {
            case 0 -> gc.drawImage(animation.idle.get(0), screenX, screenY);
            case 1 -> gc.drawImage(animation.idle.get(1), screenX, screenY);
            case 2 -> gc.drawImage(animation.idle.get(2), screenX, screenY);
            case 3 -> gc.drawImage(animation.idle.get(3), screenX, screenY);
        }
    }

    private void drawWalk(GraphicsContext gc) {
        switch (spriteCounter % 60 / 15) {
            case 0 -> gc.drawImage(animation.walk.get(0), screenX, screenY);
            case 1 -> gc.drawImage(animation.walk.get(1), screenX, screenY);
            case 2 -> gc.drawImage(animation.walk.get(2), screenX, screenY);
            case 3 -> gc.drawImage(animation.walk.get(3), screenX, screenY);
        }
    }

    private void drawAttack1(GraphicsContext gc) {
        switch (spriteCounter % 200 / 25) {
            case 0 -> gc.drawImage(animation.attack1.get(0), screenX, screenY);
            case 1 -> gc.drawImage(animation.attack1.get(1), screenX, screenY);
            case 2 -> gc.drawImage(animation.attack1.get(2), screenX, screenY);
            case 3 -> gc.drawImage(animation.attack1.get(3), screenX, screenY);
            case 4 -> gc.drawImage(animation.attack1.get(4), screenX, screenY);
            case 5 -> gc.drawImage(animation.attack1.get(5), screenX, screenY);
            case 6 -> attack1 = false;
        }
    }

    private void drawDeath(GraphicsContext gc) {
        switch (spriteCounter % 175 / 25) {
            case 0 -> gc.drawImage(animation.dead.get(0), screenX, screenY);
            case 1 -> gc.drawImage(animation.dead.get(1), screenX, screenY);
            case 2 -> gc.drawImage(animation.dead.get(2), screenX, screenY);
            case 3 -> gc.drawImage(animation.dead.get(3), screenX, screenY);
            case 4 -> gc.drawImage(animation.dead.get(4), screenX, screenY);
            case 5 -> gc.drawImage(animation.dead.get(5), screenX, screenY);
            case 6 -> AfterAnimationDead = true;
        }
    }

    @Override
    public void playGetHitSound() {
        if (System.currentTimeMillis() - timeSinceLastDamageSound >= 3_500) {
            timeSinceLastDamageSound = System.currentTimeMillis();
            //animation.playGetHitSound(4);
        }
    }

    private void gruntMovement() {
        if (mg.client && onPath) {
        } else if (onPath) {
            if (searchTicks >= Math.random() * 45) {
                getNearestPlayer();
                searchPath(goalCol, goalRow, 16);
                searchTicks = 0;
            } else {
                trackPath();
            }
        }
    }
}

