import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    private static int[][] map;
    private static Queue<int[]> teamHead = new ArrayDeque<>();

    private static int[] dr = {0, -1, 0, 1};
    private static int[] dc = {1, 0, -1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        map = new int[N][N];
        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
                if(map[i][j] == 1){
                    teamHead.add(new int[]{i, j});
                }
            }
        }
        
        int point = 0;
        for(int round = 0; round < K; round++){
            while(!teamHead.isEmpty()){
                int[] head_pos = teamHead.poll();

                boolean[][] visit = new boolean[N][N];
                visit[head_pos[0]][head_pos[1]] = true;

                Queue<int[]> q = new ArrayDeque<>();
                for(int k = 0; k < 4; k++){
                    int nr = head_pos[0] + dr[k];
                    int nc = head_pos[1] + dc[k];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N || map[nr][nc] == 0) continue;
                    if(map[nr][nc] == 2){
                        visit[nr][nc] = true;
                        q.add(new int[]{nr, nc, head_pos[0], head_pos[1]});
                    }else{
                        map[nr][nc] = 1;
                    }
                }
                
                while(!q.isEmpty()){
                    int[] info = q.poll();
                    int r = info[0];
                    int c = info[1];
                    int pr = info[2];
                    int pc = info[3];

                    map[pr][pc] = map[r][c];
                    if(map[r][c] == 4) break;

                    for(int k = 0; k < 4; k++){
                        int nr = r + dr[k];
                        int nc = c + dc[k];
                        if(nr < 0 || nr >= N || nc < 0 || nc >= N || map[nr][nc] == 0) continue; 
                        if(visit[nr][nc] || map[nr][nc] < map[r][c]) continue;
                        visit[nr][nc] = true;
                        q.add(new int[]{nr, nc, r, c});
                        break;
                    }
                    if(q.isEmpty() && map[r][c] != 3) map[r][c] = 3;
                }
            }
            
            int rnd = round % (4 * N);
            int d = rnd < N ? 0
                : rnd < 2*N ? 1
                : rnd < 3*N ? 2
                : 3;

            int r = rnd < N ? rnd
                : rnd < 2*N ? N - 1
                : rnd < 3*N ? N - (rnd % N) - 1
                : 0;

            int c = rnd < N ? 0
                : rnd < 2*N ? rnd % N
                : rnd < 3*N ? N - 1
                : N - (rnd % N) - 1;

            boolean[][] visit = new boolean[N][N];
            for(int i = 0; i < 7; i++){
                int nr = r + (dr[d] * i);
                int nc = c + (dc[d] * i);
                if(map[nr][nc] == 4 || map[nr][nc] == 0 || visit[nr][nc]) continue;
                visit[nr][nc] = true;
                Queue<int[]> q = new ArrayDeque<>();
                q.add(new int[]{nr, nc, 1});

                int[] head = new int[2];
                int[] tail = new int[2];
                while(!q.isEmpty()){
                    int[] info = q.poll();
                    if(map[info[0]][info[1]] == 1){
                        point += (info[2] * info[2]);
                        head[0] = info[0];
                        head[1] = info[1];
                    }else if(map[info[0]][info[1]] == 3){
                        tail[0] = info[0];
                        tail[1] = info[1];
                    }

                    for(int j = 0; j < 4; j++){
                        int new_r = info[0] + dr[j];
                        int new_c = info[1] + dc[j];
                        if(new_r < 0 || new_r >= N || new_c < 0 || new_c >= N || map[new_r][new_c] == 0) continue;
                        if(visit[new_r][new_c] || map[new_r][new_c] == 4) continue;
                        if(map[info[0]][info[1]] == 3 && map[new_r][new_c] == 1) continue;
                        visit[new_r][new_c] = true;
                        q.add(new int[]{new_r, new_c, info[2] + 1});
                    }
                }

                map[head[0]][head[1]] = 3;
                map[tail[0]][tail[1]] = 1;
            }

            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(map[i][j] == 1){
                        teamHead.add(new int[]{i, j});
                    }
                }
            }
        }

        System.out.println(point);
    }
}