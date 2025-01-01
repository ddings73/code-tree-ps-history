import java.util.*;
import java.io.*;

public class Main {
    private static int N, M;

    private static int[][] map;

    private static int[] dr = {0, 1, 0, -1};
    private static int[] dc = {1, 0, -1, 0};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        N = Integer.parseInt(stk.nextToken());
        M = Integer.parseInt(stk.nextToken());

        map = new int[N][N];
        for(int i = 0; i <  N; i++){
            stk = new StringTokenizer(br.readLine());
            for(int j = 0; j < N; j++){
                map[i][j] = Integer.parseInt(stk.nextToken());
            }
        }

        int point = 0;
        for(int round = 1; round <= M; round++){
            stk = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(stk.nextToken());
            int p = Integer.parseInt(stk.nextToken());
            
            for(int i = 1; i <= p; i++){
                int nr = N / 2 + (i * dr[d]);
                int nc = N / 2 + (i * dc[d]);
                if(nr < 0 || nr >= N || nc < 0 || nc >= N) break;
                point += map[nr][nc];
                map[nr][nc] = 0;
            }

            // 빈공간 채우기
            // 몬스터 종류 4번이상 반복 연쇄확인 및 제거
            Queue<Integer> q = printInQueue();

            boolean remove = true;
            while(remove){
                remove = false;

                ArrayDeque<Integer> dq = new ArrayDeque<>();
                
                int prev_num = -1;
                int count = 0;
                while(!q.isEmpty()){
                    Integer num = q.poll();
                    if(prev_num == -1 || prev_num != num){
                        if(count < 4){
                            for(int i = 0; i < count; i++) 
                                dq.addLast(prev_num);
                        }else{
                            point += (count * prev_num);
                            remove = true;
                        }

                        prev_num = num;
                        count = 1;
                    }else if(prev_num == num){
                        count++;
                    }
                }

                if(count < 4){
                    for(int i = 0; i < count; i++) 
                        dq.addLast(prev_num);
                }else{
                    point += (count * prev_num);
                    remove = true;
                }


                q = new ArrayDeque<>(dq);
            }

            // 차례대로 나열하여 새롭게 삽입
            Queue<Integer> newNums = new ArrayDeque<>();
            int prev_num = -1;
            int count = 0;
            while(!q.isEmpty()){
                int num = q.poll();
                if(prev_num == -1 || prev_num != num){
                    if(count > 0){
                        newNums.add(count);
                        newNums.add(prev_num);
                    }

                    prev_num = num;
                    count = 1;
                }else if(prev_num == num){
                    count++;
                }
            }
            newNums.add(count);
            newNums.add(prev_num);

            map = new int[N][N];
            refillMap(newNums);
        }

        System.out.println(point);
    }

    private static Queue<Integer> printInQueue(){
        int r = N / 2;
        int c = N / 2;

        Queue<Integer> q = new ArrayDeque<>();
        
        int d = 2; // 2 -> 1 -> 0 -> 4 ..
        int len = 1, repeat = 0, cnt = 0, limit = 1;

        while(r != 0 || c != 0){
            repeat++;
            
            for(int i = 0; i < len; i++, cnt++){
                r = r + dr[d];
                c = c + dc[d];
                if(r == 0 && c == 0) break;
                if(map[r][c] != 0) q.add(map[r][c]);
            }

            if(cnt == limit){
                cnt = 0;
                d = (d + 3) % 4;
            }

            if(repeat == 2){
                repeat = 0;
                len++;
                limit++;
            }
        }
        return q;
    }
    
    private static void refillMap(Queue<Integer> q){
        int r = N / 2;
        int c = N / 2;
        
        int d = 2; // 2 -> 1 -> 0 -> 4 ..
        int len = 1, repeat = 0, cnt = 0, limit = 1;

        while(!q.isEmpty() && (r != 0 || c != 0)){
            repeat++;
            
            for(int i = 0; !q.isEmpty() && i < len; i++, cnt++){
                r = r + dr[d];
                c = c + dc[d];
                if(r == 0 && c == 0) break;
                map[r][c] =  q.poll();
            }

            if(cnt == limit){
                cnt = 0;
                d = (d + 3) % 4;
            }

            if(repeat == 2){
                repeat = 0;
                len++;
                limit++;
            }
        }
    }
}