import java.util.*;
import java.io.*;

public class Main {
    private static int L, N, Q;
    private static int[][] chess;
    
    private static Map<Integer, Knight> knights = new HashMap<>();

    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};

    private static class Knight{
        int idx, r, c, h, w, k, dmg;
        Knight(int idx, int r, int c, int h, int w, int k, int dmg){
            this.idx = idx;
            this.r = r;
            this.c = c;
            this.h = h;
            this.w = w;
            this.k = k;
            this.dmg = dmg;
        }

        public int check_area(){
            int trap = 0;
            for(int i = r; i <= L && i < r + h; i++){
                for(int j = c; j <= L && j < c + w; j++){
                    if(chess[i][j] == 1) trap++;
                    else if(chess[i][j] == 2) return -1;
                }
            }
            return trap;
        }

        public boolean health_check(){
            return this.k > this.dmg;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        L = Integer.parseInt(stk.nextToken());
        N = Integer.parseInt(stk.nextToken());
        Q = Integer.parseInt(stk.nextToken());

        // 0 빈칸
        // 1 함정
        // 2 벽
        chess = new int[L + 1][L + 1];
        for(int i = 1; i <= L; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 1; j <= L; j++){
                chess[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        for(int i = 1; i <= N; i++){
            stk = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(stk.nextToken());
            int c = Integer.parseInt(stk.nextToken());
            int h = Integer.parseInt(stk.nextToken());
            int w = Integer.parseInt(stk.nextToken());
            int k = Integer.parseInt(stk.nextToken());

            Knight knight = new Knight(i,r,c,h,w,k,0);
            knights.put(i, knight);
        }

        while(Q-- > 0){
            stk = new StringTokenizer(br.readLine());
            int idx = Integer.parseInt(stk.nextToken());
            int d = Integer.parseInt(stk.nextToken());

            if(!knights.containsKey(idx)) continue;
            Knight target = knights.get(idx);

            // 기사 이동
            Queue<Knight> q = new ArrayDeque<>();
            q.add(target);

            Map<Integer, Knight> tmp_map = new HashMap<>();
            boolean[] visit = new boolean[N + 1];
            visit[idx] = true;
            while(!q.isEmpty()){
                Knight knight = q.poll();

                int nr = knight.r + dr[d];
                int nc = knight.c + dc[d];
                Knight moved_knight = new Knight(knight.idx, nr, nc, knight.h, knight.w, knight.k,knight.dmg);

                if(nr <= 0 || nr > L || nc <= 0 || nc > L || moved_knight.check_area() == -1){
                    tmp_map.clear();
                    break;
                }

                tmp_map.put(moved_knight.idx, moved_knight);
                for(Integer key : knights.keySet()){
                    if(visit[key]) continue;
                    Knight knight2 = knights.get(key);

                    if(collapse(moved_knight, knight2)){
                        visit[key] = true;
                        q.add(knight2);
                    }
                }
            }

            // 대결 데미지
            for(Integer key : tmp_map.keySet()){
                Knight knight = tmp_map.get(key);
                int damage = knight.check_area();

                if(key != idx){
                    knight.dmg += damage;
                    if(!knight.health_check()){
                        knights.remove(key);
                        continue;
                    }
                }

                knights.put(key, knight);
            }
        }

        
        int total_damage = 0;
        for(Integer key : knights.keySet()){
            total_damage += knights.get(key).dmg;
        }

        System.out.println(total_damage);
    }

    private static boolean collapse(Knight a, Knight b){
        int b_from_r = b.r;
        int b_from_c = b.c;
        int b_to_r = b.r + b.h >= L ? L : b.r + b.h;
        int b_to_c = b.c + b.w >= L ? L : b.c + b.w; 

        for(int i = a.r; i <= L && i < a.r + a.h; i++){
            for(int j = a.c; j <= L && j < a.c + a.w; j++){
                if(b_from_r <= i && i < b_to_r && b_from_c <= j && j < b_to_c) 
                    return true;
            }
        }
        return false;
    }
}


/*

*/