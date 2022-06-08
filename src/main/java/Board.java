import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Board {
    private int[][] cells;
    private int[][] prev;
    private CyclicBarrier barrier;
    private CyclicBarrier barrierSaveBoard;
    private final List<Thread> threadList;

    public Board(int[][] cells) {
        this.cells = cells;
        prev = new int[cells.length][cells[0].length];
        threadList = new LinkedList<>();
    }

    public void determineNewBoardState(int numGen, int threadsNumber, boolean rowSplit) throws InterruptedException {
        Thread thread;

        threadsNumber = rowSplit ? checkThreadsNumber(threadsNumber, cells.length) :
                                    checkThreadsNumber(threadsNumber, cells[0].length);

        barrier = new CyclicBarrier(threadsNumber);
        barrierSaveBoard = new CyclicBarrier(threadsNumber, new SaveBoardToFileThread(cells));

        int partSize = calcPartSize(threadsNumber, rowSplit);
        int rest = calcRest(threadsNumber, rowSplit);

        int start = 0;
        int end = partSize - 1;
        if (rest > 0) {
            end++;
            rest--;
        }

        for (int i = 0; i < threadsNumber; i++) {
            thread = new Thread(new ChangeCellsStateThread(cells, prev, start, end, rowSplit, numGen, barrier, barrierSaveBoard));
            thread.start();
            threadList.add(thread);

            start = end + 1;
            end = end + partSize;
            if (rest > 0) {
                end++;
                rest--;
            }
        }

        for (Thread t : threadList) {
            t.join();
        }
    }

    private int calcPartSize(int threadsNumber, boolean rowSplit) {
        return rowSplit ? cells.length / threadsNumber : cells[0].length / threadsNumber;
    }

    private int calcRest(int threadsNumber, boolean rowSplit) {
        return rowSplit ? cells.length % threadsNumber : cells[0].length % threadsNumber;
    }

    private int checkThreadsNumber(int threads, int board) {
        return Math.min(threads, board);
    }

    public int[][] getCells() {
        return cells;
    }
}
