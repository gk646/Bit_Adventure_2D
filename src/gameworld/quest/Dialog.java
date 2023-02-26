package gameworld.quest;

import gameworld.entities.ENTITY;
import gameworld.player.Player;
import input.InputHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.system.ui.FonT;

public class Dialog {
    public int dialogRenderCounter = 0;
    public String dialogLine = "...";
    public boolean drawChoice;
    public String choice1, choice2, choice3, choice4;
    public int choicePointer = 0;
    public int maxChoices;

    /**
     * The dialog framework
     * <p>
     * mainGame to access cross-class information
     * to choose the type for dialog text (also quest id)
     */
    public Dialog() {
    }

    public static String insertNewLine(String str) {
        StringBuilder sb = new StringBuilder();
        String[] words = str.split("\\s+");
        int count = 0;
        for (String word : words) {
            if (count + word.length() > 39) {
                sb.append("\n");
                count = 0;
            }
            count += word.length();
            sb.append(word);
            sb.append(" ");
            count++;
        }
        return sb.toString();
    }

    public void drawDialog(GraphicsContext gc, ENTITY entity) {
        gc.setFont(FonT.minecraftBoldItalic15);
        gc.setFill(Color.BLACK);
        gc.fillRoundRect(entity.worldX - Player.worldX + Player.screenX - 24 - 124, entity.worldY - Player.worldY + Player.screenY - 24 - 115, 373, 120, 25, 25);
        gc.setLineWidth(2);
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        int x = (int) (entity.worldX - Player.worldX + Player.screenX - 24 + 5 - 124);
        //TODO check if still works
        gc.strokeRoundRect(entity.worldX - Player.worldX + Player.screenX - 24 - 124, entity.worldY - Player.worldY + Player.screenY - 24 - 115, 373, 120, 25, 25);
        if (dialogRenderCounter == 2_000) {
            int stringY = (int) (entity.worldY - Player.worldY + Player.screenY - 24 - 115 + 6);

            for (String string : dialogLine.split("\n")) {
                gc.fillText(string, x, stringY += 16);
            }
            if (drawChoice) {
                gc.fillText(choice1, x, stringY += 16);
                gc.fillText(choice2, x + 50, stringY += 16);
                if (choicePointer == 0) {
                    gc.fillText(">", x, stringY);
                } else if (choicePointer == 1) {
                    gc.fillText(">", x + 50, stringY + 16);
                }
                if (InputHandler.q_typed) {
                    if (choicePointer < maxChoices) {
                        choicePointer++;
                    } else {
                        choicePointer = 0;
                    }
                }
                if (InputHandler.instance.f_pressed) {
                    if (choicePointer == 0) {
                        choicePointer = 10;
                    } else if (choicePointer == 1) {
                        choicePointer = 20;
                    } else if (choicePointer == 2) {
                        choicePointer = 30;
                    } else if (choicePointer == 3) {
                        choicePointer = 40;
                    }
                }
            }
        } else {
            gc.strokeRoundRect(entity.worldX - Player.worldX + Player.screenX - 24 - 124, entity.worldY - Player.worldY + Player.screenY - 24 - 115, 373, 120, 25, 25);
            int stringY = (int) (entity.worldY - Player.worldY + Player.screenY - 24 - 115 + 6);
            for (String string : dialogLine.substring(0, Math.min(dialogLine.length(), dialogRenderCounter / 4)).split("\n")) {
                gc.fillText(string, x, stringY += 16);
            }
            if (dialogRenderCounter / 4 >= dialogLine.length()) {
                dialogRenderCounter = 2000;
            } else {
                dialogRenderCounter++;
            }
        }
    }

    public void loadNewLine(String dialogLine) {
        this.dialogLine = insertNewLine(dialogLine);
        this.dialogRenderCounter = 0;
    }

    public int drawChoice(String option1, String option2, String option3, String option4) {
        if (drawChoice) {
            if (choicePointer == 10 || choicePointer == 20 || choicePointer == 30 || choicePointer == 40) {
                drawChoice = false;
                choicePointer = 0;
                return choicePointer;
            } else {
                return 0;
            }
        } else {
            drawChoice = true;
            maxChoices = 2;
            choice1 = option1;
            choice2 = option2;
            choicePointer = 0;
            if (option3 != null) {
                maxChoices++;
                choice3 = option3;
            }
            if (option4 != null) {
                maxChoices++;
                choice4 = option4;
            }
        }
        return 0;
    }
}
