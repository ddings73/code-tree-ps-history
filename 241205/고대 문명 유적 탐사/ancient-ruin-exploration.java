import java.util.*;
import java.io.*;

public class Main {
    private static int K, M;
    private static int[][] map = new int[5][5];

    private static Queue<Integer> Q = new ArrayDeque<>();

    private static int[] dr = {0, 0, -1, 1};
    private static int[] dc = {-1, 1, 0, 0};

    private static class Info{
        int r, c, angle;
        int[][] map;
        Info2 info2;

        Info(int r, int c, int angle, int[][] map, Info2 info2){
            this.r = r;
            this.c = c;
            this.angle = angle;
            this.map = map;
            this.info2 = info2;
        }
    }

    private static class Info2{
        int cost;
        PriorityQueue<int[]> pq;

        Info2(int cost, PriorityQueue<int[]> pq){
            this.cost = cost;
            this.pq = pq;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        K = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());

        for(int i = 0; i < 5; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < 5; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        stk = new StringTokenizer(br.readLine());
        for(int i = 0; i < M; i++){
            Q.add(Integer.parseInt(stk.nextToken()));
        }

        StringBuilder sb = new StringBuilder();

        while(K-- > 0){
            int total_cost = 0;

            PriorityQueue<Info> pq = new PriorityQueue<>((o1, o2)->{
                if(o1.info2.cost == o2.info2.cost){
                    if(o1.angle == o2.angle){
                        if(o1.c == o2.c) return Integer.compare(o1.r, o2.r);
                        return Integer.compare(o1.c, o2.c);
                    }
                    return Integer.compare(o1.angle, o2.angle);
                }
                return Integer.compare(o2.info2.cost, o1.info2.cost);
            });

            for(int i = 1; i <= 3; i++){
                for(int j = 1; j <= 3; j++){
                    for(int k = 1; k <= 3; k++){ // 90 -> 180 -> 270
                        int[][] rotatedMap = rotate(i, j, k);
                        Info2 info2 = calculateCost(rotatedMap);
                        Info info = new Info(i, j, k, rotatedMap, info2);
                        pq.add(info);
                    }
                }
            }

            Info target = pq.poll();
            map = target.map;

            Info2 info2 = target.info2;
            while(info2.cost > 0){
                total_cost += info2.cost;
                
                while(!info2.pq.isEmpty()){
                    int[] pos = info2.pq.poll();
                    int num = Q.poll();
                    map[pos[0]][pos[1]] = num;
                }

                info2 = calculateCost(map);
            }

            if(total_cost == 0) break;
            sb.append(total_cost).append(" ");
        }

        if(sb.length() != 0) sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb.toString());
    }

    private static int[][] rotate(int r, int c, int k){
        int[][] clone = new int[5][];
        for(int i = 0; i < 5; i++) clone[i] = map[i].clone();

        List<int[]> list = new ArrayList<>();
        ArrayDeque<Integer> dq = new ArrayDeque<>();
        for(int i = -1; i <= 1; i++){ 
            list.add(new int[]{r - 1, c + i});
            dq.addLast(clone[r - 1][c + i]);
        }
        list.add(new int[]{r, c + 1});
        dq.addLast(clone[r][c + 1]);
        for(int i = 1; i >= -1; i--){
            list.add(new int[]{r + 1, c + i});
            dq.addLast(clone[r + 1][c + i]);
        }
        list.add(new int[]{r, c - 1});
        dq.addLast(clone[r][c - 1]);

        int move_cnt = k * 2;
        while(move_cnt-- > 0){
            dq.addFirst(dq.pollLast());
        }

        for(int[] pos : list){
            clone[pos[0]][pos[1]] = dq.pollFirst();
        }

        return clone;
    }

    private static Info2 calculateCost(int[][] map){
        int cost = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
            if(o1[1] == o2[1]) return Integer.compare(o2[0], o1[0]);
            return Integer.compare(o1[1], o2[1]);
        });

        boolean[][] visit = new boolean[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                if(visit[i][j]) continue;
                visit[i][j] = true;
                
                int num = map[i][j];
                int select = 1;

                List<int[]> list = new ArrayList<>();
                Queue<int[]> q = new ArrayDeque<>();
                
                list.add(new int[]{i, j});
                q.add(new int[]{i, j});
                while(!q.isEmpty()){
                    int[] idx = q.poll();
                    for(int k = 0; k < 4; k++){
                        int nr = idx[0] + dr[k];
                        int nc = idx[1] + dc[k];
                        if(nr < 0 || nr >= 5 || nc < 0 || nc >= 5 || visit[nr][nc] || map[nr][nc] != num) continue;
                        visit[nr][nc] = true;
                        select++;

                        list.add(new int[]{nr, nc});
                        q.add(new int[]{nr, nc});
                    }
                }

                if(select < 3) continue;
                cost += select;

                for(int[] pos : list) pq.add(pos);
            }
        }

        return new Info2(cost, pq);
    }
}