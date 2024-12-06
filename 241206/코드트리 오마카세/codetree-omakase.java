import java.util.*;
import java.io.*;

public class Main {
    private static int L, Q;
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

                if(sushi_map.containsKey(name)){
                    Queue<Sushi> q = sushi_map.get(name);
                    q.add(new Sushi(t, x));
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
                
                people.put(name, new int[]{x, n, t});
            }else if(query == 300){
                // t 시각의 사람 수와 초밥 수 출력
                int t = Integer.parseInt(stk.nextToken());

                Map<String, int[]> new_people = new HashMap<>();
                for(String name : people.keySet()){
                    int[] info = people.get(name);
                    int people_pos = info[0];
                    int people_eat = info[1];
                    int people_entry = info[2];

                    if(!sushi_map.containsKey(name)){
                        new_people.put(name, new int[]{people_pos, people_eat, people_entry});
                        continue;
                    }

                    Queue<Sushi> q = sushi_map.get(name);
                    Queue<Sushi> new_q = new ArrayDeque<>();
                    while(!q.isEmpty()){
                        Sushi s = q.poll();
                        // 사람 입장 전에 나온 초밥이라면
                        if(s.t < people_entry){
                            s.x = (s.x + people_entry - s.t) % L;
                            s.t = people_entry;
                            if(s.x == people_pos){
                                people_eat--;
                                continue;
                            }
                        }

                        int new_s_pos = (s.x + t - s.t);
                        int p_dummy = people_pos < s.x ? people_pos + L : people_pos;
                        if(s.x <= p_dummy && p_dummy <= new_s_pos) people_eat--;
                        else{
                            s.t = t;
                            s.x = new_s_pos % L;
                            new_q.add(s);
                        }

                    }

                    if(!new_q.isEmpty()) sushi_map.put(name, new_q);
                    else sushi_map.remove(name);

                    if(people_eat > 0) new_people.put(name, new int[]{people_pos, people_eat, people_entry});
                }

                people = new_people;
                
                int sum = 0;
                for(Queue<Sushi> q : sushi_map.values()) sum += q.size();
                sb.append(people.size()).append(" ").append(sum).append("\n");
            }
        }

        System.out.println(sb.toString());
    }
}