import java.util.*;
import java.io.*;

public class Main {
    private static int N;
    private static int[][] map;
    private static Map<String, int[]> groups;

    private static int[] dr = {-1, 1, 0, 0};
    private static int[] dc = {0, 0, -1, 1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        map = new int[N][N];
        for(int i = 0; i < N; i++){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        int score = 0;
        for(int round = 0; round <= 3; round++){
            groups = new HashMap<>();
            String[][] gMap = getGroupInfo();

            Set<String> set = new HashSet<>();
            for(String g1_id : groups.keySet()){
                set.add(g1_id);
                for(String g2_id : groups.keySet()){
                    if(set.contains(g2_id)) continue;
                    int[] v1 = groups.get(g1_id);
                    int[] v2 = groups.get(g2_id);

                    int value = (v1[1] + v2[1]) * v1[0] * v2[0];
                    
                    int count = 0;
                    for(int i = 0; i < N; i++){
                        for(int j = 0; j < N; j++){
                            if(!gMap[i][j].equals(g1_id)) continue;
                            for(int k = 0; k < 4; k++){
                                int nr = i + dr[k];
                                int nc = j + dc[k];
                                if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                                if(gMap[nr][nc].equals(g2_id)) count++;
                            }
                        }
                    }
                    value *= count;
                    score += value;
                }
            }

            rotate();
        }

        System.out.println(score);
    }

    private static String[][] getGroupInfo(){
        int group_num = 1;
        String[][] result = new String[N][N];
        boolean[][] visit = new boolean[N][N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                if(visit[i][j]) continue;
                visit[i][j] = true;
                String group_id = "G" + (group_num++);

                Queue<int[]> q = new ArrayDeque<>();
                q.add(new int[]{i, j});
                result[i][j] = group_id;

                int count = 1;
                while(!q.isEmpty()){
                    int[] info = q.poll();

                    for(int k = 0; k < 4; k++){
                        int nr = info[0] + dr[k];
                        int nc = info[1] + dc[k];
                        if(nr < 0 || nr >= N || nc < 0 || nc >= N || visit[nr][nc]) continue;
                        if(map[nr][nc] != map[i][j]) continue;
                        visit[nr][nc] = true;
                        result[nr][nc] = group_id;
                        count++;
                        q.add(new int[]{nr, nc});
                    }
                }

                
                groups.put(group_id, new int[]{map[i][j], count});
            }
        }

        return result;
    }

    private static void rotate(){
        int mid = N / 2;

        clockRotate(0, 0, mid - 1, mid - 1);
        clockRotate(0, mid + 1, mid - 1, N - 1);
        clockRotate(mid + 1, 0, N - 1, mid - 1);
        clockRotate(mid + 1, mid + 1, N - 1, N - 1);

        int[][] clone = new int[N][];
        for(int i = 0; i < N; i++) clone[i] = map[i].clone();

        for(int i = 0; i < N; i++){
            map[mid][i] = clone[i][mid];
            map[i][mid] = clone[mid][N - 1 - i];
        }
    }

    private static void clockRotate(int r1, int c1, int r2, int c2){
        int[][] arr = new int[N/2][N/2];

        for(int i = 0; i < N/2; i++){
            for(int j = 0; j < N/2; j++){
                arr[i][j] = map[r2-j][c1+i];
            }
        }

        for(int i = 0; i < N/2; i++){
            for(int j = 0; j < N/2; j++){
                map[r1 + i][c1 + j] = arr[i][j];
            }
        }

    }
}