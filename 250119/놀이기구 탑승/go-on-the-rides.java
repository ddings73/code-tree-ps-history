import java.util.*;
import java.io.*;

public class Main {
    private static int N;
    private static int[][] map;

    private static Map<Integer, List<Integer>> favorite_info = new HashMap<>();
    

    private static int[] dr = {1, -1, 0, 0};
    private static int[] dc = {0, 0, 1, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        map = new int[N][N];

        for(int i = 0; i < N*N; i++){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            int n0 = Integer.parseInt(stk.nextToken());
            int n1 = Integer.parseInt(stk.nextToken());
            int n2 = Integer.parseInt(stk.nextToken());
            int n3 = Integer.parseInt(stk.nextToken());
            int n4 = Integer.parseInt(stk.nextToken());

            int[] pos = selectPosition(n1, n2, n3, n4);
            map[pos[0]][pos[1]] = n0;
            
            List<Integer> v = new ArrayList<>();
            v.add(n1);
            v.add(n2);
            v.add(n3);
            v.add(n4);

            favorite_info.put(n0, v);
        }

        int point = 0;
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(map[i][j] == 0) continue;
                List<Integer> favorite = favorite_info.get(map[i][j]);

                int count = 0;
                for(int k = 0; k < 4; k++){
                    int nr = i + dr[k];
                    int nc = j + dc[k];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(!favorite.contains(map[nr][nc])) continue;
                    count++;
                }

                if(count == 0) continue;
                point += Math.pow(10, count - 1);
            }
        }

        System.out.println(point);
    }

    public static int[] selectPosition(int n1, int n2, int n3, int n4){
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
            Integer task = Integer.compare(o2[2], o1[2]); // 좋아하는 사람 수
            if(task == 0){
                task = Integer.compare(o2[3], o1[3]); // 빈 칸의 수
                if(task == 0){
                    task = Integer.compare(o1[0], o2[0]);
                    if(task == 0) task = Integer.compare(o1[1], o2[1]);
                }
            }
            return task;
        });

        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(map[i][j] != 0) continue;
                int[] info = new int[]{i, j, 0, 0};
                for(int k = 0; k < 4; k++){
                    int nr = i + dr[k];
                    int nc = j + dc[k];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    if(map[nr][nc] == 0) info[3]++;
                    else if(map[nr][nc] == n1 || map[nr][nc] == n2 || map[nr][nc] == n3 || map[nr][nc] == n4){
                        info[2]++;
                    }
                }
                pq.add(info);
            }
        }

        int[] result = pq.poll();
        return new int[]{result[0], result[1]};
    }
}