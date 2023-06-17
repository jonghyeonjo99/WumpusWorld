package org.example;

import java.util.PrimitiveIterator;
import java.util.Random;

public class main {
    private static final int SIZE = 4;
    private static final int EMPTY = 0;
    private static final int GOLD = 1;
    private static final int WUMPUS = 2;
    private static final int PIT = 3;
    private static final int STENCH = 5;
    private static final int BREEZE = 6;

    private static int[][] cave = new int[SIZE][SIZE];
    private static boolean[][] visited = new boolean[SIZE][SIZE];
    private static int agentX = 0;
    private static int agentY = 0;
    private static boolean hasGold = false;
    private static boolean isGameover = false;
    private static boolean not_gold = false;
    private static Random random = new Random();

    public static void main(String[] args) {
        initializeCave();
        printCave();
        boolean isAgentAlive = true;

        while (!isGameover && !checkGameover() && !not_gold && isAgentAlive) {
            boolean stench = checkStench();
            boolean breeze = checkBreeze();
            boolean glitter = checkGlitter();
            boolean bump = checkBump();
            boolean scream = checkScream();

            String action = selectAction(stench, breeze, glitter, bump, scream);
            System.out.println("Action: " + action);
            performAction(action);

            printCave();

            if (cave[agentX][agentY] == WUMPUS || cave[agentX][agentY] == PIT) {
                isAgentAlive = false;
            }
        }
    }

    private static void initializeCave() {
        Random random = new Random();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cave[i][j] = EMPTY;
            }
        }

        int goldX, goldY;
        do {
            goldX = random.nextInt(SIZE);
            goldY = random.nextInt(SIZE);
        } while (goldX == 0 && goldY == 0);
        cave[goldX][goldY] = GOLD;

        int wumpusX, wumpusY;
        do {
            wumpusX = random.nextInt(SIZE);
            wumpusY = random.nextInt(SIZE);
        } while (wumpusX == 0 && wumpusY == 0);
        cave[wumpusX][wumpusY] = WUMPUS;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (cave[i][j] == EMPTY && random.nextDouble() < 0.1 && !(i == 0 && j == 0)) {
                    cave[i][j] = PIT;
                }
            }
        }
    }


    private static void printCave() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (agentX == i && agentY == j) {
                    System.out.print("A ");
                } else {
                    System.out.print(getCellSymbol(cave[i][j]) + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static String getCellSymbol(int cell) {
        switch (cell) {
            case STENCH:
                return "S";
            case BREEZE:
                return "B";
            case EMPTY:
                return "-";
            case GOLD:
                return "G";
            case WUMPUS:
                return "W";
            case PIT:
                return "P";
            default:
                return "?";
        }
    }

    private static boolean checkStench() {
        if (agentX > 0 && cave[agentX - 1][agentY] == WUMPUS) {
            return true;
        }
        if (agentX < SIZE - 1 && cave[agentX + 1][agentY] == WUMPUS) {
            return true;
        }
        if (agentY > 0 && cave[agentX][agentY - 1] == WUMPUS) {
            return true;
        }
        if (agentY < SIZE - 1 && cave[agentX][agentY + 1] == WUMPUS) {
            return true;
        }
        return false;
    }

    private static boolean checkBreeze() {
        if (agentX > 0 && cave[agentX - 1][agentY] == PIT) {
            return true;
        }
        if (agentX < SIZE - 1 && cave[agentX + 1][agentY] == PIT) {
            return true;
        }
        if (agentY > 0 && cave[agentX][agentY - 1] == PIT) {
            return true;
        }
        if (agentY < SIZE - 1 && cave[agentX][agentY + 1] == PIT) {
            return true;
        }
        return false;
    }

    private static boolean checkGlitter() {
        return cave[agentX][agentY] == GOLD;
    }

    private static boolean checkBump() {
        if ((agentX == 0 || visited[agentX - 1][agentY]) &&
                (agentX == SIZE - 1 || visited[agentX + 1][agentY]) &&
                (agentY == 0 || visited[agentX][agentY - 1]) &&
                (agentY == SIZE - 1 || visited[agentX][agentY + 1])) {
            return true;
        }
        return false;
    }

    private static boolean checkScream() {

        return false;
    }

    private static String selectAction(boolean stench, boolean breeze, boolean glitter, boolean bump, boolean scream) {
        if (glitter) {
            return "Grab";
        } else if (stench) {
            System.out.println("stench");
            return "Shoot";
        } else if (bump) {
            return "TurnRight";
        } else if (agentX == 0 && agentY == 0 && hasGold) {
            return "Climb";
        } else if (breeze) {
            System.out.println("breeze");
            return "GoForward";
        } else {
            return "GoForward";
        }
    }

    private static void performAction(String action) {
        switch (action) {
            case "GoForward":
                goForward();
                break;
            case "TurnLeft":
                turnLeft();
                break;
            case "TurnRight":
                turnRight();
                break;
            case "Grab":
                grab();
                break;
            case "Shoot":
                shoot();
                break;
            case "Climb":
                climb();
                break;
        }
    }

    private static void goForward() {
        if (agentX > 0 && !visited[agentX - 1][agentY]) {
            agentX--;
        } else if (agentX < SIZE - 1 && !visited[agentX + 1][agentY]) {
            agentX++;
        } else if (agentY > 0 && !visited[agentX][agentY - 1]) {
            agentY--;
        } else if (agentY < SIZE - 1 && !visited[agentX][agentY + 1]) {
            agentY++;
        }
        visited[agentX][agentY] = true;
    }

    private static void turnLeft() {
        if (agentX == 0 && agentY == 0) {
            agentX = 1;
            agentY = 0;
        } else if (agentX == 1 && agentY == 0) {
            agentX = 1;
            agentY = 1;
        } else if (agentX == 1 && agentY == 1) {
            agentX = 0;
            agentY = 1;
        } else if (agentX == 0 && agentY == 1) {
            agentX = 0;
            agentY = 0;
        }
    }

    private static void turnRight() {
        if (agentX == 0 && agentY == 0) {
            agentX = 0;
            agentY = 1;
        } else if (agentX == 0 && agentY == 1) {
            agentX = 1;
            agentY = 1;
        } else if (agentX == 1 && agentY == 1) {
            agentX = 1;
            agentY = 0;
        } else if (agentX == 1 && agentY == 0) {
            agentX = 0;
            agentY = 0;
        }
    }

    private static void grab() {
        if (cave[agentX][agentY] == GOLD) {
            cave[agentX][agentY] = EMPTY;
            hasGold = true;
        }
    }

    private static void shoot() {
        if (agentX > 0 && cave[agentX - 1][agentY] == WUMPUS) {
            cave[agentX - 1][agentY] = EMPTY;
        } else if (agentX < SIZE - 1 && cave[agentX + 1][agentY] == WUMPUS) {
            cave[agentX + 1][agentY] = EMPTY;
        } else if (agentY > 0 && cave[agentX][agentY - 1] == WUMPUS) {
            cave[agentX][agentY - 1] = EMPTY;
        } else if (agentY < SIZE - 1 && cave[agentX][agentY + 1] == WUMPUS) {
            cave[agentX][agentY + 1] = EMPTY;
        }
    }

    private static void climb() {
        if (agentX == 0 && agentY == 0 && hasGold) {
            isGameover = true;
            not_gold = true;
            System.out.println("Game over: Agent climbed out of the cave with the gold!");
        }
    }

    private static boolean checkGameover() {
        if (visited[0][0] && hasGold) {
            isGameover = true;
            System.out.println("Game over: Agent found the gold and returned to (1,1)!");
        } else if (cave[agentX][agentY] == WUMPUS) {
            isGameover = true;
            System.out.println("Game over: Agent was eaten by the Wumpus!");
        } else if (cave[agentX][agentY] == PIT) {
            isGameover = true;
            System.out.println("Game over: Agent fell into a pit!");
        }
        return isGameover;
    }
}
