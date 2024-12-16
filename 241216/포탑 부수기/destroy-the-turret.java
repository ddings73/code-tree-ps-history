import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    private static Turret[][] map;
    
    private static int[] dr = {0, 1, 0, -1, -1, -1, 1, 1};
    private static int[] dc = {1, 0, -1, 0, -1, 1, -1, 1};
    private static class Turret{
        int r, c, atk, atk_time;
        Turret(int r, int c, int atk){
            this.r = r;
            this.c = c;
            this.atk = atk;
            this.atk_time = 0;
        }

        @Override
        public boolean equals(Object obj){
            if(obj instanceof Turret){
                Turret turret = (Turret) obj;
                return this.r == turret.r && this.c == turret.c;
            }
            return false;
        }
    }
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());
        
        map = new Turret[N][M];
        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < M; j++){
                int atk = Integer.parseInt(stk.nextToken());
                map[i][j] = new Turret(i, j, atk);
            }
        }

        for(int time = 1; time <= K; time++){
            Turret attacker = findAttacker();
            Turret target = findTarget();
            
            if(attacker.equals(target)) break;
            attacker.atk_time = time;
            attacker.atk += (N + M);

            boolean[][] visit = new boolean[N][M];
            Turret[][] parent = new Turret[N][M];

            Queue<int[]> q = new ArrayDeque<>();
            q.add(new int[]{attacker.r, attacker.c});
            visit[attacker.r][attacker.c] = true;

            while(!q.isEmpty()){
                int[] info = q.poll();
                int r = info[0];
                int c = info[1];

                if(target.r == r && target.c == c) break;
                for(int i = 0; i < 4; i++){
                    int nr = r + dr[i];
                    int nc = c + dc[i];

                    if(nr == N) nr = 0; if(nr == -1) nr = N - 1;
                    if(nc == M) nc = 0; if(nc == -1) nc = M - 1;
                    if(map[nr][nc].atk <= 0 || visit[nr][nc]) continue;
                    visit[nr][nc] = true;
                    parent[nr][nc] = map[r][c];
                    q.add(new int[]{nr, nc}); 
                }
            }
            
            boolean[][] except = new boolean[N][M];
            except[attacker.r][attacker.c] = true;
            except[target.r][target.c] = true;
            target.atk -= attacker.atk;

            if(!visit[target.r][target.c]){
                for(int i = 0; i < 8; i++){
                    int nr = target.r + dr[i];
                    int nc = target.c + dc[i];

                    if(nr == N) nr = 0; if(nr == -1) nr = N - 1;
                    if(nc == M) nc = 0; if(nc == -1) nc = M - 1;
                    if(map[nr][nc].atk <= 0 || (attacker.r == nr && attacker.c == nc)) continue;
                    map[nr][nc].atk -= (attacker.atk / 2);
                    except[nr][nc] = true;
                }
            }else{
                int r = target.r;
                int c = target.c;

                Turret turret = parent[r][c];
                while(!turret.equals(attacker)){
                    except[turret.r][turret.c] = true;
                    map[turret.r][turret.c].atk -= (attacker.atk / 2);
                    turret = parent[turret.r][turret.c];
                }
            }

            // 정비
            for(int i = 0; i < N; i++){
                for(int j = 0; j < M; j++){
                    if(map[i][j].atk <= 0 || except[i][j]) continue;
                    map[i][j].atk++;
                }
            }
        }
        
        int max_atk = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                max_atk = Math.max(max_atk, map[i][j].atk);
            }
        }

        System.out.println(max_atk);
    }

    private static Turret findAttacker(){
        Turret attacker = null;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(map[i][j].atk <= 0) continue;
                if(attacker == null || compareForAttacker(attacker, map[i][j])){
                    attacker = map[i][j];
                }
            }
        }
        return attacker;
    }

    private static boolean compareForAttacker(Turret a, Turret b){
        if(a.atk > b.atk) return true;
        if(a.atk == b.atk){
            if(a.atk_time < b.atk_time) return true;
            if(a.atk_time == b.atk_time){
                if((a.r + a.c < b.r + b.c) || (a.r + a.c == b.r + b.c && a.c < b.c)) 
                    return true;
            }
        }
        return false;
    }

    
    private static Turret findTarget(){
        Turret target = null;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < M; j++){
                if(map[i][j].atk == 0) continue;
                if(target == null || compareForTarget(target, map[i][j])){
                    target = map[i][j];
                }
            }
        }
        return target;
    }

    private static boolean compareForTarget(Turret a, Turret b){
        if(a.atk < b.atk) return true;
        if(a.atk == b.atk){
            if(a.atk_time > b.atk_time) return true;
            if(a.atk_time == b.atk_time){
                if((a.r + a.c > b.r + b.c) || (a.r + a.c == b.r + b.c && a.c > b.c)) 
                    return true;
            }
        }
        return false;
    }
}