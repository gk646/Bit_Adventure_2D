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

package main.system.ui.skillbar.skills;

import gameworld.entities.damage.DamageType;
import gameworld.player.abilities.ARCANE.PRJ_ThunderStrike;
import javafx.scene.canvas.GraphicsContext;
import main.MainGame;
import main.system.ui.skillbar.SKILL;


public class SKL_ThunderStrike extends SKILL {


    public SKL_ThunderStrike(MainGame mg) {
        super(mg);
        totalCoolDown = 300;
        actualCoolDown = totalCoolDown;
        manaCost = 35;
        this.weapon_damage_percent = 350;
        icon = setup("thunderStrike");
        type = DamageType.Arcane;
        name = "Thunder Strike";
        description = "Thunder Strike is a lightning-based ability that allows the player to call down a powerful bolt of lightning onto enemies in a targeted area. When activated, the sky darkens and crackles with electricity and after a short delay, a bolt of lightning strikes down, dealing massive area of effect damage to all enemies within the target area.";
    }

    /**
     * used for drawing the skill icon and the cooldown overlay
     *
     * @param gc graphics context
     * @param x  x start
     * @param y  y start
     */
    @Override
    public void draw(GraphicsContext gc, int x, int y) {
        drawIcon(gc, x, y);
        drawCooldown(gc, x, y);
    }

    /**
     *
     */
    @Override
    public void update() {
        updateCooldown();
    }

    /**
     *
     */
    @Override
    public void activate() {
        if (actualCoolDown >= totalCoolDown && mg.player.getMana() >= manaCost) {
            mg.player.loseMana(manaCost);
            actualCoolDown = 0;
            mg.player.playCastAnimation(3);
            mg.PROJECTILES.add(new PRJ_ThunderStrike(weapon_damage_percent));
        }
    }
}




