import java.util.*;
import java.io.*;

public class Main {
    private static int N, M;
    private static int[][] map;

    private static int[] dr = new int[]{0, 1, 0, -1};
    private static int[] dc = new int[]{1, 0, -1, 0};
    private static Dice dice = new Dice(0, 0, 0);
    private static class Dice{
        int r, c, d;

        int under = 6, top = 1;
        int left = 4, right= 3;
        int up = 5, down = 2;

        Dice(int r, int c, int d){
            this.r = r;
            this.c = c;
            this.d = d;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());

        map = new int[N][N];
        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        int point = 0;
        for(int roll = 1; roll <= M; roll++){
            // 주사위 굴림
            rollDice();

            // 포인트 획득
            point += getPoint();

            // 방향변경
            if(dice.under > map[dice.r][dice.c]){
                dice.d = (dice.d + 1) % 4;
            }else if(dice.under < map[dice.r][dice.c]){
                dice.d = (dice.d + 3) % 4;
            }
        }

        System.out.println(point);
    }

    private static void rollDice(){
        int dir = dice.d;

        int nr = dice.r + dr[dir];
        int nc = dice.c + dc[dir];

        if(nr < 0 || nr >= N || nc < 0 || nc >= N){
            dir = (dir + 2) % 4;
            dice.d = dir;

            nr = dice.r + dr[dir];
            nc = dice.c + dc[dir];
        }

        dice.r = nr;
        dice.c = nc;

        int tmp = dice.under;
        if(dir == 0){ // 우
            dice.under = dice.right;
            dice.right = dice.top;
            dice.top = dice.left;
            dice.left = tmp;
        }else if(dir == 1){ // 하
            dice.under = dice.down;
            dice.down = dice.top;
            dice.top = dice.up;
            dice.up = tmp;
        }else if(dir == 2){ // 좌
            dice.under = dice.left;
            dice.left = dice.top;
            dice.top = dice.right;
            dice.right = tmp;
        }else if(dir == 3){ // 상
            dice.under = dice.up;
            dice.up = dice.top;
            dice.top = dice.down;
            dice.down = tmp;
        }
    }

    private static int getPoint(){
        int count = 1;
        int value = map[dice.r][dice.c];

        boolean[][] visit = new boolean[N][N];
        visit[dice.r][dice.c] = true;

        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{dice.r, dice.c});
        while(!q.isEmpty()){
            int[] pos = q.poll();

            for(int i = 0; i < 4; i++){
                int nr = pos[0] + dr[i];
                int nc = pos[1] + dc[i];
                if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                if(visit[nr][nc] || map[nr][nc] != value) continue;

                visit[nr][nc] = true;
                count++;

                q.add(new int[]{nr, nc});
            }
        }
        return (count * value);
    } 
}