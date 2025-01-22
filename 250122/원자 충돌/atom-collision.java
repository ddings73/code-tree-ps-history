import java.util.*;
import java.io.*;

public class Main {

    private static int N, M, K;
    private static Queue<int[]>[][] map;

    private static int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static int[] dc = {0, 1, 1, 1, 0, -1, -1, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());

        map = new ArrayDeque[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                map[i][j] = new ArrayDeque<>();
            }
        }

        for(int i = 0; i < M; i++){
            stk = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(stk.nextToken()) - 1;
            int y = Integer.parseInt(stk.nextToken()) - 1;
            int m = Integer.parseInt(stk.nextToken());
            int s = Integer.parseInt(stk.nextToken());
            int d = Integer.parseInt(stk.nextToken());

            map[x][y].add(new int[]{m, s, d});
        }

        while(K-- > 0){
            // 이동
            Queue<int[]>[][] nxt_map = new ArrayDeque[N][N];
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    nxt_map[i][j] = new ArrayDeque<>();
                }
            }

            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    while(!map[i][j].isEmpty()){
                        int[] atom = map[i][j].poll();

                        int nr = i + (dr[atom[2]] * atom[1]);
                        int nc = j + (dc[atom[2]] * atom[1]);
                        
                        while(nr < 0) nr += N;
                        while(nc < 0) nc += N;
                        
                        nr %= N;
                        nc %= N;
                        
                        nxt_map[nr][nc].add(atom);
                    }
                }
            }

            // 원자결합 및 분열
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    int count = nxt_map[i][j].size();
                    if(count < 2) map[i][j].addAll(nxt_map[i][j]);
                    else{
                        int m = 0, s = 0;
                        boolean even = false, odd = false;
                        for(int[] atom : nxt_map[i][j]){
                            m += atom[0];
                            s += atom[1];

                            even = even || atom[2] % 2 == 0;
                            odd = odd || atom[2] % 2 == 1;
                        }

                        m /= 5;
                        s /= count;

                        if(m == 0) continue;
                        
                        for(int k = 0; k < 8; k += 2){
                            int d = even && odd ? k + 1 : k;
                            map[i][j].add(new int[]{m, s, d});
                        }
                    }
                }
            }
        }

        int answer = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                for(int[] atom : map[i][j]){
                    answer += atom[0];
                }
            }
        }

        System.out.println(answer);
    }
}