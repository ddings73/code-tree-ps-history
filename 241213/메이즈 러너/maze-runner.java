import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    private static int MOVE;

    private static Queue<Pos> q = new ArrayDeque<>();    
    private static Pos exit;
    private static int[][] map;
    
    private static int[] dr = {-1, 1, 0, 0};
    private static int[] dc = {0, 0, -1, 1};

    private static class Pos{
        int r, c, move;
        Pos(int r, int c, int move){
            this.r = r;
            this.c = c;
            this.move = move;
        }

        @Override
        public boolean equals(Object obj){
            if(obj instanceof Pos){
                Pos p = (Pos) obj;
                return this.r == p.r && this.c == p.c;
            }
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        map = new int[N + 1][N + 1];
        for(int i = 1; i <= N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 1; j <= N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(stk.nextToken());
            int c = Integer.parseInt(stk.nextToken());

            Pos person = new Pos(r, c, 0);
            q.add(person);
        }

        
        stk = new StringTokenizer(br.readLine());
        int r = Integer.parseInt(stk.nextToken());
        int c = Integer.parseInt(stk.nextToken());

        exit = new Pos(r, c, 0);
        map[r][c] = -1;

        while(K-- > 0){
            q = new ArrayDeque<>(step1());
            if(q.isEmpty()) break;

            // 회전 시작
            int[] info = getRectanglePos();

            int r1 = info[0];
            int c1 = info[1];

            int r2 = info[2];
            int c2 = info[3];
            int width = info[4];

            for(int i = r1; i <= r2; i++){
                for(int j = c1; j <= c2; j++){
                    if(map[i][j] <= 0) continue;
                    map[i][j]--;
                }
            }

            int[][] sub_arr = new int[width + 1][width + 1];
            for(int i = 0; i <= width; i++){
                for(int j = 0; j <= width; j++){
                    sub_arr[i][j] = map[r2 - j][c1 + i];
                }
            }

            for(int i = 0; i <= width; i++){
                for(int j = 0; j <= width; j++){
                    map[r1 + i][c1 + j] = sub_arr[i][j];
                    if(map[r1 + i][c1 + j] == -1){
                        exit.r = r1 + i;
                        exit.c = c1 + j;
                    }
                }
            }

            for(Pos p : q){
                boolean check = false;
                for(int i = 0; !check && i <= width; i++){
                    for(int j = 0; !check && j <= width; j++){
                        if(p.r == r2 - j && p.c == c1 + i){
                            p.r = r1 + i;
                            p.c = c1 + j;
                            check = true;
                        }
                    }
                }
            }

        }

        while(!q.isEmpty()){
            Pos p = q.poll();
            MOVE += p.move;
        }

        System.out.println(MOVE);
        System.out.println(exit.r + " " + exit.c);
    }

    private static Queue<Pos> step1(){
        Queue<Pos> sub_q = new ArrayDeque<>();
        while(!q.isEmpty()){
            Pos pos = q.poll();
            int dist1 = getDistance(pos, exit);
            
            PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
                int task1 = Integer.compare(o1[2], o2[2]);
                return task1 == 0 ? Integer.compare(o1[3], o2[3]) : task1;
            });

            for(int i = 0; i < 4; i++){
                int nr = pos.r + dr[i];
                int nc = pos.c + dc[i];
                if(nr <= 0 || nr > N || nc <= 0 || nc > N || map[nr][nc] > 0) continue;
                int dist2 = getDistance(new Pos(nr, nc, 0), exit);
                if(dist1 <= dist2) continue;                
                pq.add(new int[]{nr, nc, dist2, i});
            }
            
            if(pq.isEmpty()){
                sub_q.add(pos);
                continue;
            }

            int[] result = pq.poll();
            Pos new_pos = new Pos(result[0], result[1], pos.move + 1);

            if(exit.equals(new_pos)){
                MOVE += new_pos.move;
                continue;
            }

            sub_q.add(new_pos);
        }

        return sub_q;
    }

    private static int[] getRectanglePos() {
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> {
            int task1 = Integer.compare(o1[4], o2[4]);
            if(task1 == 0){
                int task2 = Integer.compare(o1[0], o2[0]);
                return task2 == 0 ? Integer.compare(o1[1], o2[1]) : task2;
            }
            return task1;
        });
        
        for(Pos p : q){
            for(int i = 1; i <= N; i++){
                for(int j = 1; j <= N; j++){
                    for(int k = 1; i + k <= N && j + k <= N; k++){
                        if(i <= exit.r && exit.r <= i + k && j <= exit.c && exit.c <= j + k && i <= p.r && p.r <= i + k && j <= p.c && p.c <= j + k){
                            pq.add(new int[]{i, j, i + k, j + k, k});
                            break;
                        }
                    }
                }
            }
        }

        return pq.poll();
    }

    private static int getDistance(Pos p1, Pos p2){
        return Math.abs(p1.r - p2.r) + Math.abs(p1.c - p2.c);
    }
}