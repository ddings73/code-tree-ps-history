import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    private static int[][] map;
    private static int[][] temp_map;
    private static Set<Integer>[][] wall_map;

    private static Direction AIRCON_DIR = new Direction(2, 3, 4, 5);

    private static Direction WALL_DIR = new Direction(1, 0, 3, 2);

    private static int[] dr = new int[]{-1, 0, 1, 0};
    private static int[] dc = new int[]{0, -1, 0, 1};
    private static class Direction{
        private int LEFT;
        private int UP;
        private int RIGHT;
        private int DOWN;
        Direction(int LEFT, int UP, int RIGHT, int DOWN){
            this.LEFT = LEFT;
            this.UP = UP;
            this.RIGHT = RIGHT;
            this.DOWN = DOWN;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        // 격자판 
        map = new int[N][N];
        temp_map = new int[N][N];
        wall_map = new Set[N][N];
        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());

            wall_map[i] = new Set[N];
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
                wall_map[i][j] = new HashSet<>();
            }
        }
        
        // 벽 정보
        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(stk.nextToken()) - 1;
            int y = Integer.parseInt(stk.nextToken()) - 1;
            int s = Integer.parseInt(stk.nextToken()); // 위, 왼

            wall_map[x][y].add(s);
            if(s == WALL_DIR.UP){ // 위
                wall_map[x-1][y].add(WALL_DIR.DOWN);
            }else if(s == WALL_DIR.LEFT){ // 왼
                wall_map[x][y-1].add(WALL_DIR.RIGHT);
            }
        }

        int time = -1;
        for(int minute = 1; time == -1 && minute <= 100; minute++){
            // 에어컨 공기 전파
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(map[i][j] >= 2){
                        int d = map[i][j] == AIRCON_DIR.LEFT ? WALL_DIR.LEFT
                            : map[i][j] == AIRCON_DIR.UP ? WALL_DIR.UP
                            : map[i][j] == AIRCON_DIR.RIGHT ? WALL_DIR.RIGHT
                            : WALL_DIR.DOWN;

                        boolean[][] visit = new boolean[N][N];
                        Queue<int[]> q = new ArrayDeque<>();
                        q.add(new int[]{i + dr[d], j + dc[d], 5});
                        while(!q.isEmpty()){
                            int[] info = q.poll();
                            int r = info[0];
                            int c = info[1];
                            int air = info[2];

                            temp_map[r][c] += air;

                            if(air == 1) continue;

                            int nr = r + dr[d];
                            int nc = c + dc[d];
                            if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;

                            if(!wall_map[r][c].contains(d) && !visit[nr][nc]){
                                visit[nr][nc] = true;
                                q.add(new int[]{nr, nc, air - 1});
                            }

                            if(d % 2 == 0){
                                // 오, 왼 체크
                                if(!wall_map[r][c].contains(WALL_DIR.LEFT) && c - 1 >= 0 && !wall_map[r][c - 1].contains(d)){
                                    if(!visit[nr][nc - 1]){
                                        visit[nr][nc - 1] = true;
                                        q.add(new int[]{nr, nc - 1, air - 1});
                                    }
                                }

                                if(!wall_map[r][c].contains(WALL_DIR.RIGHT) && c + 1 < N && !wall_map[r][c + 1].contains(d)){
                                    if(!visit[nr][nc + 1]){
                                        visit[nr][nc + 1] = true;
                                        q.add(new int[]{nr, nc + 1, air - 1});
                                    }
                                }
                            }else{ 
                                // 위, 아래 체크
                                if(!wall_map[r][c].contains(WALL_DIR.UP) && r - 1 >= 0 && !wall_map[r - 1][c].contains(d)){
                                    if(!visit[nr - 1][nc]){
                                        visit[nr - 1][nc] = true;
                                        q.add(new int[]{nr - 1, nc, air - 1});
                                    }
                                }
                                
                                if(!wall_map[r][c].contains(WALL_DIR.DOWN) && r + 1 < N && !wall_map[r + 1][c].contains(d)){
                                    if(!visit[nr + 1][nc]){
                                        visit[nr + 1][nc] = true;
                                        q.add(new int[]{nr + 1, nc, air - 1});
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            // 공기 섞임
            boolean[][] visit = new boolean[N][N];
            int[][] temp_map_diff = new int[N][N];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    visit[i][j] = true;

                    int hereT = temp_map[i][j];
                    for(int k = 0; k < 4; k++){
                        if(wall_map[i][j].contains(k)) continue;
                        int nr = i + dr[k];
                        int nc = j + dc[k];
                        if(nr < 0 || nr >= N || nc < 0 || nc >= N || visit[nr][nc]) continue;
                        int thereT = temp_map[nr][nc];

                        int value = Math.abs(hereT - thereT) / 4;
                        if(hereT == thereT) continue;
                        else if(hereT > thereT){
                            temp_map_diff[i][j] -= value;
                            temp_map_diff[nr][nc] += value;
                        }else{
                            temp_map_diff[i][j] += value;
                            temp_map_diff[nr][nc] -= value;
                        }
                    }
                }
            }

            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    temp_map[i][j] += temp_map_diff[i][j];
                }
            }

            // 외벽 시원함 감소
            for(int i = 0; i < N; i++){
                if(temp_map[0][i] > 0) temp_map[0][i]--;
                if(i != 0 && i != N - 1 && temp_map[i][0] > 0) temp_map[i][0]--;
                if(temp_map[N - 1][i] > 0) temp_map[N - 1][i]--;
                if(i != 0 && i != N - 1 && temp_map[i][N - 1] > 0) temp_map[i][N - 1]--;
            }

            if(checkWorkspace()) time = minute;
        }

        System.out.println(time);
    }

    private static boolean checkWorkspace(){
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(map[i][j] == 1 && temp_map[i][j] < K){
                    return false; 
                }
            }
        }
        return true;
    }
}