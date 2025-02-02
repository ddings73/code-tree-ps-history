import java.util.*;
import java.io.*;

public class Main {
    private static int N, M;
    private static int[][] board;

    private static List<int[]> hospital;
    private static int[][][] hospitalRmBoard;

    private static int[] dr = new int[]{1, -1, 0, 0};
    private static int[] dc = new int[]{0, 0, 1, -1};
    public static void main(String[] args) {
        input();

        int hospitalCnt = hospital.size();
        bfsEachHospital(hospitalCnt);
        
        int answer = dfsForSelectHospital(0, 0, hospitalCnt, 0);
        System.out.println(answer);
    }

    private static void bfsEachHospital(int hospitalCnt){
        hospitalRmBoard = new int[hospitalCnt][][];

        int idx = 0;
        for(int[] h_pos : hospital){
            boolean[][] visit = new boolean[N][N];
            visit[h_pos[0]][h_pos[1]] = true;

            int[][] rmTimeBoard = new int[N][N];

            // bfs 시작
            Queue<int[]> q = new ArrayDeque<>();
            q.add(new int[]{h_pos[0], h_pos[1], 0});
            while(!q.isEmpty()){
                int[] p = q.poll();
                
                if(board[p[0]][p[1]] == 0) 
                    rmTimeBoard[p[0]][p[1]] = p[2];

                for(int i = 0; i < 4; i++){
                    int nr = p[0] + dr[i];
                    int nc = p[1] + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N || visit[nr][nc]) continue;
                    if(board[nr][nc] == 1) continue;

                    visit[nr][nc] = true;
                    q.add(new int[]{nr, nc, p[2] + 1});
                }
            }

            hospitalRmBoard[idx++] = rmTimeBoard;
        }
    }

    private static int dfsForSelectHospital(int cnt, int idx, int hCnt, int selected){
        if(cnt == M){
            int hIdx = 0;

            int[][] mergeBoard = new int[N][N];
            while(selected > 0){
                int v = selected % 2;
                if(v == 1){
                    for(int i = 0; i < N; i++){
                        for(int j = 0; j < N; j++){
                            if(board[i][j] != 0) continue;
                            mergeBoard[i][j] = mergeBoard[i][j] == 0 || mergeBoard[i][j] > hospitalRmBoard[hIdx][i][j] 
                                ? hospitalRmBoard[hIdx][i][j]
                                : mergeBoard[i][j];
                        }
                    }
                }

                selected /= 2;
                hIdx++;
            }
            
            int result = 0;
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(board[i][j] != 0) continue;
                    if(mergeBoard[i][j] == 0) return -1;
                    result = Math.max(result, mergeBoard[i][j]);
                }
            }
            return result;
        }
        if(idx == hCnt) return -1;

        int result = -1;
        int r1 = dfsForSelectHospital(cnt + 1, idx + 1, hCnt, selected | (1 << idx));
        int r2 = dfsForSelectHospital(cnt, idx + 1, hCnt, selected);

        result = r1 != -1 && (r1 < result || result == -1) ? r1 : result;
        result = r2 != -1 && (r2 < result || result == -1) ? r2 : result;

        return result;
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());

            // N => 영역 크기
            // M => 병원 수
            N = Integer.parseInt(stk.nextToken());
            M = Integer.parseInt(stk.nextToken());

            // 영역 정보 입력
            // 0 => 바이러스
            // 1 => 벽
            // 2 => 병원
            board = new int[N][N];
            hospital = new ArrayList<>();
            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    board[i][j] = Integer.parseInt(stk.nextToken());
                    if(board[i][j] == 2){
                        hospital.add(new int[]{i, j});
                    }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}