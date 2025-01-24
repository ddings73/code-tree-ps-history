import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, charge;
    private static int[][] map;

    private static Map<Integer, int[]> info = new HashMap<>();

    private static int[] dr = {1, -1, 0, 0};
    private static int[] dc = {0, 0, 1, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        charge = Integer.parseInt(stk.nextToken());

        map = new int[N + 1][N + 1];
        for(int i = 1; i <= N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 1; j <= N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        stk = new StringTokenizer(br.readLine());
        int pX = Integer.parseInt(stk.nextToken());
        int pY = Integer.parseInt(stk.nextToken());

        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int x_s = Integer.parseInt(stk.nextToken());
            int y_s = Integer.parseInt(stk.nextToken());
            int x_e = Integer.parseInt(stk.nextToken());
            int y_e = Integer.parseInt(stk.nextToken());
            info.put(i, new int[]{x_s, y_s, x_e, y_e});
        }

        while(charge > 0){
            PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
                Integer task = Integer.compare(o1[1], o2[1]);
                if(task == 0){
                    task = Integer.compare(o1[2], o2[2]);
                    return task == 0 ? Integer.compare(o1[3], o2[3]) : task;
                }
                return task;
            });

            for(int key : info.keySet()){
                int[] value = info.get(key);

                int dist = getDist(pX, pY, value[0], value[1]);
                if(dist == -1) continue;
                pq.add(new int[]{key, dist, value[0], value[1]});
            }
            if(pq.isEmpty()) break;
            int[] target = pq.poll();
            charge -= target[1];

            int key = target[0];
            int[] value = info.get(key);

            int dist = getDist(value[0], value[1], value[2], value[3]);
            if(dist == -1 || dist > charge){
                charge = 0;
                break;
            }

            charge += dist;
            info.remove(key);

            pX = value[2];
            pY = value[3];
        }

        charge = info.size() > 0 ? -1 : charge;
        System.out.println(charge);
    }

    private static int getDist(int pX, int pY, int tX, int tY){
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
            return Integer.compare(o1[0], o2[1]);
        });

        int[][] visit = new int[N + 1][N + 1];
        for(int i = 1; i <= N; i++){
            for(int j = 1; j <= N; j++){
                visit[i][j] = N*N;
            }
        }

        visit[pX][pY] = 0;
        pq.add(new int[]{0, pX, pY});
        while(!pq.isEmpty()){
            int[] now = pq.poll();
            int x = now[1], y = now[2];

            if(x == tX && y == tY) return now[0];

            for(int i = 0; i < 4; i++){
                int nX = x + dr[i];
                int nY = y + dc[i];
                if(nX < 1 || nX > N || nY < 1 || nY > N) continue;
                if(visit[nX][nY] <= now[0] || map[nX][nY] == 1) continue;
                visit[nX][nY] = now[0] + 1;
                pq.add(new int[]{now[0] + 1, nX, nY});    
            }
        }

        return -1;
    }
}