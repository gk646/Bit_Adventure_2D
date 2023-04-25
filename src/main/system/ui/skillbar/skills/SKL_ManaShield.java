/*
 * MIT License
 *
 * Copyright (c) 2023 Lukas Gilch
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

import javafx.scene.canvas.GraphicsContext;
import main.MainGame;
import main.system.ui.skillbar.SKILL;

public class SKL_ManaShield extends SKILL {
    public SKL_ManaShield(MainGame mg) {
        super(mg);
        name = "Mana Shield";
        description = "Mana Shield is a powerful defensive ability that allows the caster to create a protective shield around themselves using their own magical energy. Upon activation, the shield generates a shimmering barrier of pure mana that absorbs incoming attacks. It has no cast time,however tt has a continuous mana cost that drains the caster's magical reserves while it remains active. Regenerates a small amount of mana upon getting hit.";
        //TODO mana restoration upon taking hit
    }

    /**
     */
    @Override
    public void draw(GraphicsContext gc, int x, int y) {

    }

    /**
     *
     */
    @Override
    public void update() {

    }

    /**
     *
     */
    @Override
    public void activate() {
        mg.player.playCastAnimation(2);
    }
}
