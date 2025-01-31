import java.util.*;
import java.io.*;

public class Main {
    private static int WHITE = 0, RED = 1, BLUE = 2;

    private static int N, K;
    private static int[][] board, map;

    private static Horse[] horses;

    private static int[] dr = {0, 0, -1, 1};
    private static int[] dc = {1, -1, 0, 0};

    private static class Horse{
        int num, r, c, d;
        Horse prev, next;

        Horse(int num, int r, int c, int d, Horse prev, Horse next){
            this.num = num;
            this.r = r;
            this.c = c;
            this.d = d;
            this.prev = prev;
            this.next = next;
        }

        private void reverseDir(){
            this.d = this.d == 0 ? 1
                : this.d == 1 ? 0
                : this.d == 2 ? 3
                : 2;
        }
    }
    public static void main(String[] args) {
        input();

        int turn = 0;
        boolean endGame = false;
        while(!endGame && turn++ < 1000){
            endGame = false;

            // 말 순서대로 이동
            for(int i = 0; !endGame && i < K; i++){
                Horse horse = horses[i];

                int nr = horse.r + dr[horse.d];
                int nc = horse.c + dc[horse.d];

                
                // 파랑 or 벗어남 => 방향 반전 + 이동
                if(nr < 0 || nr >= N || nc < 0 || nc >= N || board[nr][nc] == BLUE){
                    horse.reverseDir();
                    nr = horse.r + dr[horse.d];
                    nc = horse.c + dc[horse.d];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N || board[nr][nc] == BLUE) continue;
                }
                
                // 빨강 => 순서 반전 + 이동
                if(board[nr][nc] == RED){
                    if(horse.prev != null){
                        horse.prev.next = null;
                        horse.prev = null;
                    }else{
                        map[horse.r][horse.c] = 0;
                    }

                    Horse head = horse;
                    while(head.next != null) head = head.next;
                    
                    horse = head;

                    while(head != null){
                        Horse nxt = head.prev;
                        head.prev = head.next;
                        head.next = nxt;
                        head = nxt;
                    }
                }

                // 원래 위치
                if(horse.num + 1 == map[horse.r][horse.c]) map[horse.r][horse.c] = 0;
                else if(horse.prev != null){
                    horse.prev.next = null;
                    horse.prev = null;
                }

                // 새 위치
                if(map[nr][nc] == 0) map[nr][nc] = horse.num + 1;
                else{
                    Horse there = horses[map[nr][nc] - 1];
                    while(there.next != null) there = there.next;

                    there.next = horse;
                    horse.prev = there;
                }
                
                // 새 위치에서 내 위에 있는 모든 말들 위치 갱신
                while(horse != null){
                    horse.r = nr;
                    horse.c = nc;
                    horse = horse.next;
                }

                // 말 4개 쌓였는지 확인
                int count = 0;
                horse = horses[map[nr][nc] - 1];
                while(horse != null){
                    count++;
                    horse = horse.next;
                }

                if(count >= 4) endGame = true;
            }
        }

        if(turn > 1000) turn = -1;
        System.out.println(turn);
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            K = Integer.parseInt(stk.nextToken());

            // 윳놀이 판 정보
            board = new int[N][N];
            map = new int[N][N];
            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                }
            }

            // 말 정보
            // 0 1 2 3 => 오 왼 위 아 
            horses = new Horse[K];
            for(int i = 0; i < K; i++){
                stk = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(stk.nextToken()) - 1;
                int y = Integer.parseInt(stk.nextToken()) - 1;
                int d = Integer.parseInt(stk.nextToken()) - 1;

                map[x][y] = i + 1;
                horses[i] = new Horse(i, x, y, d, null, null);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}