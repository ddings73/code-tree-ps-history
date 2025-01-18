import java.util.*;
import java.io.*;

public class Main {
    private static int N, M;
    private static int[][] map;

    private static int[] dr = {1, -1, 0, 0};
    private static int[] dc = {0, 0, 1, -1};
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

        /*
        -1 : 검은색
        0 : 빨간 폭탄
        1 ~ m : 빨강이 아닌 폭탄
        */

        int point = 0;
        while(true){
            PriorityQueue<int[]> pq = bombInfo();
            if(pq.isEmpty()) break;
            int[] target = pq.poll();
            point += (target[3] * target[3]);

            int r = target[1];
            int c = target[2];

            int bomb = map[r][c];
            Queue<int[]> q = new ArrayDeque<>();
            q.add(new int[]{r, c});
            while(!q.isEmpty()){
                int[] info = q.poll();

                map[info[0]][info[1]] = -2;

                for(int i = 0; i < 4; i++){
                    int nr = info[0] + dr[i];
                    int nc = info[1] + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(map[nr][nc] != bomb && map[nr][nc] != 0) continue;
                    q.add(new int[]{nr, nc});
                }
            }
            
            gravity();

            // rotate
            int[][] newMap = new int[N][N];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    newMap[i][j] = map[j][N - 1 - i];
                }
            }

            map = newMap;

            gravity();
        }

        System.out.println(point);
    }

    private static PriorityQueue<int[]> bombInfo(){
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
            Integer task = Integer.compare(o2[3], o1[3]);
            if(task == 0){
                Integer task1 = Integer.compare(o1[0], o2[0]);
                if(task1 == 0){
                    Integer task2 = Integer.compare(o2[1], o1[1]);
                    return task2 == 0 ? Integer.compare(o1[2], o2[2]) : task2;
                }
                return task1;
            }
            return task;
        });

        boolean[][] visit = new boolean[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(map[i][j] <= 0 || visit[i][j]) continue;
                int[] info = new int[]{0, i, j, 0};
                
                int bomb = map[i][j];
                Queue<int[]> redBomb = new ArrayDeque<>();
                Queue<int[]> q = new ArrayDeque<>();
                visit[i][j] = true;
                q.add(new int[]{i, j});
                while(!q.isEmpty()){
                    int[] item = q.poll();
                    
                    info[3]++;
                    
                    if(map[item[0]][item[1]] == 0) info[0]++;
                    else if(item[0] > info[1] || (item[0] == info[1] && item[1] < info[2])){
                        info[1] = item[0];
                        info[2] = item[1];
                    }

                    for(int k = 0; k < 4; k++){
                        int nr = item[0] + dr[k];
                        int nc = item[1] + dc[k];
                        if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                        if(visit[nr][nc] || (map[nr][nc] != bomb && map[nr][nc] != 0)) continue;
                        visit[nr][nc] = true;
                        q.add(new int[]{nr, nc});

                        if(map[nr][nc] == 0) 
                            redBomb.add(new int[]{nr, nc});
                    }
                }

                while(!redBomb.isEmpty()){
                    int[] p = redBomb.poll();
                    visit[p[0]][p[1]] = false;
                }

                if(info[3] < 2) continue;
                pq.add(info);
            }
        }

        return pq;
    }

    private static void gravity(){
        for(int j = 0; j < N; j++){
            int bottom = -1;
            for(int i = N - 1; i >= 0; i--){
                if(map[i][j] == -1){
                     bottom = -1;
                     continue;
                }

                if(bottom == -1 && map[i][j] == -2) bottom = i;
                else if(bottom != -1 && map[i][j] >= 0){
                    while(i >= 0 && map[i][j] >= 0){
                        map[bottom][j] = map[i][j];
                        map[i][j] = -2;
                        i--; bottom--;
                    }
                    i++;
                }
            }
        }
    }
}