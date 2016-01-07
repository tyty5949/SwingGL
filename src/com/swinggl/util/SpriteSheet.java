package com.swinggl.util;

import com.swinggl.backend.Texture;

/**
 * Created on 1/6/2016.
 */
public class SpriteSheet {

    /**
     * Convenience method that gets all of the texture coordinates for a button in a spritesheet. The spritesheet MUST contain 3 textures of the same size
     * and they MUST be in line from left to right.
     *
     * @param x   - The x coord of the first button on the texture
     * @param y   - The y coord of the first button on the texture
     * @param w   - The width of the buttons in the texture
     * @param h   - The height of the buttons in the texture
     * @param tex - The texture that contains the data for the button
     * @return - The fully populated float array with every texture coordinate
     */
    public static float[][] getGLButtonCoords(float x, float y, float w, float h, Texture tex) {
        float[][] temp = new float[][]{
                {x, y, x + w, y, x + w, y + h, x, y + h},
                {x + w, y, x + w + w, y, x + w + w, y + h, x + w, y + h},
                {x + (2 * w), y, x + w + (2 * w), y, x + w + (2 * w), y + h, x + (2 * w), y + h}};
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                if (j % 2 == 0)
                    temp[i][j] = temp[i][j] / (float) tex.getWidth();
                else
                    temp[i][j] = temp[i][j] / (float) tex.getHeight();
            }
        }
        return temp;
    }

    /**
     * Convenience method that gets all of the texture coordinates for a checkbox in a spritesheet. The spritesheet MUST contain 4 textures of the same size
     * and they MUST be in line from left to right.
     *
     * @param x   - The x coord of the first checkbox on the texture
     * @param y   - The y coord of the first checkbox on the texture
     * @param w   - The width of the checkbox in the texture
     * @param h   - The height of the checkbox in the texture
     * @param tex - The texture that contains the data for the checkbox
     * @return - The fully populated float array with every texture coordinate
     */
    public static float[][] getGLCheckBoxCoords(float x, float y, float w, float h, Texture tex) {
        float[][] temp = new float[][]{
                {x, y, x + w, y, x + w, y + h, x, y + h},
                {x + w, y, x + w + w, y, x + w + w, y + h, x + w, y + h},
                {x + (2 * w), y, x + w + (2 * w), y, x + w + (2 * w), y + h, x + (2 * w), y + h},
                {x + (3 * w), y, x + w + (3 * w), y, x + w + (3 * w), y + h, x + (3 * w), y + h}};
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                if (j % 2 == 0)
                    temp[i][j] = temp[i][j] / (float) tex.getWidth();
                else
                    temp[i][j] = temp[i][j] / (float) tex.getHeight();
            }
        }
        return temp;
    }
}
