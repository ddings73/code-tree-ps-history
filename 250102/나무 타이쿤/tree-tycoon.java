import java.util.*;
import java.io.*;

public class Main {
    private static int N, M;

    private static Queue<int[]> q = new ArrayDeque<>();
    private static int[][] map;
    private static int[] dr = {0, -1, -1, -1, 0, 1, 1, 1};
    private static int[] dc = {1, 1, 0, -1, -1, -1, 0, 1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());

        map = new int[N][N];
        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());

            }
        }

        q.add(new int[]{N - 2, 0});
        q.add(new int[]{N - 2, 1});
        q.add(new int[]{N - 1, 0});
        q.add(new int[]{N - 1, 1});

        for(int year = 1; year <= M; year++){
            stk = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(stk.nextToken()) - 1;
            int p = Integer.parseInt(stk.nextToken());
            
            boolean[][] visit = new boolean[N][N];
            Queue<int[]> tmp = new ArrayDeque<>();
            while(!q.isEmpty()){
                int[] pos = q.poll();
                int nr = (pos[0] + (p * dr[d])) % N;
                int nc = (pos[1] + (p * dc[d])) % N;
                if(nr < 0) nr += N;
                if(nc < 0) nc += N; 

                visit[nr][nc] = true;
                map[nr][nc]++;
                tmp.add(new int[]{nr, nc});
            }

            while(!tmp.isEmpty()){
                int[] pos = tmp.poll();

                int cnt = 0;
                for(int i = 1; i < 8; i += 2){
                    int nr = pos[0] + dr[i];
                    int nc = pos[1] + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(map[nr][nc] >= 1) cnt++;
                }
                map[pos[0]][pos[1]] += cnt;
            }

            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(!visit[i][j] && map[i][j] >= 2){
                        map[i][j] -= 2;
                        q.add(new int[]{i, j});
                    }
                }
            }
        }

        int sum = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                sum += map[i][j];
            }
        }

        System.out.println(sum);
    }
}