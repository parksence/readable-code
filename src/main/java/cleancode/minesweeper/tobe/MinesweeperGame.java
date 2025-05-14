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
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        Scanner scanner = new Scanner(System.in);
        // 보드판 그리기
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
                if (!landMines[col][row]) {
                    if (col - 1 >= 0 && row - 1 >= 0 && landMines[col - 1][row - 1]) {
                        count++;
                    }
                    if (col - 1 >= 0 && landMines[col - 1][row]) {
                        count++;
                    }
                    if (col - 1 >= 0 && row + 1 < 10 && landMines[col - 1][row + 1]) {
                        count++;
                    }
                    if (row - 1 >= 0 && landMines[col][row - 1]) {
                        count++;
                    }
                    if (row + 1 < 10 && landMines[col][row + 1]) {
                        count++;
                    }
                    if (col + 1 < 8 && row - 1 >= 0 && landMines[col + 1][row - 1]) {
                        count++;
                    }
                    if (col + 1 < 8 && landMines[col + 1][row]) {
                        count++;
                    }
                    if (col + 1 < 8 && row + 1 < 10 && landMines[col + 1][row + 1]) {
                        count++;
                    }
                    landMineCounts[col][row] = count;
                    continue;
                }
                landMineCounts[col][row] = 0;
            }
        }
        while (true) {
            System.out.println("   a b c d e f g h i j");
            for (int col = 0; col < 8; col++) {
                System.out.printf("%d  ", col + 1);
                for (int row = 0; row < 10; row++) {
                    System.out.print(board[col][row] + " ");
                }
                System.out.println();
            }
            if (gameStatus == 1) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (gameStatus == -1) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }
            System.out.println();
            System.out.println("선택할 좌표를 입력하세요. (예: a1)");
            String cellInput = scanner.nextLine();
            System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
            String userActionInput = scanner.nextLine();
            char cellInputRow = cellInput.charAt(0);
            char cellInputCol = cellInput.charAt(1);
            int selectedRowIndex;
            switch (cellInputRow) {
                case 'a':
                    selectedRowIndex = 0;
                    break;
                case 'b':
                    selectedRowIndex = 1;
                    break;
                case 'c':
                    selectedRowIndex = 2;
                    break;
                case 'd':
                    selectedRowIndex = 3;
                    break;
                case 'e':
                    selectedRowIndex = 4;
                    break;
                case 'f':
                    selectedRowIndex = 5;
                    break;
                case 'g':
                    selectedRowIndex = 6;
                    break;
                case 'h':
                    selectedRowIndex = 7;
                    break;
                case 'i':
                    selectedRowIndex = 8;
                    break;
                case 'j':
                    selectedRowIndex = 9;
                    break;
                default:
                    selectedRowIndex = -1;
                    break;
            }
            int selectedColIndex = Character.getNumericValue(cellInputCol) - 1;
            if (userActionInput.equals("2")) {
                board[selectedColIndex][selectedRowIndex] = "⚑";
                boolean isAllOpened = true;
                for (int col = 0; col < 8; col++) {
                    for (int row = 0; row < 10; row++) {
                        if (board[col][row].equals("□")) {
                            isAllOpened = false;
                        }
                    }
                }
                if (isAllOpened) {
                    gameStatus = 1;
                }
            } else if (userActionInput.equals("1")) {
                if (landMines[selectedColIndex][selectedRowIndex]) {
                    board[selectedColIndex][selectedRowIndex] = "☼";
                    gameStatus = -1;
                    continue;
                } else {
                    open(selectedColIndex, selectedRowIndex);
                }
                boolean isAllOpened = true;
                for (int col = 0; col < 8; col++) {
                    for (int row = 0; row < 10; row++) {
                        if (board[col][row].equals("□")) {
                            isAllOpened = false;
                        }
                    }
                }
                if (isAllOpened) {
                    gameStatus = 1;
                }
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void open(int col, int row) {
        if (col < 0 || col >= 8 || row < 0 || row >= 10) {
            return;
        }
        if (!board[col][row].equals("□")) {
            return;
        }
        if (landMines[col][row]) {
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
