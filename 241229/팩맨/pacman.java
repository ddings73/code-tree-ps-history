import java.util.*;
import java.io.*;

public class Main {
    private static int N = 4, M, T;

    private static int[] pacman = new int[2];
    private static int global_kill = 0;
    private static int[][] global_visit;

    private static Queue<int[]> ghost = new ArrayDeque<>();
    private static Queue<int[]> egg = new ArrayDeque<>();
    private static int[][] ghost_map;

    private static int[] dr_man = {-1, 0, 1, 0};
    private static int[] dc_man = {0, -1, 0, 1};

    private static int[] dr_ghost = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static int[] dc_ghost = {0, -1, -1, -1, 0, 1, 1, 1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        M = Integer.parseInt(stk.nextToken());
        T = Integer.parseInt(stk.nextToken());

        stk = new StringTokenizer(br.readLine());
        pacman[0] = Integer.parseInt(stk.nextToken()) - 1;
        pacman[1] = Integer.parseInt(stk.nextToken()) - 1;

        ghost_map = new int[N][N];
        int[][] ghost_body_map = new int[N][N];
        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(stk.nextToken()) - 1;
            int c = Integer.parseInt(stk.nextToken()) - 1;
            int d = Integer.parseInt(stk.nextToken()) - 1;

            ghost.add(new int[]{r, c, d});
            ghost_map[r][c]++;
        }

        for(int round = 1; round <= T; round++){
            // 알 생성 및 유령 이동
            Queue<int[]> g_tmp = new ArrayDeque<>();
            while(!ghost.isEmpty()){
                int[] info = ghost.poll();
                egg.add(info.clone());

                ghost_map[info[0]][info[1]]--;
                int d = info[2];
                int nr = info[0] + dr_ghost[d];
                int nc = info[1] + dc_ghost[d];
                int count = 0;
                while(nr < 0 || nr >= N || nc < 0 || nc >= N || (pacman[0] == nr && pacman[1] == nc) || ghost_body_map[nr][nc] > 0){ // 시체조건 포함
                    d = (d + 1) % 8;
                    if(d == info[2]) break;
                    
                    nr = info[0] + dr_ghost[d];
                    nc = info[1] + dc_ghost[d];
                }

                if(nr < 0 || nr >= N || nc < 0 || nc >= N || (pacman[0] == nr && pacman[1] == nc) || ghost_body_map[nr][nc] > 0){
                    nr = info[0];
                    nc = info[1];
                }

                ghost_map[nr][nc]++;
                g_tmp.add(new int[]{nr, nc, d});
            }
            ghost = new ArrayDeque<>(g_tmp);

            // 팩맨 이동
            global_kill = 0;
            global_visit = new int[N][N];

            int r = pacman[0];
            int c = pacman[1];
            for(int i = 0; i < 4; i++){
                int nr = r + dr_man[i];
                int nc = c + dc_man[i];
                if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                int[][] visit = new int[N][N];
                visit[nr][nc]++;
                pacmanMove(nr, nc, 0, ghost_map[nr][nc], visit);
                visit[nr][nc]--;
            }

            g_tmp = new ArrayDeque<>();
            while(!ghost.isEmpty()){
                int[] info = ghost.poll();
                if(global_visit[info[0]][info[1]] > 0){
                    ghost_map[info[0]][info[1]]--;
                    ghost_body_map[info[0]][info[1]] = 3;
                    continue;
                }
                g_tmp.add(info);
            }
            ghost = new ArrayDeque<>(g_tmp);

            // 시체 소멸
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(ghost_body_map[i][j] == 0) continue;
                    ghost_body_map[i][j]--;
                }
            }

            // 알 부화
            while(!egg.isEmpty()){
                int[] info = egg.poll();
                ghost_map[info[0]][info[1]]++;
                ghost.add(info);
            }
        }

        System.out.println(ghost.size());
    }

    private static void pacmanMove(int r, int c, int time, int kill, int[][] visit){
        if(time == 2){
            if(global_kill < kill){
                pacman[0] = r;
                pacman[1] = c;

                global_kill = kill;
                global_visit = new int[N][];
                for(int i = 0; i < N; i++){
                    global_visit[i] = visit[i].clone();
                }
            }
            return;
        }

        for(int i = 0; i < 4; i++){
            int nr = r + dr_man[i];
            int nc = c + dc_man[i];
            if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
            int killupdate = visit[nr][nc] != 0 ? kill : kill + ghost_map[nr][nc];

            visit[nr][nc]++;
            pacmanMove(nr, nc, time + 1, killupdate, visit);
            visit[nr][nc]--;
        }
    }
}