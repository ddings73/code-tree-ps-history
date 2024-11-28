import java.util.*;
import java.io.*;

public class Main {
    private static int N, M, P, C, D;
    private static int[] rudolph;

    private static int[][] santas;
    private static int[] santaPoints;
    private static int[] santaStatus;
    private static int[][][] santaMap;

    private static int[] stunCnt;

    private static final int ARRIVE = 0;
    private static final int STUN = 1;
    private static final int OUT = 2;
    

    // 상 우 하 좌 좌상 우상 좌하 우하
    private static int[] dr = {-1, 0, 1, 0, -1, -1, 1, 1};
    private static int[] dc = {0, 1, 0, -1, -1, 1, -1, 1};

    private static Map<Integer, Integer> dirPair = new HashMap<>();

    public static void main(String[] args) throws IOException {
        input();        

        while(M-- > 0){
            for(int i = 0; i < P; i++){
                if(stunCnt[i] == 0) continue;
                stunCnt[i]--;
                if(stunCnt[i] == 0) 
                    santaStatus[i] = ARRIVE;
            }

            // 루돌프 움직임
            rudolphMove();
            if(!checkSanta(false)) break;

            // 산타 움직임
            santaMove();
            if(!checkSanta(true)) break;
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < P; i++){
            sb.append(santaPoints[i]);
            if(i + 1 < P) sb.append(" ");
            
        }

        System.out.println(sb.toString());
    }

    private static void rudolphMove(){
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
            if(o1[0] == o2[0]){
                if(o1[1] == o2[1]) return o2[2]-o1[2];
                return o2[1]-o1[1];
            }
            return o1[0]-o2[0];
        });

        for(int i = 0; i < P; i++){
            if(santaStatus[i] == OUT) continue;
            int dist = getDistance(rudolph, santas[i]);
            pq.add(new int[]{dist, santas[i][0], santas[i][1]});
        }
        int[] target = pq.poll();

        pq = new PriorityQueue<>((o1, o2)->o1[0]-o2[0]);
        for(int i = 0; i < 8; i++){
            int nr = rudolph[0] + dr[i];
            int nc = rudolph[1] + dc[i];
            if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
            int dist = getDistance(new int[]{nr, nc}, new int[]{target[1], target[2]});
            pq.add(new int[]{dist, i, nr, nc});
        }

        int[] move = pq.poll();
        rudolph[0] = move[2];
        rudolph[1] = move[3];

        // 충돌 및 상호작용
        if(santaMap[rudolph[0]][rudolph[1]][0] > 0){
            int seq = santaMap[rudolph[0]][rudolph[1]][1];
            santaPoints[seq] += C;

            int dir = move[1];

            int r = santas[seq][0] + (C * dr[dir]);
            int c = santas[seq][1] + (C * dc[dir]);
            
            santaStatus[seq] = STUN;
            stunCnt[seq] = 2;

            santaMap[rudolph[0]][rudolph[1]][0] = -1;
            santaMap[rudolph[0]][rudolph[1]][1] = -1;
            if(r < 0 || r >= N || c < 0 || c >= N){
                santaStatus[seq] = OUT;
                stunCnt[seq] = 0;
            }else{
                interact(r, c, dir, seq);
            }
        }
    }

    private static void santaMove(){
        for(int i = 0; i < P; i++){
            if(santaStatus[i] == STUN || santaStatus[i] == OUT) continue;
            int originDist = getDistance(santas[i], rudolph);

            PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)-> {
                if(o1[0] == o2[0]) return o1[1] - o2[1];
                return o1[0] - o2[0];
            });

            for(int j = 0; j < 4; j++){
                int nr = santas[i][0] + dr[j];
                int nc = santas[i][1] + dc[j];
                if(nr < 0 || nr >= N || nc < 0 || nc >= N || santaMap[nr][nc][0] > 0) continue;
                int dist = getDistance(new int[]{nr, nc}, rudolph);

                if(originDist <= dist) continue;

                pq.add(new int[]{dist, j, nr, nc});    
            }
            
            if(pq.isEmpty()) continue;
            
            santaMap[santas[i][0]][santas[i][1]][0] = -1;
            santaMap[santas[i][0]][santas[i][1]][1] = -1;

            int[] result = pq.poll();


            santas[i][0] = result[2];
            santas[i][1] = result[3];
            
            santaMap[santas[i][0]][santas[i][1]][0] = 1;
            santaMap[santas[i][0]][santas[i][1]][1] = i;

            // 충돌
            if(rudolph[0] == santas[i][0] && rudolph[1] == santas[i][1]){
                santaMap[santas[i][0]][santas[i][1]][0] = -1;
                santaMap[santas[i][0]][santas[i][1]][1] = -1;
                
                santaPoints[i] += D;
                santaStatus[i] = STUN;
                stunCnt[i] = 2;
                
                int dir = dirPair.get(result[1]);
                int r = santas[i][0] + (D * dr[dir]);
                int c = santas[i][1] + (D * dc[dir]);

                if(r < 0 || r >= N || c < 0 || c >= N){
                    santaStatus[i] = OUT;
                    stunCnt[i] = 0;
                }else{
                    interact(r, c, dir, i);
                }
            }
        }
    }

    private static void interact(int r, int c, int dir, int seq){
        while(santaMap[r][c][0] >= 0){
            int tSeq = santaMap[r][c][1];

            santas[seq][0] = r;
            santas[seq][1] = c;
            santaMap[r][c][0] = 1;
            santaMap[r][c][1] = seq;

            r += dr[dir];
            c += dc[dir];
            if(r < 0 || r >= N || c < 0 || c >= N){
                santaStatus[tSeq] = OUT;
                break;
            }

            seq = tSeq;
        }

        if(0 <= r && r < N && 0 <= c && c < N){
            santas[seq][0] = r;
            santas[seq][1] = c;
            santaMap[r][c][0] = 1;
            santaMap[r][c][1] = seq;
        }
    }

    private static boolean checkSanta(boolean roundEnd){
        boolean check = false;

        for(int i = 0; i < P; i++){
            if(santaStatus[i] == ARRIVE || santaStatus[i] == STUN){
                if(roundEnd) santaPoints[i]++;
                check = true;
            }
        }
        return check;
    }

    private static int getDistance(int[] p1, int[] p2){
        return (int)Math.pow(p1[0]-p2[0], 2) + (int)Math.pow(p1[1]-p2[1], 2);
    }

    private static void input() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        P = Integer.parseInt(stk.nextToken());
        C = Integer.parseInt(stk.nextToken());
        D = Integer.parseInt(stk.nextToken());
        
        // 루돌프 초기위치
        stk = new StringTokenizer(br.readLine());
        rudolph = new int[2];
        rudolph[0] = Integer.parseInt(stk.nextToken()) - 1;
        rudolph[1] = Integer.parseInt(stk.nextToken()) - 1;

        // 산타 번호 및 초기위치
        santas = new int[P][2];
        stunCnt = new int[P];
        santaStatus = new int[P];
        santaPoints = new int[P];
        santaMap = new int[N][N][2];

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                santaMap[i][j][0] = -1;
                santaMap[i][j][1] = -1;
            }
        }

        for(int i = 0; i < P; i++){
            stk = new StringTokenizer(br.readLine());
            int seq = Integer.parseInt(stk.nextToken()) - 1;
            int r = Integer.parseInt(stk.nextToken()) - 1;
            int c = Integer.parseInt(stk.nextToken()) - 1;

            santas[seq][0] = r;
            santas[seq][1] = c;

            santaMap[r][c][0] = 1;
            santaMap[r][c][1] = seq;
        }

        // 상 우 하 좌 좌상 우상 좌하 우하
        // 상하
        dirPair.put(0, 2);
        dirPair.put(2, 0);

        // 우좌
        dirPair.put(1, 3);
        dirPair.put(3, 1);

        // 좌상 우하
        dirPair.put(4, 7);
        dirPair.put(7, 4);
        
        // 좌하 우상
        dirPair.put(5, 6);
        dirPair.put(6, 5);
    }
}

/*
0 0 5 2 0
0 0 0 4 0
0 1 0 0 0
0 0 0 0 0
0 0 3 0 x

0 0 0 2 0
0 0 5 0 0
0 0 1 4 0
0 0 0 0 0
0 3 0 x 0
[1,1,3,1,1]


// 3 기절 스택 1
0 0 0 0 0
0 0 0 2 0
0 0 5 0 0
0 0 1 4 0
0 3 x 0 0
[2,2,4,2,2]

// 3 기절 스택 2
0 0 0 0 0
0 0 0 0 0
0 5 0 2 0
0 0 4 0 0
3 x 1 0 0
[3,3,7,3,3]
*/