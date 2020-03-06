import java.util.*;

public class Main {
    static String[] board = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    static String turn = "O";
    static List<int[]> winConditions = new ArrayList<>(){{
            add(new int[]{1, 2, 3});
            add(new int[]{1, 5, 9});
            add(new int[]{1, 4, 7});
            add(new int[]{2, 5, 8});
            add(new int[]{3, 5, 7});
            add(new int[]{3, 6, 9});
            add(new int[]{4, 5, 6});
            add(new int[]{7, 8, 9});
    }};
    static Map<Integer, Integer> viableMoves = new HashMap<>();
    static Random rand = new Random();
    static List<Integer> playerChoices = new ArrayList<>();
    static List<Integer> computerChoices = new ArrayList<>();
    static boolean playerIsX = false;
    static boolean isDone = false;
    static boolean isWon = false;
    static Scanner input = new Scanner(System.in);
    static int count = 0;
    static String winner = "";
    public static void main(String[] args) {
        System.out.println("Welcome to Noughts and Crosses!");
        do{
            initializeNoughtsCrosses();
            playNoughtsCrosses();
        }while(!isDone);
    }

    public static void initializeNoughtsCrosses() {
        board = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        turn = "O";
        winConditions = new ArrayList<>(){{
            add(new int[]{1, 2, 3});
            add(new int[]{1, 5, 9});
            add(new int[]{1, 4, 7});
            add(new int[]{2, 5, 8});
            add(new int[]{3, 5, 7});
            add(new int[]{3, 6, 9});
            add(new int[]{4, 5, 6});
            add(new int[]{7, 8, 9});
        }};
        viableMoves = new HashMap<>(){{
            put(1, 1);
            put(2, 2);
            put(3, 3);
            put(4, 4);
            put(5, 5);
            put(6, 6);
            put(7, 7);
            put(8, 8);
            put(9, 9);
        }};
        playerChoices = new ArrayList<>();
        computerChoices = new ArrayList<>();
        isWon = false;
        playerIsX = false;
        count = 0;
        winner = null;
    }

    public static void printBoard() {
        System.out.println(String.format("""
                %s|%s|%s
                -----
                %s|%s|%s
                -----
                %s|%s|%s""",
                board[0] == "1" ? " " : board[0],
                board[1] == "2" ? " " : board[1],
                board[2] == "3" ? " " : board[2],
                board[3] == "4" ? " " : board[3],
                board[4] == "5" ? " " : board[4],
                board[5] == "6" ? " " : board[5],
                board[6] == "7" ? " " : board[6],
                board[7] == "8" ? " " : board[7],
                board[8] == "9" ? " " : board[8]
        ));
    }

    public static int chooseRandomTile(){
        Integer result = null;
        do{
            result = viableMoves.get(rand.nextInt(9) + 1);
        }while(result == null);
        return result;
    }

    public static boolean placeTile(int n) {
        boolean isTaken = true;
        if(!board[n - 1].equals("X") && !board[n - 1].equals("O")){
            isTaken = false;
            board[n - 1] = turn;
        }
        return isTaken;
    }

    public static boolean arrayContainsInt(int[] arr, int i) {
        boolean result = false;
        for (int j : arr) {
            if(i == j) result = true;
        }
        return result;
    }

    public static boolean eliminateWinConditions() {
        for (int i : playerChoices) {
            for(int j = winConditions.size() - 1; j >= 0; j--){
                if(arrayContainsInt(winConditions.get(j), i)) winConditions.remove(j);
            }
        }
        return winConditions.size() != 0;
    }

    public static void evaluateViableMoves(boolean hasWinConditions) {
        if(hasWinConditions){
            viableMoves.clear();
            for( int[] i: winConditions){
                for (int j : i) {
                    viableMoves.put(j, j);
                }
            }
        }
        else{
            for( int k : playerChoices) {
                viableMoves.remove(k);
            }
        }
        for( int l : computerChoices) {
            viableMoves.remove(l);
        }
    }

    public static String checkForWinner(){
        for(int m = 0; m < 8; m++){
            String line = "";
            switch (m){
                case 0:
                    line = board[0] + board[1] + board[2];
                    break;
                case 1:
                    line = board[3] + board[4] + board[5];
                    break;
                case 2:
                    line = board[6] + board[7] + board[8];
                    break;
                case 3:
                    line = board[0] + board[3] + board[6];
                    break;
                case 4:
                    line = board[1] + board[4] + board[7];
                    break;
                case 5:
                    line = board[2] + board[5] + board[8];
                    break;
                case 6:
                    line = board[0] + board[4] + board[8];
                    break;
                case 7:
                    line = board[2] + board[4] + board[6];
                    break;
            }
            if(line.equals("XXX")) return "X";
            else if(line.equals("OOO")) return "O";
        }
        if(count == 9) return "draw";
        else return "";
    }

    public static void playNoughtsCrosses() {
        System.out.println("Do you want to be X or O?");
        playerIsX = input.next().equalsIgnoreCase("x");
        if(!playerIsX) turn = "X";
        System.out.println("The computer will go first.");
        while(!isWon){
            int temp = 0;
            evaluateViableMoves(eliminateWinConditions());
            if(count % 2 == 0) {
                temp = chooseRandomTile();
                placeTile(temp);
                computerChoices.add(temp);
                turn = playerIsX ? "X" : "O";
            }else {
                printBoard();
                System.out.println("What is your next move? (1-9)");
                boolean isValid = true;
                do {
                    temp = input.nextInt();
                    isValid = !placeTile(temp);
                    if(!isValid)System.out.println("That tile isn't valid, please pick another tile that is not taken. (0-9)");
                }while(!isValid);
                playerChoices.add(temp);
                turn = playerIsX? "O" : "X";
            }
            count++;
            winner = checkForWinner();
            isWon = winner != "";
        }
        printBoard();
        switch (winner){
            case "X":
                System.out.println(playerIsX ? "You have beaten the computer! You win!" : "The computer has beaten you! You lose");
                break;
            case "O":
                System.out.println(playerIsX ? "The computer has beaten you! You lose" : "You have beaten the computer! You win!");
                break;
            case "draw":
                System.out.println("It is a draw! Nobody wins.");
            default:
                break;
        }
        System.out.println("Do you want to play again? (yes or no)");
        isDone = input.next().equalsIgnoreCase("no") ? true : false;
    }
}
