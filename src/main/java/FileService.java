import java.io.*;

public class FileService {

    public static int[][] readInputBoardFromFile(String path) {
        int[][] board = null;
        try (RandomAccessFile file = new RandomAccessFile(path, "r")) {
            String line;
            int countLines = 0;
            int prevLineLength = 0;
            while((line = file.readLine()) != null) {
                if(countLines != 0 && prevLineLength != line.trim().length()) {
                   throw new IllegalArgumentException("Wiersz " + (countLines + 1) + " ma inną liczbę znaków niż poprzednie wiersze.");
                }
                prevLineLength = line.trim().length();
                countLines++;
            }

            board = new int[countLines][prevLineLength];
            file.seek(0);

            int i = 0;
            int j;
            while((line = file.readLine()) != null) {
                for(j = 0; j < line.length(); j++) {
                    if(line.charAt(j) == '0' || line.charAt(j) == '1') {
                        board[i][j] = Integer.parseInt(String.valueOf(line.charAt(j)));
                    } else {
                        throw new IllegalArgumentException("Na pozycji " + j + " w wierszu " + i +" występuje niepoprawny znak." +
                                "\nPlik może zawierać tylko 0 (komórka martwa) i 1 (komórka żywa).");
                    }
                }
                i++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("Nie można odnaleźć podanego pliku \"" + path + "\".");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Błąd wczytywania danych z pliku \"" + path + "\" :(.");
            System.exit(0);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        return board;
    }

    public static int[][] writeCurrentBoardToFile(String path, int[][] board) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, false))) {
            for(int i = 0; i < board.length; i++) {
                for(int j = 0; j < board[0].length; j++) {
                    bw.write(String.valueOf(board[i][j]));
                }
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return board;
    }
}
