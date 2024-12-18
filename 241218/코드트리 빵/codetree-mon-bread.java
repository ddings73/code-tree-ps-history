import java.util.*;
import java.io.*;

public class Main {
    private static int N, M;
    private static int campIdx;
    private static int[][] map, dest;
    private static Map<Integer, int[]> baseCamp = new HashMap<>();

    private static int[] dr = {-1, 0, 0, 1};
    private static int[] dc = {0, -1, 1, 0};
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
                if(map[i][j] == 1){
                    baseCamp.put(campIdx++, new int[]{i, j});
                }
            }
        }

        dest = new int[M][2];
        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(stk.nextToken()) - 1;
            int y = Integer.parseInt(stk.nextToken()) - 1;

            dest[i][0] = x;
            dest[i][1] = y;
        }

        int time = 0;
        Map<Integer, int[]> people = new HashMap<>();
        while(time <= N*N){
            if(time < M){ // 인원 투입
                int[] dest_pos = dest[time];
                
                PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
                    int task1 = Integer.compare(o1[2], o2[2]);
                    if(task1 == 0){
                        int task2 = Integer.compare(o1[0], o2[0]);
                        return task2 == 0 ? Integer.compare(o1[1], o2[1]) : task2;
                    }
                    return task1;
                });

                for(int key = 0; key < campIdx; key++){
                    int[] camp = baseCamp.get(key);
                    if(map[camp[0]][camp[1]] == -1) continue;
                    int[][][] dist_map = getDistance(camp, dest_pos);
                    pq.add(new int[]{camp[0], camp[1], dist_map[dest_pos[0]][dest_pos[1]][0]});
                }
                int[] camp_info = pq.poll();
                map[camp_info[0]][camp_info[1]] = -1;
                people.put(time, new int[]{camp_info[0], camp_info[1]});
            }

            if(time >= M && people.isEmpty()) break; 

            for(Integer key : people.keySet()){
                if(key == time) continue;
                int[] dest_pos = dest[key];
                int[] pos = people.get(key);
                int[][][] dist_map = getDistance(pos, dest_pos);

                int r = dest_pos[0];
                int c = dest_pos[1];
                while(dist_map[r][c][0] > 0){
                    if(dist_map[r][c][0] == 1){
                        pos[0] = r;
                        pos[1] = c;
                    }

                    int nr = dist_map[r][c][1];
                    int nc = dist_map[r][c][2];
                    r = nr;
                    c = nc;
                }

                people.put(key, pos);
            }

            Queue<Integer> rmKeys = new ArrayDeque<>();
            for(Integer key : people.keySet()){
                int[] dest_pos = dest[key];
                int[] pos = people.get(key);

                if(pos[0] == dest_pos[0] && pos[1] == dest_pos[1]){
                    map[pos[0]][pos[1]] = -1;
                    rmKeys.add(key);
                }
            }

            for(Integer key : rmKeys){
                people.remove(key);
            }

            time++;
        }

        System.out.println(time);
    }

    private static int[][][] getDistance(int[] from, int[] to){
        int[][][] visit = new int[N][N][3];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                Arrays.fill(visit[i][j], -1);
            }
        }

        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{from[0], from[1], 0});
        visit[from[0]][from[1]][0] = 0;

        while(!q.isEmpty()){
            int[] info = q.poll();
            int r = info[0];
            int c = info[1];
            int cnt = info[2];
            if(r == to[0] && c == to[1]) break;
            for(int i = 0; i < 4; i++){
                int nr = r + dr[i];
                int nc = c + dc[i];
                if(nr < 0 || nr >= N || nc < 0 || nc >= N || map[nr][nc] < 0) continue;
                if(visit[nr][nc][0] != -1 && visit[nr][nc][0] <= cnt + 1) continue;
                visit[nr][nc][0] = cnt + 1;
                visit[nr][nc][1] = r;
                visit[nr][nc][2] = c;
                q.add(new int[]{nr, nc, cnt + 1});
            }
        }

        return visit;
    }
}