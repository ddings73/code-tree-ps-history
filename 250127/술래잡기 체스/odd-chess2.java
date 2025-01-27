import java.util.*;
import java.io.*;

public class Main {
    private static int max_point = 0;
    private static int[] dr = {0, -1, -1, 0, 1, 1, 1, 0, -1};
    private static int[] dc = {0, 0, -1, -1, -1, 0, 1, 1, 1};
    public static void main(String[] args) {
        int[][] map = new int[4][4];
        Map<Integer, int[]> thief = input(map);
        
        int[] tag = new int[]{0, 0, 0};
        
        playGame(0, map, tag, thief);
        System.out.println(max_point);
    }

    private static void playGame(int point, int[][] map, int[] tag, Map<Integer, int[]> thief){
        // 술래 위치 도둑말 제거 및 포인트 계산
        point += map[tag[0]][tag[1]];
        tag[2] = thief.get(map[tag[0]][tag[1]])[2];

        thief.remove(map[tag[0]][tag[1]]);
        map[tag[0]][tag[1]] = 0;

        max_point = Math.max(max_point, point);

        thiefMove(map, tag, thief);
        for(int len = 1; len < 4; len++){
            int nr = tag[0] + (dr[tag[2]] * len);
            int nc = tag[1] + (dc[tag[2]] * len);
            if(nr < 0 || nr >= 4 || nc < 0 || nc >= 4 || map[nr][nc] == 0) continue;
            int[][] clone_map = new int[4][];
            for(int i = 0; i < 4; i++) clone_map[i] = map[i].clone();
            playGame(point, clone_map, new int[]{nr, nc, tag[2]}, new HashMap<>(thief));
        }
    }

    private static void thiefMove(int[][] map, int[] tag, Map<Integer, int[]> thief){
        for(int i = 1; i <= 16; i++){
            if(!thief.containsKey(i)) continue;
            int[] arr = thief.get(i);
            
            int r = arr[0];
            int c = arr[1];
            int d = arr[2];

            for(int j = 0; j < 8; j++){
                int nr = r + dr[d];
                int nc = c + dc[d];
                if(0 <= nr && nr < 4 && 0 <= nc && nc < 4 && (nr != tag[0] || nc != tag[1])){
                    if(map[nr][nc] != 0){
                        int there = map[nr][nc];
                        int[] there_info = thief.get(there);
                        
                        thief.replace(there, new int[]{r, c, there_info[2]});

                        map[r][c] = there;
                    }else{
                        map[r][c] = 0;
                    }

                    
                    thief.replace(i, new int[]{nr, nc, d});
                    map[nr][nc] = i;
                    break;
                }
                
                d = d + 1 > 8 ? 1 : d + 1;
            }
        }
    }

    private static Map<Integer, int[]> input(int[][] map){
        Map<Integer, int[]> thief = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            for(int i = 0; i < 4; i++){
                StringTokenizer stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < 4; j++){
                    int p = Integer.parseInt(stk.nextToken());
                    int d = Integer.parseInt(stk.nextToken());
                    
                    map[i][j] = p;
                    
                    thief.put(p, new int[]{i, j, d});
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        return thief;
    }
}