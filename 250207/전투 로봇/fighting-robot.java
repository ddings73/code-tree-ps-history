import java.util.*;
import java.io.*;

public class Main {
    private static int N;
    private static int[][] board; 

    private static Robot robot;
    
    private static class Robot{
        int r, c;
        int killCount, level;

        int[] dr, dc;
        Robot(int r, int c){
            this.r = r;
            this.c = c;
            this.level = 2;
            this.killCount = 0;

            dr = new int[]{1, -1, 0, 0};
            dc = new int[]{0, 0, 1, -1};  
        }

        void increaseKillCount(){
            this.killCount++;
            if(this.killCount == this.level){
                this.level++;
                this.killCount = 0;
            }
        }
    }
    public static void main(String[] args) {
        input();

        int time = 0;
        boolean kill = true;
        while(kill){
            kill = false;

            int[] target = new int[]{-1, -1, N * N};

            int[][] visit = new int[N][N];
            for(int i = 0; i < N; i++) Arrays.fill(visit[i], N * N);

            Queue<int[]> q = new ArrayDeque<>();
            q.add(new int[]{robot.r, robot.c, 0});
            visit[robot.r][robot.c] = 0;
            while(!q.isEmpty()){
                int[] info = q.poll();
                int r = info[0];
                int c = info[1];
                int move = info[2];

                // 로봇보다 레벨이 작으면서 거리가 가까운 몬스터라면
                if(board[r][c] != 0 && board[r][c] < robot.level && move <= target[2]){
                    boolean task = move < target[2];                     // 거리가 가장 가까운가?
                    if(!task) task = move == target[2] && r < target[0]; // 거리는 같지만 행이 작은가?
                    if(!task) task = move == target[2] && r == target[0] && c < target[1];   // 거리와 행이 같지만 열이 작은가?
                    
                    if(task){
                        target[0] = r;
                        target[1] = c;
                        target[2] = move;
                    }
                }

                for(int i = 0; i < 4; i++){
                    int nr = r + robot.dr[i];
                    int nc = c + robot.dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(visit[nr][nc] <= move || board[nr][nc] > robot.level) continue;
                    visit[nr][nc] = move + 1;
                    q.add(new int[]{nr, nc, move + 1});
                }
            }

            if(target[0] != -1 && target[1] != -1){
                kill = true;
                robot.r = target[0];
                robot.c = target[1];
                robot.increaseKillCount();

                board[target[0]][target[1]] = 0;

                time += target[2];
            }
        }

        System.out.println(time);
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            N = Integer.parseInt(br.readLine());
            board = new int[N][N];

            for(int i = 0; i < N; i++){
                StringTokenizer stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                    if(board[i][j] == 9){
                        robot = new Robot(i, j);
                        board[i][j] = 0;
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}