public class SaveBoardToFileThread implements Runnable {
    private int[][] cells;
    private int gen;

    public SaveBoardToFileThread(int[][] cells) {
        this.cells = cells;
        gen = 1;
    }

    @Override
    public void run() {
        FileService.writeCurrentBoardToFile("generation" + gen + ".txt", cells);
        gen++;
    }

}
