import java.util.*;
import java.io.*;

public class Main {

    private static int N, M, T;
    private static int[][] board;
    
    private static int[] storm;
    /*
        0 -> 3 반시계 
        3 -> 0 시계
    */
    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, -1, 0, 1};
    public static void main(String[] args) {
        input();

        while(T-- > 0){
            board = spreadDust(); // 먼지 확산
            
            // 시공의 돌풍 움직임
            for(int sIdx = 0; sIdx < 2; sIdx++){
                int r = storm[sIdx], c = 0;
                int d = sIdx == 0 ? 0 : 2;
                boolean plus = sIdx == 1;

                // 위 0 -> 3 -> 2 -> 1
                // 아래 2 -> 3 -> 0 -> 1
                while(areaCheck(r, c)){
                    int nr = r + dr[d];
                    int nc = c + dc[d];
                    if(!areaCheck(nr, nc) || (!plus && nr > storm[sIdx]) || (plus && nr < storm[sIdx])){
                        d = plus ? (d + 1) % 4 : d - 1;
                        if(d < 0) d = 3;
                        nr = r + dr[d];
                        nc = c + dc[d];
                    }

                    if(nr == storm[sIdx] && nc == 0){
                        board[r][c] = 0;
                        break;
                    }
                    
                    if(board[r][c] != -1){
                        board[r][c] = board[nr][nc];
                    }

                    r = nr;
                    c = nc;
                }
            }
        }

        int total_dust = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(board[i][j] == -1) continue;
                total_dust += board[i][j];
            }
        }
        
        System.out.println(total_dust);
    }

    private static boolean areaCheck(int r, int c){
        return 0 <= r && r < N && 0 <= c && c < M;
    }

    private static int[][] spreadDust(){
        int[][] newBoard = new int[N][M];
        for(int r = 0; r < N; r++){
            for(int c = 0; c < M; c++){
                newBoard[r][c] += board[r][c];
                int dust = (int)(board[r][c] / 5);
                for(int i = 0; i < 4; i++){
                    int nr = r + dr[i];
                    int nc = c + dc[i];
                    if(!areaCheck(nr, nc) || board[nr][nc] == -1) continue;
                    newBoard[r][c] -= dust;
                    newBoard[nr][nc] += dust;                    
                }
            }
        }

        return newBoard;
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());

            N = Integer.parseInt(stk.nextToken());
            M = Integer.parseInt(stk.nextToken());
            T = Integer.parseInt(stk.nextToken());

            board = new int[N][M];
            storm = new int[2];
            int stormIdx = 0;

            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < M; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                    if(board[i][j] == -1){
                        storm[stormIdx++] = i;
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}