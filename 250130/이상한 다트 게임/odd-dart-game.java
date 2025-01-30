import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, Q;

    private static int[][] board, query;
    public static void main(String[] args) {
        input();

        for(int[] q : query){
            int x = q[0]; // 타겟
            int d = q[1]; // 방향
            int k = q[2]; // 횟수
            
            // 원판 돌리기
            for(int num = x - 1; num < N; num += x){
                int[] clone = board[num].clone();
                for(int idx = 0; idx < M; idx++){
                    int c_idx = d == 0 
                        ? (idx + M - k) % M // 시계 
                        : (idx + k) % M;        // 반시계

                    board[num][idx] = clone[c_idx];
                }
            }

            // 인접 수 삭제
            // 삭제되는 수가 없다면 정규화
            boolean removed = false;
            boolean[][] remove_check = new boolean[N][M];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < M; j++){
                    if(board[i][j] == 0) continue;
                    boolean flag = false;
                    // 동일 원판 양옆 확인
                    int left = j - 1 < 0 ? M - 1 : j - 1;
                    int right = j + 1 >= M ? 0 : j + 1;
                    flag = flag || sameCheck(i, left, board[i][j], remove_check);
                    flag = flag || sameCheck(i, right, board[i][j], remove_check);
                    flag = flag || sameCheck(i - 1, j, board[i][j], remove_check);
                    flag = flag || sameCheck(i + 1, j, board[i][j], remove_check);
                    

                    if(flag){
                        remove_check[i][j] = true;
                        removed = true;
                    }
                }
            }

            for(int i = 0; i < N; i++){
                for(int j = 0; j < M; j++){
                    if(remove_check[i][j]) board[i][j] = 0;
                }
            }

            if(!removed) normalize();
        }

        int answer = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                answer += board[i][j];
            }
        }

        System.out.println(answer);
    }
    
    private static boolean sameCheck(int r, int m, int value, boolean[][] remove_check){
        if(r < 0 || r >= N || board[r][m] != value) return false;
        remove_check[r][m] = true;
        return true;
    }

    private static void normalize(){
        int sum = 0, count = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(board[i][j] == 0) continue;
                sum += board[i][j];
                count++;
            }
        }

        if(count == 0) return;

        int avg = (int)(sum / count);
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(board[i][j] == 0) continue;
                if(board[i][j] < avg) board[i][j]++;
                else if(board[i][j] > avg) board[i][j]--;
            }
        }
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            M = Integer.parseInt(stk.nextToken());
            Q = Integer.parseInt(stk.nextToken());

            // 원판에 적힌 수
            board = new int[N][M];
            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < M; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                }
            }

            // 회전 정보
            query = new int[Q][];
            for(int i = 0; i < Q; i++){
                stk = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(stk.nextToken());
                int d = Integer.parseInt(stk.nextToken());
                int k = Integer.parseInt(stk.nextToken());

                query[i] = new int[]{x,d,k};
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}