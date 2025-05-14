package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    // 보드판
    private static String[][] board = new String[8][10];
    // 지뢰 숫자
    private static Integer[][] landMineCounts = new Integer[8][10];
    // 지뢰 유무
    private static boolean[][] landMines = new boolean[8][10];
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGateStartComments();
        Scanner scanner = new Scanner(System.in);
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

            String cellInput = getCellInputFromUser(scanner);
            String userActionInput = getUserActionInputFromUser(scanner);
            int selectedRowIndex = getSelectedRowIndex(cellInput);
            int selectedColIndex = getSelectedColIndex(cellInput);
            if (doesUserChooseToPlantFlag(userActionInput)) {
                board[selectedColIndex][selectedRowIndex] = "⚑";
                checkIfGameIsOver();
            } else if (doesUserChooseToOpenCell(userActionInput)) {
                if (isLandMineCell(selectedColIndex, selectedRowIndex)) {
                    board[selectedColIndex][selectedRowIndex] = "☼";
                    changeGameStatusToLose();
                    continue;
                } else {
                    open(selectedColIndex, selectedRowIndex);
                }
                checkIfGameIsOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedColIndex, int selectedRowIndex) {
        return landMines[selectedColIndex][selectedRowIndex];
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

    private static String getUserActionInputFromUser(Scanner scanner) {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return scanner.nextLine();
    }

    private static String getCellInputFromUser(Scanner scanner) {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return scanner.nextLine();
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
        boolean isAllOpened = true;
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 10; row++) {
                if (board[col][row].equals("□")) {
                    isAllOpened = false;
                }
            }
        }
        return isAllOpened;
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
        for (int col = 0; col < 8; col++) {
            System.out.printf("%d  ", col + 1);
            for (int row = 0; row < 10; row++) {
                System.out.print(board[col][row] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 10; row++) {
                board[col][row] = "□";
            }
        }
        // 지뢰 셋팅
        for (int i = 0; i < 10; i++) {
            int col = new Random().nextInt(10);
            int row = new Random().nextInt(8);
            landMines[row][col] = true;
        }
        for (int col = 0; col < 8; col++) {
            for (int row = 0; row < 10; row++) {
                int count = 0;
                // 지뢰가 아닌 칸에서
                if (!isLandMineCell(col, row)) {
                    if (col - 1 >= 0 && row - 1 >= 0 && isLandMineCell(col - 1, row - 1)) {
                        count++;
                    }
                    if (col - 1 >= 0 && isLandMineCell(col - 1, row)) {
                        count++;
                    }
                    if (col - 1 >= 0 && row + 1 < 10 && isLandMineCell(col - 1, row + 1)) {
                        count++;
                    }
                    if (row - 1 >= 0 && isLandMineCell(col, row - 1)) {
                        count++;
                    }
                    if (row + 1 < 10 && isLandMineCell(col, row + 1)) {
                        count++;
                    }
                    if (col + 1 < 8 && row - 1 >= 0 && isLandMineCell(col + 1, row - 1)) {
                        count++;
                    }
                    if (col + 1 < 8 && isLandMineCell(col + 1, row)) {
                        count++;
                    }
                    if (col + 1 < 8 && row + 1 < 10 && isLandMineCell(col + 1, row + 1)) {
                        count++;
                    }
                    landMineCounts[col][row] = count;
                    continue;
                }
                landMineCounts[col][row] = 0;
            }
        }
    }

    private static void showGateStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int col, int row) {
        if (col < 0 || col >= 8 || row < 0 || row >= 10) {
            return;
        }
        if (!board[col][row].equals("□")) {
            return;
        }
        if (isLandMineCell(col, row)) {
            return;
        }
        if (landMineCounts[col][row] != 0) {
            board[col][row] = String.valueOf(landMineCounts[col][row]);
            return;
        } else {
            board[col][row] = "■";
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
