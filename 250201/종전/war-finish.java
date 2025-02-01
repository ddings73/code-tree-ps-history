import java.util.*;
import java.io.*;

public class Main {

    private static int MAX = 100*20*20;
    private static int N;
    private static int[][] board;
    
    /*
        0 => 우측 상단 방향
        1 => 좌측 상단 방향
        2 => 좌측 하단 방향
        4 => 우측 하단 방향
    */
    private static int[] dr = new int[]{-1, -1, 1, 1};
    private static int[] dc = new int[]{1, -1, -1, 1};

    public static void main(String[] args) {
        input();

        // 기울어진 직사각형을 만들었을 때, 생성되는 5개의 부족
        // 각 부족들의 인원 수 차이가 가장 적은 경우(max(인원) - min(인원)이 가장 적은 경우)
        // 해당하는 값(max(인원) - min(인원)) 출력하기
        int answer = MAX;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                int[][] points = new int[4][2];
                points[0][0] = i;
                points[0][1] = j;

                int value = makeRectangle(points, 1);
                answer = Math.min(answer, value);
            }
        }

        System.out.println(answer);
    }

    private static int makeRectangle(int[][] points, int idx){
        if(idx == 3){
            int r_gap = Math.abs(points[1][0] - points[0][0]);
            int c_gap = Math.abs(points[1][1] - points[0][1]);
            points[3][0] = points[2][0] + r_gap;
            points[3][1] = points[2][1] - c_gap;

            if(points[3][0] < 0 || points[3][0] >= N || points[3][1] < 0 || points[3][1] >= N) return MAX;

            int[][] area = new int[N][N];
            boolean[][] border = new boolean[N][N];

            // 경계선 체크
            for(int i = 0; i < 4; i++){
                int r = points[i][0];
                int c = points[i][1];

                int nxt = (i + 1) % 4;
                while(r != points[nxt][0] || c != points[nxt][1]){
                    border[r][c] = true;
                    r += dr[i];
                    c += dc[i];
                }
            }
            
            // 2 ~ 5 까지의 영역 색칠
            for(int i = 0; i < N; i++){
                Arrays.fill(area[i], 1);
                for(int j = 0; j < N; j++){
                    if(border[i][j]) break;
                    if(0 <= i && i < points[3][0] && 0 <= j && j <= points[2][1]){
                        area[i][j] = 2;
                    }else if(points[3][0] <= i && i < N && 0 <= j && j < points[0][1]){
                        area[i][j] = 4;
                    }
                }

                for(int j = N - 1; j >= 0; j--){
                    if(border[i][j]) break;
                    if(0 <= i && i <= points[1][0] && points[2][1] < j && j < N){
                        area[i][j] = 3;
                    }else if(points[1][0] < i && i < N && points[0][1] <= j && j < N){
                        area[i][j] = 5;
                    }
                }
            }


            int[] score = new int[6];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    score[area[i][j]] += board[i][j];
                }
            }

            int max = -1, min = MAX;
            for(int i = 1; i <= 5; i++){
                max = Math.max(max, score[i]);
                min = Math.min(min, score[i]);
            }
     
            return max - min;
        }

        int result = MAX;
        int r = points[idx - 1][0];
        int c = points[idx - 1][1];
        while(0 <= r && r < N && 0 <= c && c < N){
            r += dr[idx - 1];
            c += dc[idx - 1];

            if(0 <= r && r < N && 0 <= c && c < N){
                points[idx][0] = r;
                points[idx][1] = c;
                result = Math.min(result, makeRectangle(points, idx + 1));
            }
        }

        return result;
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            N = Integer.parseInt(br.readLine());

            board = new int[N][N];
            for(int i = 0; i < N; i++){
                StringTokenizer stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}