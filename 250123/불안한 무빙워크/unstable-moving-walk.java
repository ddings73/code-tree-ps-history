import java.util.*;
import java.io.*;

public class Main {
    private static int N, K;
    private static int[] point;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(stk.nextToken());
        K = Integer.parseInt(stk.nextToken());
        point = new int[2*N];

        stk = new StringTokenizer(br.readLine());
        for(int i = 0; i < 2 * N; i++){
            point[i] = Integer.parseInt(stk.nextToken());
        }

        int p1 = 0;
        Queue<Integer> q = new ArrayDeque<>();
        boolean[] check = new boolean[2*N];
        
        int count = 0;
        while(checkPoint()){
            count++;

            p1 = p1 == 0 ? 2*N - 1 : p1 - 1;
            int pN = (p1 + N) % (2 * N);
            
            Queue<Integer> clone = new ArrayDeque(q);
            
            q.clear();
            while(!clone.isEmpty()){
                int pos = clone.poll();

                int n_pos = (pos + 1) % (2 * N);
                if(check[n_pos] || point[n_pos] == 0) q.add(pos);
                else if(n_pos != pN){
                    check[pos] = false;
                    check[n_pos] = true;

                    point[n_pos]--;

                    q.add(n_pos);
                }
            }

            if(!check[p1] && point[p1] > 0){
                check[p1] = true;
                point[p1]--;
                q.add(p1);
            }
        }

        System.out.println(count);
    }

    private static boolean checkPoint(){
        int zero_count = 0;
        for(int i = 0; i < 2 * N; i++){
            if(point[i] == 0) zero_count++; 
        }

        return zero_count < K;
    }
}