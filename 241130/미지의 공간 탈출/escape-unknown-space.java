import java.util.*;
import java.io.*;

public class Main {

    private static int N, M, F;
    private static int[][] map;
    private static int[][][] timeWall;

    private static boolean[][] visit;
    private static boolean[][][] twVisit;
    private static int[][][] twOutside;

    private static List<boolean[][]> anomalyMaps;
    
    private static int[] pos;
    private static int[] dr = {0, 0, 1, -1};
    private static int[] dc = {1, -1, 0, 0};
    
    private static Queue<Anomaly> anomalyQ = new ArrayDeque<>();
    private static class Anomaly{
        int r, c, d, u;
        boolean[][] visit;

        Anomaly(int r, int c, int d, int u, boolean[][] visit){
            this.r = r;
            this.c = c;
            this.d = d;
            this.u = u;
            this.visit = visit;
        }
    }

    private static class Info{
        int t, r, c, wd;

        Info(int t, int r, int c, int wd){
            this.t = t;
            this.r = r;
            this.c = c;
            this.wd = wd;
        }
    }

    /*
        0 : 빈공간
        1 : 장애물
        2 : 타임머신 위치
        3 : 시간의 벽
        4 : 탈출구
    */
    public static void main(String[] args) throws IOException {
        input();

        int result = -1;
        // 이상현상 확산
        anomalyInfoSetting();

        PriorityQueue<Info> pq = new PriorityQueue<>((o1, o2)->Integer.compare(o1.t, o2.t));
        visit = new boolean[N][N];
        twVisit = new boolean[5][M][M];

        twVisit[4][pos[0]][pos[1]] = true;
        pq.add(new Info(0, pos[0], pos[1], 4));
        while(!pq.isEmpty()){
            Info info = pq.poll();
            if(info.wd == -1 && map[info.r][info.c] == 4){
                result = info.t;
                break;
            }  

            boolean[][] anomalyMap = info.t >= anomalyMaps.size() 
                ? anomalyMaps.get(anomalyMaps.size() - 1)
                : anomalyMaps.get(info.t);

            // 시간의 벽위에 있는 경우
            if(info.wd >= 0){ // 동, 서, 남, 북, 윗
                for(int i = 0; i < 4; i++){
                    int nr = info.r + dr[i];
                    int nc = info.c + dc[i];
                    
                    if(0 <= nr && nr < M && 0 <= nc && nc < M){
                        if(twVisit[info.wd][nr][nc] || timeWall[info.wd][nr][nc] == 1) continue;
                        twVisit[info.wd][nr][nc] = true;
                        Info nxt = new Info(info.t + 1, nr, nc, info.wd);
                        pq.add(nxt);
                        continue;
                    }

                    if(info.wd == 0){ // 동
                        if(nr >= M && map[twOutside[info.wd][(M - 1 - info.c)][0]][twOutside[info.wd][(M - 1 - info.c)][1]] == 0){
                            nr = twOutside[info.wd][(M - 1 - info.c)][0];
                            nc = twOutside[info.wd][(M - 1 - info.c)][1];
                            if(anomalyMap[nr][nc] || visit[nr][nc]) continue;
                            visit[nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, -1);
                            pq.add(nxt);
                        }else if(nr < 0){ // 윗
                            nr = (M - 1) - info.c;
                            nc = M - 1;
                            if(twVisit[4][nr][nc] || timeWall[4][nr][nc] == 1) continue;
                            twVisit[4][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 4);
                            pq.add(nxt);
                        }else if(nc >= M){ // 북
                            nc = 0;
                            if(twVisit[3][nr][nc] || timeWall[3][nr][nc] == 1) continue;
                            twVisit[3][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 3);
                            pq.add(nxt);
                        }else if(nc < 0){ // 남
                            nc = M - 1;
                            if(twVisit[2][nr][nc] || timeWall[2][nr][nc] == 1) continue;
                            twVisit[2][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 2);
                            pq.add(nxt);
                        }
                    }else if(info.wd == 1){ // 서
                        if(nr >= M && map[twOutside[info.wd][info.c][0]][twOutside[info.wd][info.c][1]] == 0){
                            nr = twOutside[info.wd][info.c][0];
                            nc = twOutside[info.wd][info.c][1];
                            if(anomalyMap[nr][nc] || visit[nr][nc]) continue;
                            visit[nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, -1);
                            pq.add(nxt);
                        }else if(nr < 0){ // 윗
                            nr = info.c;
                            nc = 0;
                            if(twVisit[4][nr][nc] || timeWall[4][nr][nc] == 1) continue;
                            twVisit[4][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 4);
                            pq.add(nxt);
                        }else if(nc < 0){ // 북
                            nc = M - 1;
                            if(twVisit[3][nr][nc] || timeWall[3][nr][nc] == 1) continue;
                            twVisit[3][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 3);
                        }else if(nc >= M){ // 남
                            nc = 0;
                            if(twVisit[2][nr][nc] || timeWall[2][nr][nc] == 1) continue;
                            twVisit[2][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 2);
                            pq.add(nxt);
                        }
                    }else if(info.wd == 2){ // 남
                        if(nr >= M&& map[twOutside[info.wd][info.c][0]][twOutside[info.wd][info.c][1]] == 0){
                            nr = twOutside[info.wd][info.c][0];
                            nc = twOutside[info.wd][info.c][1];
                            if(anomalyMap[nr][nc] || visit[nr][nc]) continue;
                            visit[nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, -1);
                            pq.add(nxt);
                        }else if(nr < 0){ // 윗
                            nr = M - 1;
                            if(twVisit[4][nr][nc] || timeWall[4][nr][nc] == 1) continue;
                            twVisit[4][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 4);
                            pq.add(nxt);
                        }else if(nc >= M){ // 동
                            nc = 0;
                            if(twVisit[0][nr][nc] || timeWall[0][nr][nc] == 1) continue;
                            twVisit[0][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 0);
                            pq.add(nxt);
                        }else if(nc < 0){ // 서
                            nc = M - 1;
                            if(twVisit[1][nr][nc] || timeWall[1][nr][nc] == 1) continue;
                            twVisit[1][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 1);
                            pq.add(nxt);
                        }
                    }else if(info.wd == 3){ // 북
                        if(nr >= M && map[twOutside[info.wd][(M - 1 - info.c)][0]][twOutside[info.wd][(M - 1 - info.c)][1]] == 0){
                            nr = twOutside[info.wd][(M - 1 - info.c)][0];
                            nc = twOutside[info.wd][(M - 1 - info.c)][1];
                            if(anomalyMap[nr][nc] || visit[nr][nc]) continue;
                            visit[nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, -1);
                            pq.add(nxt);
                        }else if(nr < 0){ // 윗
                            nr = 0;
                            nc = (M - 1) - info.c;
                            if(twVisit[4][nr][nc] || timeWall[4][nr][nc] == 1) continue;
                            twVisit[4][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 4);
                            pq.add(nxt);
                        }else if(nc >= M){ // 서
                            nc = 0;
                            if(twVisit[1][nr][nc] || timeWall[1][nr][nc] == 1) continue;
                            twVisit[1][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 1);
                            pq.add(nxt);
                        }else if(nc < 0){ // 동
                            nc = M - 1;
                            if(twVisit[0][nr][nc] || timeWall[0][nr][nc] == 1) continue;
                            twVisit[0][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 0);
                            pq.add(nxt);
                        }
                    }else if(info.wd == 4){ // 윗
                        if(nr < 0){ // 북
                            nr = 0;
                            nc = (M - 1) - info.c;
                            if(twVisit[3][nr][nc] || timeWall[3][nr][nc] == 1) continue;
                            twVisit[3][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 3);
                            pq.add(nxt);
                        }else if(nr >= M){ // 남
                            nr = 0;
                            if(twVisit[2][nr][nc] || timeWall[2][nr][nc] == 1) continue;
                            twVisit[2][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 2);
                            pq.add(nxt);
                        }else if(nc < 0){ // 서
                            nc = nr;
                            nr = 0;
                            if(twVisit[1][nr][nc] || timeWall[1][nr][nc] == 1) continue;
                            twVisit[1][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 1);
                            pq.add(nxt);
                        }else if(nc >= M){ // 동
                            nc = (M - 1) - info.r;
                            nr = 0;
                            if(twVisit[0][nr][nc] || timeWall[0][nr][nc] == 1) continue;
                            twVisit[0][nr][nc] = true;
                            Info nxt = new Info(info.t + 1, nr, nc, 0);
                            pq.add(nxt);
                        }
                    }
                }
            }else{
                for(int i = 0; i < 4; i++){
                    int nr = info.r + dr[i];
                    int nc = info.c + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N || map[nr][nc] == 1 || map[nr][nc] == 3) continue;
                    if(anomalyMap[nr][nc] || visit[nr][nc]) continue;
                    visit[nr][nc] = true;
                    Info nxt = new Info(info.t + 1, nr, nc, info.wd);
                    pq.add(nxt);
                }
            }
        }
        System.out.println(result);
    }

    private static void anomalyInfoSetting(){
        int T = 0;
        while(!anomalyQ.isEmpty()){
            boolean[][] visit = new boolean[N][N];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    visit[i][j] = anomalyMaps.get(T)[i][j];
                }
            }

            int anomalyCnt = anomalyQ.size();
            while(anomalyCnt-- > 0){
                Anomaly anomaly = anomalyQ.poll();
                if(T % anomaly.u != 0) anomalyQ.add(anomaly);
                else{
                    int nr = anomaly.r + dr[anomaly.d];
                    int nc = anomaly.c + dc[anomaly.d];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(anomaly.visit[nr][nc] || map[nr][nc] != 0) continue;
                    anomaly.visit[nr][nc] = true;
                    visit[nr][nc] = true;

                    Anomaly nxt = new Anomaly(nr,nc,anomaly.d, anomaly.u, anomaly.visit);
                    anomalyQ.add(nxt);
                }
            }

            anomalyMaps.add(visit);
            T++;
        }
    }

    private static void input() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        F = Integer.parseInt(stk.nextToken());

        map = new int[N][N];
        twOutside = new int[4][M][2];

        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        int tN = 0, tS = 0, tW = 0, tE = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(map[i][j] == 3){
                    // 동쪽 우측
                    if(map[i][j + 1] != 3){
                        twOutside[0][tE][0] = i;
                        twOutside[0][tE][1] = j + 1;
                        tE++;
                    }

                    // 서쪽 좌측
                    if(map[i][j - 1] != 3){
                        twOutside[1][tW][0] = i;
                        twOutside[1][tW][1] = j - 1;
                        tW++;
                    }

                    // 남쪽 하단
                    if(map[i + 1][j] != 3){
                        twOutside[2][tS][0] = i + 1;
                        twOutside[2][tS][1] = j;
                        tS++;
                    }

                    // 북쪽 상단
                    if(map[i - 1][j] != 3){
                        twOutside[3][tN][0] = i - 1;
                        twOutside[3][tN][1] = j;
                        tN++;
                    }
                }
            }
        }
        
        // 동, 서, 남, 북, 윗
        timeWall = new int[5][M][M];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < M; j++){
                stk = new StringTokenizer(br.readLine());
                for(int k = 0; k < M; k++){
                    timeWall[i][j][k] = Integer.parseInt(stk.nextToken());
                    if(i == 4 && timeWall[i][j][k] == 2){ 
                        pos = new int[]{j, k};
                    }
                }
            }
        }

        // 시간 이상현상 정보
        anomalyMaps = new ArrayList<>();
        boolean[][] initVisit = new boolean[N][N];
        for(int i = 0; i < F; i++){
            stk = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(stk.nextToken());
            int c = Integer.parseInt(stk.nextToken());
            int d = Integer.parseInt(stk.nextToken());
            int u = Integer.parseInt(stk.nextToken());

            boolean[][] visit = new boolean[N][N];
            visit[r][c] = true;
            
            initVisit[r][c] = true;

            Anomaly anomaly = new Anomaly(r, c, d, u, visit);
            anomalyQ.add(anomaly);
        }
        anomalyMaps.add(initVisit);
    }
}