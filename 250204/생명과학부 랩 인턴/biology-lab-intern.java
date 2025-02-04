import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    
    private static int[][] board;
    private static Gompang[] gompangs;

    private static int[] dr = {-1, 1, 0, 0};
    private static int[] dc = {0, 0, 1, -1};
    private static class Gompang{
        int idx, r, c, s, d, b;
        Gompang(int r, int c, int s, int d, int b){
            this.r = r;
            this.c = c;
            this.s = s;
            this.d = d;
            this.b = b;
        }

        void move(){
            this.r += (dr[this.d] * this.s);
            this.c += (dc[this.d] * this.s);

            this.r = move_detail(this.r, N);
            this.c = move_detail(this.c, M);
        }

        private int move_detail(int p, int len){
            if(0 <= p && p < len) return p;

            boolean minus = p < 0;
            if(minus) p = -p;

            int div = p / (len - 1);
            int mod = p % (len - 1);

            if(div % 2 == 0 || mod == 0){
                p %= (len - 1);
                if(mod == 0) p += (len - 1);
            }else{
                p = len - 1 - mod;
            }

            if((minus && div % 2 == 0) || (!minus && div % 2 == 1)){
                this.d = this.d == 0 ? 1
                    : this.d == 1 ? 0
                    : this.d == 2 ? 3 : 2;
            } 

            return p;
        }
    }

    public static void main(String[] args) {
        input();
        
        int answer = 0;
        for(int intern = 0; intern < M; intern++){
            // 곰팡이 채취
            for(int row = 0; row < N; row++){
                if(board[row][intern] == 0) continue;
                int t_idx = board[row][intern];
                board[row][intern] = 0;

                answer += gompangs[t_idx].b;
                gompangs[t_idx] = null;

                break;
            }
            
            // 곰팡이 이동( 좌표 변경 및 기존 좌표 비우기 )
            for(int i = 1; i <= K; i++){
                Gompang g = gompangs[i];
                if(g == null) continue;
                board[g.r][g.c] = 0;

                // 좌표 조정
                g.move();
            }

            // 새로운 좌표를 대상으로 곰팡이 크기 경합
            for(int i = 1; i <= K; i++){
                Gompang g = gompangs[i];
                if(g == null) continue;
                if(board[g.r][g.c] == 0){
                    board[g.r][g.c] = i;
                }else{
                    int t_idx = board[g.r][g.c];
                    Gompang t_g = gompangs[t_idx];
                    if(t_g.b > g.b){
                        gompangs[i] = null;
                    }else if(t_g.b < g.b){
                        gompangs[t_idx] = null;
                        board[g.r][g.c] = i;
                    }
                }
            }
        }

        System.out.println(answer);
    }

    private static void input(){

        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            M = Integer.parseInt(stk.nextToken());
            K = Integer.parseInt(stk.nextToken());

            board = new int[N][M];
            gompangs = new Gompang[K + 1];
            for(int i = 1; i <= K; i++){
                stk = new StringTokenizer(br.readLine());
                // 곰팡이 위치 0 ~ N, 0 ~ M 으로 조정
                int r = Integer.parseInt(stk.nextToken()) - 1;
                int c = Integer.parseInt(stk.nextToken()) - 1;
                // 1초동안 움직이는 거리 
                int s = Integer.parseInt(stk.nextToken());
                // 이동방향( 1 ~ 4 : 상하우좌) 0 ~ 3 으로 조정
                int d = Integer.parseInt(stk.nextToken()) - 1; 
                // 곰팡이 크기( 전부 다름 )
                int b = Integer.parseInt(stk.nextToken());

                board[r][c] = i;
                gompangs[i] = new Gompang(r,c,s,d,b);
            }        
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}