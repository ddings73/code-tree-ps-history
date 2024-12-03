import java.util.*;
import java.io.*;


/*
이동 시 해당하는 방향 + 좌우까지 비어야함
1. 남쪽으로 한칸 내려감
2. 서쪽으로 한칸 이동한 뒤 남쪽으로 내려감( 과정 중 반시계 회전 )
3. 동쪽으로 한칸 이동한 뒤 남쪽으로 내려감( 과정 중 시계 회전 )
4. 남쪽 끝(골렘의 하단부가 바닥에 접촉 or 다른골렘에 의해 못감 )에 도달하면 골렘 "내"에서 상하좌우 인접한 칸으로 이동
5. 4의 과정에서 출구가 다른 골렘과 붙어있다면 다른골렘으로 이동
6. 정령이 최하단에 도착하면 이동종료 ( 최종 위치 )

# 골렘의 몸 일부가 범위를 벗어나면(북쪽으로) 숲을 통째로 비우고, 다음 골렘부터 이동함 
*/
public class Main {
    private static int R, C, K;

    private static int[] dr = {-1, 0, 1, 0};
    private static int[] dc = {0, 1, 0, -1};

    private static GOLEM[] spirits;
    private static int[][] visit;

    private static class GOLEM{
        int r, c, d;
        GOLEM(int r, int c, int d){
            this.r = r;
            this.c = c;
            this.d = d;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        R = Integer.parseInt(stk.nextToken());
        C = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        spirits = new GOLEM[K];
        for(int i = 0; i < K; i++){
            stk = new StringTokenizer(br.readLine());
            int c = Integer.parseInt(stk.nextToken()) - 1;
            int d = Integer.parseInt(stk.nextToken());

            spirits[i] = new GOLEM(-2, c, d);
        }

        visit = new int[R][C]; 
        int row_sum = 0;

        for(int T = 1; T <= K; T++){
            GOLEM spirit = spirits[T - 1];

            while(spirit.c < C){
                // 남쪽이동
                if(southCheck(spirit.r, spirit.c, visit)){
                    spirit.r++;
                }// 남쪽불가, 서쪽 이동
                else if(spirit.r + 2 != R && westCheck(spirit.r, spirit.c, visit) && southCheck(spirit.r, spirit.c - 1, visit)){
                    spirit.d = (spirit.d + 3) % 4;
                    spirit.r++;
                    spirit.c--;
                }// 남서 불가, 동쪽 이동
                else if(spirit.r + 2 != R && eastCheck(spirit.r, spirit.c, visit) && southCheck(spirit.r, spirit.c + 1, visit)){
                    spirit.d = (spirit.d + 1) % 4;
                    spirit.r++;
                    spirit.c++;
                }// 다 불가, 정령 이동
                else{
                    if(spirit.r < 1){
                        visit = new int[R][C];
                        break;
                    }

                    visit[spirit.r][spirit.c] = visit[spirit.r + 1][spirit.c] = visit[spirit.r - 1][spirit.c] = visit[spirit.r][spirit.c - 1] = visit[spirit.r][spirit.c + 1] = T;
                    visit[spirit.r + dr[spirit.d]][spirit.c + dc[spirit.d]] = -T;

                    Queue<int[]> q = new ArrayDeque<>();
                    q.add(new int[]{spirit.r, spirit.c});
                    boolean[][] spiritVisit = new boolean[R][C];

                    spiritVisit[spirit.r][spirit.c] = true;
                    while(!q.isEmpty()){
                        int[] info = q.poll();
                        if(info[0] > spirit.r){
                            spirit.r = info[0];
                            spirit.c = info[1];
                        }

                        int golem_num = visit[info[0]][info[1]];
                        for(int i = 0; i < 4; i++){
                            int nr = info[0] + dr[i];
                            int nc = info[1] + dc[i];
                            if(nr < 0 || nr >= R || nc < 0 || nc >= C) continue;
                            if(visit[nr][nc] == 0 || spiritVisit[nr][nc]) continue; // 골렘 몸 밖으로 나가거나, 이미 방문했으면 불가
                            if(visit[nr][nc] != -golem_num && golem_num > 0 && golem_num != visit[nr][nc]) continue; // 출구가 아닌데 다른 골렘몸체로 가려고하면 불가
                            
                            spiritVisit[nr][nc] = true;
                            q.add(new int[]{nr, nc});
                        }
                    }

                    if(spirit.r > 0){
                        row_sum += (spirit.r + 1);
                    }
                    break;
                }
            }
        }
        System.out.println(row_sum);
    }

    private static boolean southCheck(int r, int c, int[][] visit){
        return r + 2 < R && visit[r + 2][c] == 0 && ( r + 1 < 0 || visit[r + 1][c - 1] == 0 && visit[r + 1][c + 1] == 0 );
    }

    private static boolean westCheck(int r, int c, int[][] visit){
        return 0 <= c - 2 && ( r < 0 || visit[r][c - 2] == 0 ) && ( r - 1 < 0 || visit[r - 1][c - 1] == 0 ) && ( r + 1 < 0 || visit[r + 1][c - 1] == 0 );
    }

    private static boolean eastCheck(int r, int c, int[][] visit){
        return c + 2 < C && ( r < 0 || visit[r][c + 2] == 0 ) && ( r - 1 < 0 || visit[r - 1][c + 1] == 0 ) && ( r + 1 < 0 || visit[r + 1][c + 1] == 0);
    }
}