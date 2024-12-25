import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, H, K;

    private static boolean[][] tree;
    private static Queue<People> runners = new ArrayDeque<>();
    private static People tagger;

    private static boolean taggerDir = true;
    private static int[] taggerMoveCnt;
    private static int mvCnt = 0, taggerMv = 1, taggerMvLimit = 1;

    // 좌 우 하 상
    private static int[] dr = {0, 0, 1, -1};
    private static int[] dc = {-1, 1, 0, 0};
    
    private static class People{
        int r, c, d;
        People(int r, int c, int d){
            this.r = r;
            this.c = c;
            this.d = d;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        H = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        taggerMoveCnt = new int[N + 1];
        Arrays.fill(taggerMoveCnt, 2);
        taggerMoveCnt[N - 1] = 3;

        tagger = new People(N/2 + 1, N/2 + 1, 3);
        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(stk.nextToken());
            int c = Integer.parseInt(stk.nextToken());
            int d = Integer.parseInt(stk.nextToken());
            People people = new People(r, c, d);
            runners.add(people);
        }

        tree = new boolean[N + 1][N + 1];
        for(int i = 0; i < H; i++){
            stk = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(stk.nextToken());
            int c = Integer.parseInt(stk.nextToken());
            tree[r][c] = true;
        }

        int point = 0;
        for(int round = 1; round <= K; round++){
            // step1
            Queue<People> tmp = new ArrayDeque<>();
            while(!runners.isEmpty()){
                People runner = runners.poll();
                if(distCheck(runner, tagger)){
                    tmp.add(runner);
                    continue;
                }

                int d = runner.d;
                int nr = runner.r + dr[d];
                int nc = runner.c + dc[d];

                if(nr <= 0 || nr > N || nc <= 0 || nc > N){
                    if(d <= 1) d = d == 1 ? 0 : 1;
                    if(d >= 2) d = d == 2 ? 3 : 2;

                    nr = runner.r + dr[d];
                    nc = runner.c + dc[d];
                    runner.d = d;
                }

                if(nr != tagger.r || nc != tagger.c){
                    runner.r = nr;
                    runner.c = nc;
                }
                tmp.add(runner);
            }

            // step2
            // 술래 이동 및 방향 관리
            tagger.r = tagger.r + dr[tagger.d];
            tagger.c = tagger.c + dc[tagger.d];

            if(tagger.r == tagger.c && tagger.r == 1){ // 왼쪽 끝
                tagger.d = 2;
                taggerDir = !taggerDir;
                taggerMv = N - 1;
                taggerMvLimit = N - 1;
                mvCnt = 0;
            }else if(tagger.r == tagger.c && tagger.r == (N/2) + 1){ // 중앙
                tagger.d = 3;
                taggerDir = !taggerDir;
                taggerMv = 1;
                taggerMvLimit = 1;
                mvCnt = 0;
            }else{
                taggerMvLimit--;
                if(taggerMvLimit == 0){
                    mvCnt++;
                    if(taggerDir){
                        if(taggerMoveCnt[taggerMv] == mvCnt){
                            taggerMv++;
                            mvCnt = 0;
                        }
                        tagger.d = tagger.d == 3 ? 1
                            : tagger.d == 1 ? 2
                            : tagger.d == 2 ? 0
                            : 3;
                    }else{
                        if(taggerMoveCnt[taggerMv] == mvCnt){
                            taggerMv--;
                            mvCnt = 0;
                        }
                        tagger.d = tagger.d == 2 ? 1
                            : tagger.d == 1 ? 3
                            : tagger.d == 3 ? 0
                            : 2;
                    }
                    taggerMvLimit = taggerMv;
                }
            }
            

            // 술래 시야확인
            boolean[][] dead = new boolean[N + 1][N + 1];
            for(int i = 0; i < 3; i++){
                int nr = tagger.r + (i*dr[tagger.d]);
                int nc = tagger.c + (i*dc[tagger.d]);
                if(nr <= 0 || nr >  N || nc <= 0 || nc > N) break;
                if(tree[nr][nc]) continue;
                dead[nr][nc] = true;
            }

            int count = 0;
            while(!tmp.isEmpty()){
                People runner = tmp.poll();
                if(dead[runner.r][runner.c]){
                    count++;
                    continue;
                }
                runners.add(runner);
            }

            point += (round * count);
        }

        System.out.println(point);
    }

    private static boolean distCheck(People p1, People p2){
        return Math.abs(p1.r - p2.r) + Math.abs(p1.c - p2.c) > 3;
    }
}