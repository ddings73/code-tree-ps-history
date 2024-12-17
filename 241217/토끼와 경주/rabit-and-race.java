import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N, M, P;

    private static Map<Integer, Rabbit> rabbits = new HashMap<>();
    private static int[] jump_count;
    private static boolean[] pick_history;

    private static long minus_point;
    private static long[] point;
    private static long[] discount_point;

    private static int[] dr = new int[]{-1, 1, 0, 0};
    private static int[] dc = new int[]{0, 0, -1, 1};

    private static PriorityQueue<Rabbit> pq1 = new PriorityQueue<>((o1, o2)->{
        int task = Integer.compare(o1.jump_count, o2.jump_count);
        if(task != 0) return task;
        
        task = Integer.compare(o1.r + o1.c, o2.r + o2.c);
        if(task != 0) return task;
        
        task = Integer.compare(o1.r, o2.r);
        if(task != 0) return task;

        task = Integer.compare(o1.c, o2.c);
        if(task != 0) return task;
        
        return Integer.compare(o1.pid, o2.pid);
    });
    
    private static class Rabbit{
        int idx, pid, d, jump_count;
        int r, c;
        Rabbit(int idx, int pid, int d){
            this.idx = idx;
            this.pid = pid;
            this.d = d;
            this.jump_count = 0;
            this.r = 1;
            this.c = 1;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            String command = stk.nextToken();

            if("100".equals(command)){ 
                N = Integer.parseInt(stk.nextToken());
                M = Integer.parseInt(stk.nextToken());
                P = Integer.parseInt(stk.nextToken());

                point = new long[P];
                discount_point = new long[P];
                for(int i = 0; i < P; i++){
                    int pid = Integer.parseInt(stk.nextToken());
                    int d = Integer.parseInt(stk.nextToken());
                    Rabbit rabbit = new Rabbit(i, pid, d);
                    rabbits.put(pid, rabbit);
                    pq1.add(rabbit);
                }
            }else if("200".equals(command)){
                int K = Integer.parseInt(stk.nextToken());
                int S = Integer.parseInt(stk.nextToken());
                pick_history = new boolean[P];
                while(K-- > 0){
                    Rabbit rabbit = pq1.poll();
                    pick_history[rabbit.idx] = true;
                    
                    PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2)->{
                        int task = Integer.compare(o2[0] + o2[1], o1[0] + o1[1]);
                        if(task != 0) return task;

                        task = Integer.compare(o2[0], o1[0]);
                        if(task != 0) return task;

                        return Integer.compare(o2[1], o1[1]);
                    });

                    for(int i = 0; i < 4; i++){
                        int nr = rabbit.r + (dr[i] * rabbit.d);
                        int nc = rabbit.c + (dc[i] * rabbit.d);

                        if(nr <= 0 || nr > N)
                            nr = convertPos(nr, N);
                        if(nc <= 0 || nc > M)
                            nc = convertPos(nc, M);

                        pq.add(new int[]{nr, nc});
                    }

                    int[] info = pq.poll();
                    discount_point[rabbit.idx] += (info[0] + info[1]);
                    minus_point += (info[0] + info[1]);
                    rabbit.r = info[0];
                    rabbit.c = info[1];
                    rabbit.jump_count++;
                    pq1.add(rabbit);
                }
                Rabbit rabbit = select();
                point[rabbit.idx] += S;
            }else if("300".equals(command)){
                int pid = Integer.parseInt(stk.nextToken());
                int L = Integer.parseInt(stk.nextToken());

                Rabbit rabbit = rabbits.get(pid);
                rabbit.d *= L;
            }else if("400".equals(command)){
                long max = 0;
                for(int i = 0; i < P; i++){
                    max = Math.max(max, point[i] + minus_point - discount_point[i]);
                }  

                System.out.println(max);
            }
        }
    }

    private static Rabbit select(){
        PriorityQueue<Rabbit> pq = new PriorityQueue<>((o1, o2)->{
            int task = Integer.compare(o2.r + o2.c, o1.r + o1.c);
            if(task != 0) return task;

            task = Integer.compare(o2.r, o1.r);
            if(task != 0) return task;

            task = Integer.compare(o2.c, o1.c);
            if(task != 0) return task;

            return Integer.compare(o2.pid, o1.pid);
        });

        for(Rabbit rabbit : rabbits.values()){
            if(!pick_history[rabbit.idx]) continue;
            pq.add(rabbit);
        }

        return pq.poll();
    }

    private static int convertPos(int p, int len){
        if(p > 0){
            int v = (p - len) % ((len - 1) * 2);
            if( v == 0 ) return len;
            p = 1 <= v && v < len ? len - v : v % (len - 1) + 1;
        }else{
            p = -p;
            int v = p % ((len - 1) * 2);
            p = v < len - 1 ? v + 2 : len - (v % (len - 1) + 1);
        }

        return p;
    }
}

/*
< 6 7 8 9
> 10 11 12 13
< 14 15 16 17
> 18 19 20 21
< 22 23 24 25

> 0 1 2 3
< 4 5 6 7
> 8 9 10 11
< 12 13 14 15
> 16 17 18 19
*/