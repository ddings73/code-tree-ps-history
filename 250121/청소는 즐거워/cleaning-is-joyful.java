import java.util.*;
import java.io.*;

public class Main {

    private static int N;
    private static int[][] map;

    private static int[] dr = {0, 1, 0, -1};
    private static int[] dc = {-1, 0, 1, 0};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        map = new int[N][N];
        int dust = 0;
        for(int i = 0; i < N; i++){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
                dust += map[i][j];
            }
        }

        // 청소 시작
        int r = N/2, c = N/2;
        int dir = 0, move = 0;
        int dist = 1, count = 0;
        while(r != 0 || c != 0){
            move++;

            spreadDust(r, c, dir);

            r += dr[dir];
            c += dc[dir];

            if(move == dist){
                move = 0;
                dir = (dir + 1) % 4;
                count++;

                if(count == 2){
                    count = 0;
                    dist++;
                }
            }
        }

        // 청소 끝

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                dust -= map[i][j];
            }
        }

        System.out.println(dust);
    }

    private static void spreadDust(int r, int c, int dir){
        int nr = r + dr[dir];
        int nc = c + dc[dir];

        int value = map[nr][nc];
        int spreaded = 0;
        map[nr][nc] = 0;
        
        spreaded += percentOf((int)(value * 0.05), nr + (dr[dir] * 2), nc + (dc[dir] * 2)); // 2칸 앞
        spreaded += percentOf((int)(value * 0.07), nr + dr[(dir + 1) % 4], nc + dc[(dir + 1) % 4]); // 옆 한 칸
        spreaded += percentOf((int)(value * 0.07), nr + dr[(dir + 3) % 4], nc + dc[(dir + 3) % 4]); // 옆 한 칸
        spreaded += percentOf((int)(value * 0.02), nr + (dr[(dir + 1) % 4] * 2), nc + (dc[(dir + 1) % 4] * 2)); // 옆 두 칸
        spreaded += percentOf((int)(value * 0.02), nr + (dr[(dir + 3) % 4] * 2), nc + (dc[(dir + 3) % 4] * 2)); // 옆 두 칸
        spreaded += percentOf((int)(value * 0.1), nr + dr[dir] + dr[(dir + 1) % 4], nc + dc[dir] + dc[(dir + 1) % 4]); // 한칸 앞 옆
        spreaded += percentOf((int)(value * 0.1), nr + dr[dir] + dr[(dir + 3) % 4], nc + dc[dir] + dc[(dir + 3) % 4]); // 한칸 앞 옆
        spreaded += percentOf((int)(value * 0.01), nr + dr[(dir + 2) % 4] + dr[(dir + 1) % 4], nc + dc[(dir + 2) % 4] + dc[(dir + 1) % 4]); // 한칸 뒤 옆
        spreaded += percentOf((int)(value * 0.01), nr + dr[(dir + 2) % 4] + dr[(dir + 3) % 4], nc + dc[(dir + 2) % 4] + dc[(dir + 3) % 4]);
        percentOf(value - spreaded, nr + dr[dir], nc + dc[dir]);

    }

    private static int percentOf(int value, int r, int c){
        if(0 <= r && r < N && 0 <= c && c < N) map[r][c] += value;
        return value;
    }
}