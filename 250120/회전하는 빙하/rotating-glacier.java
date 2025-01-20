import java.util.*;
import java.io.*;

public class Main {

    private static int N, Q;
    private static int[][] map;

    private static int[] dr = {1, -1, 0, 0};
    private static int[] dc = {0, 0, 1, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        Q = Integer.parseInt(stk.nextToken());

        N = (int)Math.pow(2, N);
        map = new int[N][N];

        for(int i = 0; i < N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        stk = new StringTokenizer(br.readLine());
        while(Q-- > 0){
            int level = Integer.parseInt(stk.nextToken());

            if(level > 0){
                int len = (int)Math.pow(2, level);
                spin(len);
            }

            int[][] tmp = new int[N][N];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    if(map[i][j] > 0){
                        int count = 0;
                        for(int k = 0; k < 4; k++){
                            int nr = i + dr[k];
                            int nc = j + dc[k];
                            if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                            if(map[nr][nc] == 0) continue;
                            count++;
                        }

                        tmp[i][j] = count >= 3 ? map[i][j] : map[i][j] - 1;
                    }
                }
            }

            map = tmp;
        }


        int sum = 0;
        int max = 0;
        boolean[][] visit = new boolean[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(map[i][j] == 0) continue;

                sum += map[i][j];
                if(visit[i][j]) continue;
                visit[i][j] = true;

                int size = 0;
                Queue<int[]> q = new ArrayDeque<>();
                q.add(new int[]{i, j});
                while(!q.isEmpty()){
                    int[] p = q.poll();

                    size++;
                    for(int k = 0; k < 4; k++){
                        int nr = p[0] + dr[k];
                        int nc = p[1] + dc[k];
                        if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                        if(visit[nr][nc] || map[nr][nc] == 0) continue;
                        visit[nr][nc] = true;
                        q.add(new int[]{nr, nc});
                    }
                }

                max = Math.max(max, size);
            }
        }

        System.out.println(sum);
        System.out.println(max);
    }

    private static void spin(int len){
        for(int i = 0; i + len <= N; i += len){
            for(int j = 0; j + len <= N; j += len){
                int[] from = new int[]{i, j};
                int[] to = new int[]{i + len, j + len};

                subtask(from, to, len);
            }
        }
    }

    private static void subtask(int[] from, int[] to, int len){
        len = len >> 1;

        int idx = 0;
        int[][][] tmp = new int[4][len][len];
        for(int i = from[0]; i + len <= to[0]; i += len){
            for(int j = from[1]; j + len <= to[1]; j += len){

                for(int a = 0; a < len; a++){
                    for(int b = 0; b < len; b++){
                        tmp[idx][a][b] = map[i + a][j + b];
                    }
                }

                idx++;
            }
        }

        // 2 0 3 1
        idx = 2;
        for(int i = from[0]; i + len <= to[0]; i += len){
            for(int j = from[1]; j + len <= to[1]; j += len){

                for(int a = 0; a < len; a++){
                    for(int b = 0; b < len; b++){
                        map[i + a][j + b] = tmp[idx][a][b];
                    }
                }

                idx = idx == 2 ? 0
                    : idx == 0 ? 3
                    : 1;
            }
        }
    }
}