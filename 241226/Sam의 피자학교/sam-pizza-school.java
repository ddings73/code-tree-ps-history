import java.util.*;
import java.io.*;

public class Main {
    private static int N, K;
    
    private static Queue<Integer> q = new ArrayDeque<>();
    private static int[] dr = {1, -1, 0, 0};
    private static int[] dc = {0, 0, 1, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        stk = new StringTokenizer(br.readLine());
        for(int i = 0; i < N; i++){
            int value = Integer.parseInt(stk.nextToken());
            q.add(value);
        }

        int round = 0;
        while(true){
            int min_v = 3000;
            int max_v = 0;
            for(Integer value : q){
                min_v = Math.min(min_v, value);
                max_v = Math.max(max_v, value);
            }

            if(max_v - min_v <= K) break;
            round++;

            // step1
            Queue<Integer> tmp = new ArrayDeque<>();
            while(!q.isEmpty()){
                int value = q.poll();
                if(value == min_v) value++;
                tmp.add(value);
            }
            q = new ArrayDeque<>(tmp);
            
            // step2
            int[][] rolled = roll();

            // step3
            hold(rolled);

            // step4
            int col = q.size() / 2;
            int[][] arr = new int[2][col];
            for(int i = col - 1; i >= 0; i--) arr[0][i] = q.poll();
            for(int i = 0; i < col; i++) arr[1][i] = q.poll();

            rolled = new int[4][col / 2];
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < col / 2; j++){
                    rolled[i][j] = arr[1 - i][col / 2 - 1 - j];
                }
            }

            for(int i = 2; i < 4; i++){
                for(int j = 0; j < col / 2; j++){
                    rolled[i][j] = arr[i - 2][j + 2];
                }
            }

            // step5
            hold(rolled);
        }

        System.out.println(round);
    }

    private static int[][] roll(){
        int[][] rolled = new int[1][1];
        
        rolled[0][0] = q.poll();
        while(!q.isEmpty()){
            int row = rolled.length;
            int col = rolled[0].length;

            if(row > q.size()) break;

            int[][] arr = row == col ? new int[row + 1][col] : new int[row][col + 1];
            for(int i = 0; i < col; i++){
                for(int j = 0; j < row; j++){
                    arr[i][j] = rolled[row - 1 - j][i];
                }
            }
            for(int i = 0; i < arr[0].length; i++){
                arr[arr.length - 1][i] = q.poll();
            }

            rolled = arr;
        }

        int row = rolled.length;
        int col = rolled[0].length + q.size();
        int[][] arr = new int[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(i != row - 1 && j >= rolled[0].length) arr[i][j] = -1;
                else if(i == row - 1 && j >= rolled[0].length) arr[i][j] = q.poll();
                else arr[i][j] = rolled[i][j];
            }
        }

        rolled = arr;
        return rolled;
    }

    private static void hold(int[][] arr){
        int row = arr.length;
        int col = arr[0].length;

        int[][] tmp = new int[row][col];
        boolean[][] visit = new boolean[row][col];
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                if(arr[i][j] == -1) continue;
                visit[i][j] = true;

                for(int k = 0; k < 4; k++){
                    int nr = i + dr[k];
                    int nc = j + dc[k];
                    if(nr < 0 || nr >= row || nc < 0 || nc >= col || arr[nr][nc] == -1) continue;
                    if(visit[nr][nc]) continue;
                    int diff = Math.abs(arr[nr][nc] - arr[i][j]);
                    int d = diff / 5;

                    if(d == 0) continue;
                    if(arr[i][j] < arr[nr][nc]){
                        tmp[i][j] += d;
                        tmp[nr][nc] -= d;
                    }else{
                        tmp[i][j] -= d;
                        tmp[nr][nc] += d;
                    }
                }
            }
        }

        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                arr[i][j] += tmp[i][j];
            }
        }

        for(int i = 0; i < col; i++){
            for(int j = row - 1; j >= 0; j--){
                if(arr[j][i] == -1) continue;
                q.add(arr[j][i]);
            }
        }
    }
}