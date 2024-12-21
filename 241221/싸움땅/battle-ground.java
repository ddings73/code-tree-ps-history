import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    private static PriorityQueue<Integer>[][] map;
    private static int[][] player_map;

    private static Player[] players;

    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};

    private static class Player{
        int id, r, c, d, s, gun, point;
        Player(int id, int r, int c, int d, int s){
            this.id = id;
            this.r = r;
            this.c = c;
            this.d = d;
            this.s = s;
            this.gun = 0;
            this.point = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        map = new PriorityQueue[N + 1][N + 1];
        player_map = new int[N + 1][N + 1];

        for(int i = 0; i <= N; i++){
            for(int j = 0; j <= N; j++){
                map[i][j] = new PriorityQueue<>((o1, o2)->Integer.compare(o2, o1));
            }
        }

        for(int i = 1; i <= N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 1; j <= N; j++){
                int gun = Integer.parseInt(stk.nextToken());

                if(gun == 0) continue;
                map[i][j].add(gun);
            }
        }

        players = new Player[M + 1];
        for(int i = 1; i <= M; i++){
            stk = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(stk.nextToken());
            int y = Integer.parseInt(stk.nextToken());
            int d = Integer.parseInt(stk.nextToken());
            int s = Integer.parseInt(stk.nextToken());
            Player player = new Player(i,x,y,d,s);

            player_map[x][y] = i;
            players[i] = player;
        }

        for(int round = 1; round <= K; round++){
            for(int i = 1; i <= M; i++){
                Player p1 = players[i];
                player_map[p1.r][p1.c] = 0;

                int nr = p1.r + dr[p1.d];
                int nc = p1.c + dc[p1.d];
                if(nr <= 0 || nr > N || nc <= 0 || nc > N){
                    p1.d = (p1.d + 2) % 4;
                    nr = p1.r + dr[p1.d];
                    nc = p1.c + dc[p1.d];
                }

                p1.r = nr;
                p1.c = nc;

                Player p2 = player_map[nr][nc] != 0 ? players[player_map[nr][nc]] : null;
                if(p2 != null){
                    int p1_power = p1.s + p1.gun;
                    int p2_power = p2.s + p2.gun;
                    int point = Math.abs(p1_power - p2_power);

                    Player winner = null, loser = null;
                    if(p1_power > p2_power || (p1_power == p2_power && p1.s > p2.s)){
                        p1.point += point;
                        winner = p1;
                        loser = p2;
                    }else{
                        p2.point += point;
                        winner = p2;
                        loser = p1;
                    }
                    
                    // loser
                    if(loser.gun > 0){
                        map[loser.r][loser.c].add(loser.gun);
                        loser.gun = 0;
                    }

                    for(int j = 0; j < 4; j++){
                        nr = loser.r + dr[(loser.d + j) % 4];
                        nc = loser.c + dc[(loser.d + j) % 4];
                        if(nr <= 0 || nr > N || nc <= 0 || nc > N || player_map[nr][nc] > 0) continue;
                        loser.r = nr;
                        loser.c = nc;
                        loser.d = (loser.d + j) % 4;

                        map[nr][nc].add(loser.gun);
                        loser.gun = map[nr][nc].poll();
                        break;
                    }

                    map[winner.r][winner.c].add(winner.gun);
                    winner.gun = map[winner.r][winner.c].poll();

                    player_map[loser.r][loser.c] = loser.id;
                    player_map[winner.r][winner.c] = winner.id;
                }else{
                    map[p1.r][p1.c].add(p1.gun);
                    p1.gun = map[p1.r][p1.c].poll();

                    player_map[p1.r][p1.c] = i;
                }
            }
        }

        for(int i = 1; i <= M; i++){
            System.out.print(players[i].point + " ");
        }
    }
}