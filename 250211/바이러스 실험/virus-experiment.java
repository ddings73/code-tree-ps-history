import java.util.*;
import java.io.*;

public class Main {

    private static int N, M, K;
    private static int[][] board, foodAmount;
    private static PriorityQueue<Virus> virusQ;


    private static final int[] dr = new int[]{-1, -1, 0, 1, 1, 1, 0, -1};
    private static final int[] dc = new int[]{0, -1, -1, -1, 0, 1, 1, 1};
    private static class Virus{
        int r, c, age;
        Virus(int r, int c, int age){
            this.r = r;
            this.c = c;
            this.age = age;
        }
    }
    public static void main(String[] args) {
        input();

        while(K-- > 0){
            Queue<Virus> newVirusQ = new ArrayDeque<>();
            Queue<Virus> deadVirusQ = new ArrayDeque<>();

            // 각 바이러스들의 양분 섭취
            while(!virusQ.isEmpty()){
                Virus v = virusQ.poll();
                if(v.age > board[v.r][v.c]){
                    deadVirusQ.add(v);
                    continue;
                }

                board[v.r][v.c] -= v.age;
                v.age++;

                newVirusQ.add(v);
            }

            // 죽은 바이러스의 양분화
            while(!deadVirusQ.isEmpty()){
                Virus dv = deadVirusQ.poll();
                int food = (int)(dv.age / 2);
                board[dv.r][dv.c] += food;
            }

            // 바이러스 번식
            while(!newVirusQ.isEmpty()){
                Virus v = newVirusQ.poll();
                virusQ.add(v);

                if(v.age % 5 != 0) continue;
                
                for(int i = 0; i < 8; i++){
                    int nr = v.r + dr[i];
                    int nc = v.c + dc[i];
                    if(nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                    virusQ.add(new Virus(nr, nc, 1));
                }
            }

            // 양분 추가
            for(int i = 0; i < N; i++){
                for(int j = 0; j < N; j++){
                    board[i][j] += foodAmount[i][j];
                }
            }

        }

        System.out.println(virusQ.size());
    }

    private static void input(){
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            M = Integer.parseInt(stk.nextToken());
            K = Integer.parseInt(stk.nextToken());

            board = new int[N][N];
            foodAmount = new int[N][N];
            // 마지막에 추가되는 양분 양
            for(int i = 0; i < N; i++){
                stk = new StringTokenizer(br.readLine());
                for(int j = 0; j < N; j++){
                    foodAmount[i][j] = Integer.parseInt(stk.nextToken());
                    board[i][j] = 5;
                }
            }

            // 바이러스 정보
            virusQ = new PriorityQueue<>((o1, o2)->o1.age - o2.age);
            for(int i = 0; i < M; i++){
                stk = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(stk.nextToken()) - 1;
                int c = Integer.parseInt(stk.nextToken()) - 1;
                int age = Integer.parseInt(stk.nextToken());

                Virus virus = new Virus(r, c, age);
                virusQ.add(virus);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}