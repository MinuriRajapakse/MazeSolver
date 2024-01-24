import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeSolver {
    private final char[][] mazeArray;
    private final String[][] solvedMazeArray;
    private String endPath;
    MazeNode head;

    public MazeSolver(File mazeFile) {
        try {
            // Read the maze in the text file to the program

            Scanner readFile = new Scanner(mazeFile);

            int rows = 0;
            int cols = 0;
            while (readFile.hasNextLine()) {
                String line = readFile.nextLine();
                rows++;
                cols = Math.max(cols, line.length());
            }

            mazeArray = new char[rows][cols];

            readFile = new Scanner(mazeFile);

            int rowIndex = 0;
            while (readFile.hasNextLine()) {
                String line = readFile.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    char character = line.charAt(i);
                    mazeArray[rowIndex][i] = character;
                }
                rowIndex++;
            }

            readFile.close();
            solvedMazeArray = new String[rows][cols];

            // Find the location of the starting point and the ending point
            for (int i = 0; i < mazeArray.length; i++) {
                for (int j = 0; j < mazeArray[i].length; j++) {
                    if (mazeArray[i][j] == 'S') {
                        String startPath = i + " " + j;
                        push(startPath);
                    }
                    if (mazeArray[i][j] == 'E') {
                        endPath = i + " " + j;
                    }
                }
            }
            for (int m = 0; m < rows; m++) {
                for (int n = 0; n < cols; n++) {
                    solvedMazeArray[m][n] = "notVisited";
                }
            }
            while (!peek().equals(endPath)) {
                int startRow = Integer.parseInt(peek().split(" ")[0]);
                int startCol = Integer.parseInt(peek().split(" ")[1]);

                boolean pathFound = false;

                // Check the neighbouring cells

                if (isValidMove(startRow, startCol - 1)) {
                    push(startRow + " " + (startCol - 1));
                    solvedMazeArray[startRow][startCol - 1] = "visited";
                    pathFound = true;
                } else if (isValidMove(startRow - 1, startCol)) {
                    push((startRow - 1) + " " + (startCol));
                    solvedMazeArray[startRow - 1][startCol] = "visited";
                    pathFound = true;
                } else if (isValidMove(startRow, startCol + 1)) {
                    push((startRow) + " " + (startCol + 1));
                    solvedMazeArray[startRow][startCol + 1] = "visited";
                    pathFound = true;
                } else if (isValidMove(startRow + 1, startCol)) {
                    push((startRow + 1) + " " + (startCol));
                    solvedMazeArray[startRow + 1][startCol] = "visited";
                    pathFound = true;
                }

                if (!pathFound) {
                    pop();
                    if (isEmpty()) {
                        System.out.println("Path not found");
                        break;
                    }
                }
            }

            if (isEmpty()) {
                System.out.println("Path not found");
            } else {
                System.out.println("\nPath found\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isValidMove(int row,int col) {
        try {
            return (mazeArray[row][col] == 'E' || mazeArray[row][col] == '.') && !solvedMazeArray[row][col].equals("visited");
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isEmpty(){
        return head == null;
    }

    // Input data into the stack
    public void push(String data) {
        MazeNode newNode = new MazeNode(data);
        if (head == null) {
            head = newNode;
        } else {
            MazeNode temp = head;
            head = newNode;
            newNode.next = temp;
        }
    }

    // Read the data in the stack
    public String peek(){
        if(head==null){
            return "Stack is empty";
        } else {
            return head.data;
        }
    }

    // Remove data from the stack
    public void pop(){
        if(head==null){
            System.out.println("Stack is empty");
        } else {
            head = head.next;
        }
    }

    public void retrieveFinalValues() {
        MazeNode current = head;
        while (current != null) {
            String name= current.data;
            checkPath(name);
            current = current.next;
        }

    }
    public void checkPath(String name) {
        int nameRow = Integer.parseInt(name.split(" ")[0]);
        int nameCol = Integer.parseInt(name.split(" ")[1]);
        if (mazeArray[nameRow][nameCol] != 'E' && mazeArray[nameRow][nameCol] != 'S') {
            mazeArray[nameRow][nameCol] = '@';
        }
    }
    void print() {
        retrieveFinalValues();
        for (char[] chars : mazeArray) {
            System.out.print("| ");
            for (char c : chars) {
                if (c == '@') {
                    System.out.print("\u001B[31m" + c + " \u001B[0m");
                } else if (c == '!') {
                    System.out.print(". ");
                } else {
                    System.out.print(c + " ");
                }
            }
            System.out.println("|");
        }
    }

    public static void main(String[] args) {
        File mazeFile = new File("maze.txt");
        MazeSolver mazeSolver = new MazeSolver(mazeFile);
        mazeSolver.print();

    }

}

