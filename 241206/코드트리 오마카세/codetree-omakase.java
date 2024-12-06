import java.util.*;
import java.io.*;

/*
사람이 들어왔다면, 레일위의 대상 초밥들이 먹히는 시점이 정해짐

*/
public class Main {
    private static int L, Q;
    private static Queue<Sushi> sushi = new ArrayDeque<>();
    private static Map<String, Queue<Sushi>> sushi_map = new HashMap<>();
    private static Map<String, int[]> people = new HashMap<>();

    private static class Sushi{
        int t, x;
        Sushi(int t, int x){
            this.t = t;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());

        L = Integer.parseInt(stk.nextToken()); // 10억
        Q = Integer.parseInt(stk.nextToken()); // 10만

        StringBuilder sb = new StringBuilder();

        while(Q-- > 0){ // max 10만
            stk = new StringTokenizer(br.readLine());
            int query = Integer.parseInt(stk.nextToken());
            if(query == 100){
                // t 시각에 x위치에 name 초밥 생성
                int t = Integer.parseInt(stk.nextToken());
                int x = Integer.parseInt(stk.nextToken());
                String name = stk.nextToken();

                if(people.containsKey(name)){
                    int[] info = people.get(name);
                    if(info[0] == x){
                        info[1]--;
                        if(info[1] == 0){
                            people.remove(name);
                        }
                        continue;
                    }
                }

                if(sushi_map.containsKey(name)){
                    sushi_map.get(name).add(new Sushi(t, x));
                }else{
                    Queue<Sushi> q = new ArrayDeque<>();
                    q.add(new Sushi(t, x));
                    sushi_map.put(name, q);
                }
            }else if(query == 200){
                // name인 사람이 t 시각에 x 위치에서 n개 초밥 제거 ( 자신의 name과 동일해야 함 )
                int t = Integer.parseInt(stk.nextToken());
                int x = Integer.parseInt(stk.nextToken());
                String name = stk.nextToken();
                int n = Integer.parseInt(stk.nextToken());

                Queue<Sushi> q = sushi_map.get(name);
                int sushi_cnt = q.size();
                while(sushi_cnt-- > 0){
                    Sushi s = q.poll();

                    int time = t - s.t;
                    int pos = (s.x + time) % L;
                    if(pos != x){
                        s.t = t;
                        s.x = pos;
                        q.add(s);
                    }else n--;
                }

                if(n == 0) continue;
                people.put(name, new int[]{x, n});
            }else if(query == 300){
                // t 시각의 사람 수 와 초밥 수 출력
                int t = Integer.parseInt(stk.nextToken());

                for(String name : people.keySet()){
                    int[] info = people.get(name);

                    if(!sushi_map.containsKey(name)) continue;
                    
                    Queue<Sushi> q = sushi_map.get(name);
                    int sushi_cnt = q.size();
                    while(sushi_cnt-- > 0){
                        Sushi s = q.poll();

                        int time = t - s.t;
                        if(time >= L) info[1]--;
                        else{
                            int p_pos = info[0];
                            if(p_pos < s.x) p_pos += L; 
                            if(s.x <= p_pos && p_pos <= (s.x + time)) info[1]--;
                            else{
                                s.t = t;
                                s.x = (s.x + time) % L;
                                q.add(s);
                            }
                        }


                        if(info[1] == 0) people.remove(name);
                    }
                }
                
                int sushi_cnt = 0;
                for(String name : sushi_map.keySet()){
                    sushi_cnt += sushi_map.get(name).size();
                }

                sb.append(people.size()).append(" ").append(sushi_cnt).append("\n");
            }
        }

        System.out.println(sb.toString());
    }
}