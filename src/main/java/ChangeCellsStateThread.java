import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ChangeCellsStateThread implements Runnable {
    private final int[][] cells;
    private final int[][] prev;
    private final int start;
    private final int end;
    private final boolean rowSplit;
    private final CyclicBarrier barrier;
    private final CyclicBarrier barrierSaveBoard;
    private final int numGen;

    public ChangeCellsStateThread(int[][] cells, int[][] prev, int start, int end, boolean rowSplit, int numGen, CyclicBarrier barrier, CyclicBarrier barrierSaveBoard) {
        this.cells = cells;
        this.prev = prev;
        this.barrier = barrier;
        this.start = start;
        this.end = end;
        this.rowSplit = rowSplit;
        this.numGen = numGen;
        this.barrierSaveBoard = barrierSaveBoard;
    }

    @Override
    public void run() {
        for(int g = 0; g < numGen; g++) {
            if (rowSplit) {
                copyRows();
            } else {
                copyCols();
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            int neighbours;

            if (rowSplit) {
                for (int i = start; i <= end; i++) {
                    for (int j = 0; j < cells[0].length; j++) {
                        neighbours = countNeighbours(i, j);
                        cells[i][j] = determineNewCellState(prev[i][j], neighbours);
                    }
                }
            } else {
                for (int i = 0; i < cells.length; i++) {
                    for (int j = start; j <= end; j++) {
                        neighbours = countNeighbours(i, j);
                        cells[i][j] = determineNewCellState(prev[i][j], neighbours);
                    }
                }
            }

            try {
                barrierSaveBoard.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private int determineNewCellState(int state, int neighbours) {
        if(state == 0 && neighbours == 3) {
            return 1;
        } else if(state == 1 && (neighbours == 2 || neighbours == 3)) {
            return 1;
        }

        return 0;
    }

    private int countNeighbours(int a, int b) {
        int count = 0;
        for(int i = a - 1; i <= a + 1; i++) {
            for(int j = b - 1; j <= b + 1; j++) {
                if(checkBorder(i, j) && prev[i][j] == 1 && !(i == a && j == b)) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean checkBorder(int i, int j) {
        return i >= 0 && i < cells.length && j >= 0 && j < cells[0].length;
    }

    private void copyRows() {
        for(int i = start; i <= end; i++) {
            for(int j = 0; j < cells[0].length; j++) {
                prev[i][j] = cells[i][j];
            }
        }
    }

    private void copyCols() {
        for(int i = 0; i < cells.length; i++) {
            for(int j = start; j <= end; j++) {
                prev[i][j] = cells[i][j];
            }
        }
    }
}
