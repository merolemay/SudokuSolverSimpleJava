package code;

import java.util.*;

public class DancingLinksAlgorithm {
    private static final int BOARD_SIZE = 9;
    private static final int SUBSECTION_SIZE = 3;
    private static final int NO_VALUE = 0;
    private static final int CONSTRAINTS = 4;
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 9;
    private static final int COVER_START_INDEX = 1;

//    private static int[][] board = {
//            {8, 0, 0, 0, 0, 0, 0, 0, 0},
//            {0, 0, 3, 6, 0, 0, 0, 0, 0},
//            {0, 7, 0, 0, 9, 0, 2, 0, 0},
//            {0, 5, 0, 0, 0, 7, 0, 0, 0},
//            {0, 0, 0, 0, 4, 5, 7, 0, 0},
//            {0, 0, 0, 1, 0, 0, 0, 3, 0},
//            {0, 0, 1, 0, 0, 0, 0, 6, 8},
//            {0, 0, 8, 5, 0, 0, 0, 1, 0},
//            {0, 9, 0, 0, 0, 0, 4, 0, 0}
//    };

    public static void main(String[] args) {
        int [][ ] board = ParseInput(getInput());
        DancingLinksAlgorithm solver = new DancingLinksAlgorithm();
        solver.solve(board);

    }

    private static String getInput() {
        StringBuilder input = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i <9 ; i++) {
            input.append(sc.nextLine()).append("\n");
        }
        return input.toString();
    }

    private static int[][] ParseInput(String input) {
        String[] rows = input.split("\n");

        int [][] matrix = new int[9][9];

        for (int i = 0; i < rows.length ; i++) {
            String[] line = rows[i].split(",");
            for (int j = 0; j < 9; j++) {
                String item = line[j];
                if(!item.equals(" ")) matrix[i][j] = Integer.parseInt(item);
                else matrix[i][j] = 0;
            }
        }

        return matrix;
    }

    //MainSolvingAlgorithm
    private void solve(int[][] board) {
        boolean[][] cover = initializeExactCoverBoard(board);
        DancingLinks dlx = new DancingLinks(cover);
        dlx.runSolver();
        System.out.println(dlx.getSolutions().size());

    }

    private int getIndex(int row, int column, int num) {
        return (row - 1) * BOARD_SIZE * BOARD_SIZE + (column - 1) * BOARD_SIZE + (num - 1);
    }

    private boolean[][] createExactCoverBoard() {
        boolean[][] coverBoard = new boolean[BOARD_SIZE * BOARD_SIZE * MAX_VALUE][BOARD_SIZE * BOARD_SIZE * CONSTRAINTS];

        int hBase = 0;
        hBase = checkCellConstraint(coverBoard, hBase);
        hBase = checkRowConstraint(coverBoard, hBase);
        hBase = checkColumnConstraint(coverBoard, hBase);
        checkSubsectionConstraint(coverBoard, hBase);

        return coverBoard;
    }

    private int checkSubsectionConstraint(boolean[][] coverBoard, int hBase) {
        for (int row = COVER_START_INDEX; row <= BOARD_SIZE; row += SUBSECTION_SIZE) {
            for (int column = COVER_START_INDEX; column <= BOARD_SIZE; column += SUBSECTION_SIZE) {
                for (int n = COVER_START_INDEX; n <= BOARD_SIZE; n++, hBase++) {
                    for (int rowDelta = 0; rowDelta < SUBSECTION_SIZE; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < SUBSECTION_SIZE; columnDelta++) {
                            int index = getIndex(row + rowDelta, column + columnDelta, n);
                            coverBoard[index][hBase] = true;
                        }
                    }
                }
            }
        }
        return hBase;
    }

    private int checkColumnConstraint(boolean[][] coverBoard, int hBase) {
        for (int column = COVER_START_INDEX; column <= BOARD_SIZE; column++) {
            for (int n = COVER_START_INDEX; n <= BOARD_SIZE; n++, hBase++) {
                for (int row = COVER_START_INDEX; row <= BOARD_SIZE; row++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][hBase] = true;
                }
            }
        }
        return hBase;
    }

    private int checkRowConstraint(boolean[][] coverBoard, int hBase) {
        for (int row = COVER_START_INDEX; row <= BOARD_SIZE; row++) {
            for (int n = COVER_START_INDEX; n <= BOARD_SIZE; n++, hBase++) {
                for (int column = COVER_START_INDEX; column <= BOARD_SIZE; column++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][hBase] = true;
                }
            }
        }
        return hBase;
    }

    private int checkCellConstraint(boolean[][] coverBoard, int hBase) {
        for (int row = COVER_START_INDEX; row <= BOARD_SIZE; row++) {
            for (int column = COVER_START_INDEX; column <= BOARD_SIZE; column++, hBase++) {
                for (int n = COVER_START_INDEX; n <= BOARD_SIZE; n++) {
                    int index = getIndex(row, column, n);
                    coverBoard[index][hBase] = true;
                }
            }
        }
        return hBase;
    }

    private boolean[][] initializeExactCoverBoard(int[][] board) {
        boolean[][] coverBoard = createExactCoverBoard();
        for (int row = COVER_START_INDEX; row <= BOARD_SIZE; row++) {
            for (int column = COVER_START_INDEX; column <= BOARD_SIZE; column++) {
                int n = board[row - 1][column - 1];
                if (n != NO_VALUE) {
                    for (int num = MIN_VALUE; num <= MAX_VALUE; num++) {
                        if (num != n) {
                            Arrays.fill(coverBoard[getIndex(row, column, num)], false);
                        }
                    }
                }
            }
        }
        return coverBoard;
    }
}