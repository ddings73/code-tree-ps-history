import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, K;
    private static int[][][] area;

    private static Player[] players;

    private static int[] dr = {0, -1, 1, 0, 0};
    private static int[] dc = {0, 0 ,0, -1, 1};

    private static class Player{
        int r, c, d;
        Map<Integer, int[]> dir_priority;

        private Player(int r, int c){
            this.r = r;
            this.c = c;
            this.d = 0;
            this.dir_priority = new HashMap<>();
        }

        private int[] getPriorityInfo(){
            return this.dir_priority.get(this.d);
        }

        private void update(int r, int c, int d){
            this.r = r;
            this.c = c;
            this.d = d;
        }
    }

    public static void main(String[] args) throws IOException {
        inputData();

        // 최대 1000라운드
        int round = 0;
        while(round++ < 1000){
            // 모든 플레이어 이동
            int[][] nxt_info = new int[M + 1][];
            for(int i = 1; i <= M; i++){
                if(players[i] == null) continue;
                int r = players[i].r;
                int c = players[i].c;

                int nR = -1, nC = -1, nD = 0;
                int[] priority_info = players[i].getPriorityInfo();
                for(int j = 0; j < 4; j++){
                    int d = priority_info[j];
                    int nr = r + dr[d];
                    int nc = c + dc[d];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(area[nr][nc][0] != 0 && area[nr][nc][0] != i) continue;
                    if((nR == -1 && nC == -1) || (area[nR][nC][0] == i && area[nr][nc][0] == 0) ){
                        nR = nr;
                        nC = nc;
                        nD = d;
                    }
                }

                nxt_info[i] = new int[]{nR, nC, nD};
            }

            // 독점계약 기간 감소
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(area[i][j][0] == 0) continue;
                    area[i][j][1]--;
                    
                    if(area[i][j][1] == 0) area[i][j][0] = 0;
                }
            }
            
            // 플레이어 이동 정보 갱신
            for(int i = 1; i <= M; i++){
                if(nxt_info[i] == null) continue;
                int r = nxt_info[i][0];
                int c = nxt_info[i][1];
                int d = nxt_info[i][2];

                if(area[r][c][0] != 0 && area[r][c][0] != i) players[i] = null;
                else{
                    area[r][c][0] = i;
                    area[r][c][1] = K;

                    players[i].update(r, c, d);
                }
            }

            // 생존 플레이어 확인
            int living_count = 0;
            for(int i = 1; i <= M; i++){
                if(players[i] == null) continue;
                living_count++;
            }
            if(living_count == 1) break;
        }

        if(round >= 1000) round = -1;
        System.out.println(round);

    }

    private static void inputData(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());

            N = Integer.parseInt(stk.nextToken()); // max 20
            M = Integer.parseInt(stk.nextToken()); // max 400
            K = Integer.parseInt(stk.nextToken()); // max 1000

            area = new int[N][N][2];
            players = new Player[M + 1];

            // 영역 정보
            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    area[i][j][0] = Integer.parseInt(stk.nextToken());

                    if(area[i][j][0] == 0) continue;
                    area[i][j][1] = K;

                    players[area[i][j][0]] = new Player(i, j);
                }
            }

            // 플레이어 초기 방향 정보
            stk = new StringTokenizer(br.readLine());
            for(int i = 1; i <= M; i++){
                players[i].d = Integer.parseInt(stk.nextToken());
            }

            // 플레이어 방향에 따른 우선순위
            for(int i = 1; i <= M; i++){
                for(int j = 1; j <= 4; j++){ // 위 아래 왼 오
                    stk = new StringTokenizer(br.readLine());
                    int[] priority_info = new int[4];
                    
                    for(int k = 0; k < 4; k++){ 
                        priority_info[k] = Integer.parseInt(stk.nextToken());
                    }

                    players[i].dir_priority.put(j, priority_info);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}