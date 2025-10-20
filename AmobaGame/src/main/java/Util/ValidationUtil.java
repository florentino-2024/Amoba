package Util;

import Model.Position;
import com.amoba.model.Position;

public class ValidationUtil {

    public static boolean isValidPositionString(String input, int maxRows, int maxCols) {
        if (input == null || input.length() < 2) {
            return false;
        }

        try {
            Position Position = null;
            Position position = Position.fromString(input);
            return position.getRow() >= 0 && position.getRow() < maxRows &&
                    position.getCol() >= 0 && position.getCol() < maxCols;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDimension(int rows, int cols) {
        return rows >= 5 && rows <= 25 && cols >= 5 && cols <= 25 && cols <= rows;
    }

    public static boolean isYesNoInput(String input) {
        return input != null && (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n"));
    }
}
