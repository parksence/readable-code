package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_COL_SIZE = 8;
    public static final int BOARD_ROW_SIZE = 10;
    public static final Scanner SCANNER = new Scanner(System.in);
    private static final String[][] BOARD = new String[BOARD_COL_SIZE][BOARD_ROW_SIZE];
    private static final Integer[][] NEARBY_LAND_MINE_COUNTS = new Integer[BOARD_COL_SIZE][BOARD_ROW_SIZE];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_COL_SIZE][BOARD_ROW_SIZE];
    public static final int LAND_MINE_COUNT = 10;
    public static final String FLAG_SIGN = "⚑";
    public static final String LAND_MINE_SIGN = "☼";
    public static final String CLOSE_CELL_SIGN = "□";
    public static final String OPENED_CELL_SIGN = "■";
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGateStartComments();
        initializeGame();

        while (true) {
            showBoard();

            if (doesUserWinTheGame()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (doesUserLoseTheGame()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellInput = getCellInputFromUser();
            String userActionInput = getUserActionInputFromUser();
            actOnCell(cellInput, userActionInput);
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedRowIndex = getSelectedRowIndex(cellInput);
        int selectedColIndex = getSelectedColIndex(cellInput);

        if (doesUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedColIndex][selectedRowIndex] = FLAG_SIGN;
            checkIfGameIsOver();
            return;
        }

        if (doesUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedColIndex, selectedRowIndex)) {
                BOARD[selectedColIndex][selectedRowIndex] = LAND_MINE_SIGN;
                changeGameStatusToLose();
                return;
            }

            open(selectedColIndex, selectedRowIndex);
            checkIfGameIsOver();
            return;
        }
        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedColIndex, int selectedRowIndex) {
        return LAND_MINES[selectedColIndex][selectedRowIndex];
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(1);
        return convertColFrom(cellInputCol);
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(0);
        return convertRowFrom(cellInputRow);
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellOpened() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                .noneMatch(cell -> cell.equals(CLOSE_CELL_SIGN));
    }

    private static int convertColFrom(char cellInputCol) {
        return Character.getNumericValue(cellInputCol) - 1;
    }

    private static int convertRowFrom(char cellInputRow) {
        switch (cellInputRow) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int col = 0; col < BOARD_COL_SIZE; col++) {
            System.out.printf("%d  ", col + 1);
            for (int row = 0; row < BOARD_ROW_SIZE; row++) {
                System.out.print(BOARD[col][row] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int col = 0; col < BOARD_COL_SIZE; col++) {
            for (int row = 0; row < BOARD_ROW_SIZE; row++) {
                BOARD[col][row] = CLOSE_CELL_SIGN;
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_ROW_SIZE);
            int row = new Random().nextInt(BOARD_COL_SIZE);
            LAND_MINES[row][col] = true;
        }

        for (int col = 0; col < BOARD_COL_SIZE; col++) {
            for (int row = 0; row < BOARD_ROW_SIZE; row++) {
                if (isLandMineCell(col, row)) {
                    NEARBY_LAND_MINE_COUNTS[col][row] = 0;
                    continue;
                }
                int count = countNearbyLandMines(col, row);
                NEARBY_LAND_MINE_COUNTS[col][row] = count;
            }
        }
    }

    private static int countNearbyLandMines(int col, int row) {
        int count = 0;
        if (col - 1 >= 0 && row - 1 >= 0 && isLandMineCell(col - 1, row - 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(col - 1, row)) {
            count++;
        }
        if (col - 1 >= 0 && row + 1 < BOARD_ROW_SIZE && isLandMineCell(col - 1, row + 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(col, row - 1)) {
            count++;
        }
        if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(col, row + 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && row - 1 >= 0 && isLandMineCell(col + 1, row - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(col + 1, row)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && row + 1 < BOARD_ROW_SIZE && isLandMineCell(col + 1, row + 1)) {
            count++;
        }
        return count;
    }

    private static void showGateStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int col, int row) {
        if (col < 0 || col >= BOARD_COL_SIZE || row < 0 || row >= BOARD_ROW_SIZE) {
            return;
        }
        if (!BOARD[col][row].equals(CLOSE_CELL_SIGN)) {
            return;
        }
        if (isLandMineCell(col, row)) {
            return;
        }
        if (NEARBY_LAND_MINE_COUNTS[col][row] != 0) {
            BOARD[col][row] = String.valueOf(NEARBY_LAND_MINE_COUNTS[col][row]);
            return;
        } else {
            BOARD[col][row] = OPENED_CELL_SIGN;
        }
        open(col - 1, row - 1);
        open(col - 1, row);
        open(col - 1, row + 1);
        open(col, row - 1);
        open(col, row + 1);
        open(col + 1, row - 1);
        open(col + 1, row);
        open(col + 1, row + 1);
    }

}
