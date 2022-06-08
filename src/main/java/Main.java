public class Main {
    static int genNum = 1;
    static int threadsNumber = 10;
    static boolean rowSplit = true;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Plik z konfiguracją planszy początkowej nie został podany.");
            System.exit(0);
        }

        if (args.length > 6) {
            readArgument(args[5], args[6]);
        }
        if (args.length > 4) {
            readArgument(args[3], args[4]);
        }
        if (args.length > 2) {
            readArgument(args[1], args[2]);
        }

        int[][] boardData = FileService.readInputBoardFromFile(args[0]);
        Board board = new Board(boardData);

        try {
            board.determineNewBoardState(genNum, threadsNumber, rowSplit);
        } catch (InterruptedException e) {
            System.out.println("Błąd podczas obliczeń :(");
        }
    }

    public static void readArgument(String option, String value) {
        switch (option) {
            case "-N":
                setNumberOfGeneration(value);
                break;
            case "-T":
                setThreadsNumber(value);
                break;
            case "-P":
                setSplitMethod(value);
                break;
            default:
                System.out.println("Niepoprawna opcja \"" + option + "\".");
                System.exit(0);
        }
    }

    public static void setNumberOfGeneration(String value) {
        try {
            genNum = Integer.parseInt(value);
            if(genNum <= 0) {
                throw new IllegalArgumentException("Liczba generacji musi być liczbą dodatnią.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Liczba generacji musi być liczbą.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setThreadsNumber(String value) {
        try {
            threadsNumber = Integer.parseInt(value);
            if(threadsNumber <= 0) {
                throw new IllegalArgumentException("Liczba wątków musi być liczbą dodatnią.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Liczba wątków musi być liczbą.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setSplitMethod(String value) {
        if (value.matches("[WK]")) {
            rowSplit = value.equals("W");
        } else {
            System.out.println("Niepoprawny format wyboru sposobu podziału danych.");
        }
    }
}
