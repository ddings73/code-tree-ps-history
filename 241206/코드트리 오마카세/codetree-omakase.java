import java.util.*;
import java.io.*;

public class Main {
    private static int L, Q;
    private static Map<String, List<Sushi>> sushi_map = new HashMap<>();
    private static Map<String, int[]> people = new HashMap<>();

    private static List<int[]> query = new ArrayList<>();

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
            int command = Integer.parseInt(stk.nextToken());
            int t = -1, x = -1, n = -1;
            String name = null;

            if(command == 100){
                t = Integer.parseInt(stk.nextToken());
                x = Integer.parseInt(stk.nextToken());
                name = stk.nextToken();

                if(sushi_map.containsKey(name)){
                    List<Sushi> list = sushi_map.get(name);
                    list.add(new Sushi(t, x));
                }else{
                    List<Sushi> list = new ArrayList<>();
                    list.add(new Sushi(t, x));
                    sushi_map.put(name, list);
                }
            }else if(command == 200){
                t = Integer.parseInt(stk.nextToken());
                x = Integer.parseInt(stk.nextToken());
                name = stk.nextToken();
                n = Integer.parseInt(stk.nextToken());
                
                people.put(name, new int[]{x, n, t});
            }else if(command == 300){
                t = Integer.parseInt(stk.nextToken());
            }

            query.add(new int[]{command, t, x, n});
        }

        for(String name : people.keySet()){
            int[] info = people.get(name);
            int p_pos = info[0];
            int p_entry = info[2];

            int p_remove_time = 0;
            for(Sushi s : sushi_map.get(name)){
                int s_remove_time = 0;
                if(s.t < p_entry){
                    s.x = (s.x + p_entry - s.t) % L;
                    s_remove_time = p_entry + (p_pos - s.x + L) % L; 
                }else{
                    s_remove_time = s.t + (p_pos - s.x + L) % L;
                }

                p_remove_time = Math.max(p_remove_time, s_remove_time);

                query.add(new int[]{101, s_remove_time, -1, -1});
            }
            query.add(new int[]{201, p_remove_time, -1, -1});
        }

        Collections.sort(query, (o1, o2) -> {
            if(o1[1] == o2[1]) return Integer.compare(o1[0], o2[0]);
            return Integer.compare(o1[1], o2[1]);
        });
        
        int p_cnt = 0, s_cnt = 0;
        for(int[] q : query){
            if(q[0] == 100){
                s_cnt++;
            }else if(q[0] == 101){
                s_cnt--;
            }else if(q[0] == 200){
                p_cnt++;
            }else if(q[0] == 201){
                p_cnt--;
            }else if(q[0] == 300){
                sb.append(p_cnt).append(" ").append(s_cnt).append("\n");
            }
        }

        System.out.println(sb.toString());
    }
}