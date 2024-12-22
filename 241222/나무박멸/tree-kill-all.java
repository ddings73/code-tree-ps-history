import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K, C;

    private static int[][] map, toxic;
    private static Queue<Tree> q = new ArrayDeque<>();
    private static int[] dr = {-1, 1, 0, 0, -1, -1, 1, 1};
    private static int[] dc = {0, 0, 1, -1, -1, 1, -1, 1};

    private static class Tree{
        int r, c;
        Tree(int r, int c){
            this.r = r;
            this.c = c;
        }

        public int areaCheck(){
            int sum = map[r][c];
            for(int i = 4; i < 8; i++){
                int nr = r + dr[i];
                int nc = c + dc[i];

                int limit = K;
                while(limit-- > 0 && 0 <= nr && nr < N && 0 <= nc && nc < N){
                    if(map[nr][nc] >= 0) sum += map[nr][nc]; 
                    if(map[nr][nc] == -1 || map[nr][nc] == 0) break;
                    nr = nr + dr[i];
                    nc = nc + dc[i];
                }
            }
            return sum;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());
        C = Integer.parseInt(stk.nextToken());

        map = new int[N][N];
        toxic = new int[N][N];
        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
                if(map[i][j] > 0){
                    q.add(new Tree(i, j));
                }
            }
        }

        int kill_count = 0;
        while(M-- > 0){
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(toxic[i][j] > 0) toxic[i][j]--;
                }
            }

            Queue<Tree> sub = new ArrayDeque<>();
            while(!q.isEmpty()){
                Tree tree = q.poll();
                if(toxic[tree.r][tree.c] > 0){
                    map[tree.r][tree.c] = 0;
                    continue;
                }
                sub.add(tree);
            }
            q = new ArrayDeque<>(sub);

            // step1
            for(Tree tree : q){
                for(int i = 0; i < 4; i++){
                    int nr = tree.r + dr[i];
                    int nc = tree.c + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(map[nr][nc] <= 0 || toxic[nr][nc] > 0) continue;
                    map[tree.r][tree.c]++;
                }
            }


            // step2
            boolean[][] visit = new boolean[N][N];
            int[][] newMap = new int[N][N];
            sub = new ArrayDeque<>();
            while(!q.isEmpty()){
                Tree tree = q.poll();

                Queue<Tree> tmpQ = new ArrayDeque<>();
                int count = 0;
                for(int i = 0; i < 4; i++){
                    int nr = tree.r + dr[i];
                    int nc = tree.c + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(map[nr][nc] != 0 || toxic[nr][nc] > 0) continue;
                    count++;
                    Tree newTree = new Tree(nr, nc);
                    tmpQ.add(newTree);
                }

                while(!tmpQ.isEmpty()){
                    Tree newTree = tmpQ.poll();
                    if((map[tree.r][tree.c] / count) > 0 && !visit[newTree.r][newTree.c]){
                        visit[newTree.r][newTree.c] = true;
                        sub.add(newTree);
                    }
                    newMap[newTree.r][newTree.c] += (map[tree.r][tree.c] / count);
                }

                sub.add(tree);
            }

            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    map[i][j] += newMap[i][j];
                }
            }

            q = new ArrayDeque<>(sub);

            // step3
            int max_kill = 0;
            int r = 0, c = 0;
            for(Tree tree : q){
                int kill = tree.areaCheck();
                if(max_kill < kill || (max_kill == kill && (tree.r < r || (tree.r == r && tree.c < c)))){
                    max_kill = kill;
                    r = tree.r;
                    c = tree.c;
                }
            }

            kill_count += max_kill;
            toxic[r][c] = C + 1;
            for(int i = 4; i < 8; i++){
                int nr = r + dr[i];
                int nc = c + dc[i];

                int limit = K;
                while(limit-- > 0 && 0 <= nr && nr < N && 0 <= nc && nc < N){
                    toxic[nr][nc] = C + 1;
                    if(map[nr][nc] == -1 || map[nr][nc] == 0) break;
                    nr = nr + dr[i];
                    nc = nc + dc[i];
                }
            }
        }

        System.out.println(kill_count);
    }
}


/*
0 0 0 0 5
0 5 5 -1 5
0 -1 0 0 0
0 0 0 -1 0
-1 0 2 0 2

0 3 3 0 6
3 6 0 -1 0
0 -1 3 0 6
0 0 0 -1 1
-1 0 2 1 2
18

~ 5 ~ ~ 6
4 ~ ~ -1 ~
~ -1 ~ 9 9
0 5 3 -1 5
-1 2 6 5 6
24
*/