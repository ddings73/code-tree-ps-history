import java.util.*;
import java.io.*;

public class Main {

    private static int N, L, R;
    private static int[][] board;

    private static final int[] dr = {1, -1, 0, 0};
    private static final int[] dc = {0, 0, 1, -1};
    public static void main(String[] args) {
        input();


        int limit = 0;
        boolean move = true;
        while(move && limit++ < 2000){
            move = false;

            boolean[][] visit = new boolean[N][N];
            // BFS
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(visit[i][j]) continue;
                    visit[i][j] = true;

                    List<int[]> list = new ArrayList<>();

                    int sum = board[i][j];
                    Queue<int[]> q = new ArrayDeque<>();
                    q.add(new int[]{i, j});
                    while(!q.isEmpty()){
                        int[] p = q.poll();
                        list.add(p);
                        for(int k = 0; k < 4; k++){
                            int nr = p[0] + dr[k];
                            int nc = p[1] + dc[k];
                            if(nr < 0 || nr >= N || nc < 0 || nc >= N || visit[nr][nc]) continue;

                            int gap = Math.abs(board[p[0]][p[1]] - board[nr][nc]);
                            if(gap < L || gap > R) continue;

                            sum += board[nr][nc];
                            visit[nr][nc] = true;
                            q.add(new int[]{nr, nc});
                        }
                    }
                    
                    if(list.size() == 1) continue;
                    
                    move = true;
                    int value = sum / list.size();
                    for(int[] p : list){
                        board[p[0]][p[1]] = value;
                    }
                }
            }
        }

        System.out.println(limit - 1);
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            L = Integer.parseInt(stk.nextToken());
            R = Integer.parseInt(stk.nextToken());

            board = new int[N][N];
            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}