import java.util.*;
import java.io.*;

/*
사람이 들어왔다면, 레일위의 대상 초밥들이 먹히는 시점이 정해짐

*/
public class Main {
    private static int L, Q;
    private static Map<String, Map<Integer, int[]>> sushi_map = new HashMap<>();
    private static Map<String, int[]> people = new HashMap<>();

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
                        if(--info[1] == 0) people.remove(name);
                        continue;
                    }

                    if(!sushi_map.containsKey(name) || sushi_map.get(name).size() == 0){
                        Map<Integer, int[]> map = new HashMap<>();
                        map.put(x, new int[]{t, 1});
                        sushi_map.put(name, map);
                    }else{
                        Map<Integer, int[]> map = sushi_map.get(name);
                        Map<Integer, int[]> new_map = new HashMap<>();
                        new_map.put(x, new int[]{t, 1});
                        for(int pos : map.keySet()){
                            int[] sushi = map.get(pos);
                            int time = t - sushi[0];
                            if(time >= L) info[1] -= sushi[1];
                            else{
                                int new_pos = (pos + time);
                                int p_pos = info[0] < pos ? info[0] + L : info[0];
                                if(pos <= p_pos && p_pos <= new_pos) info[1] -= sushi[1];
                                else{
                                    new_pos %= L;
                                    if(new_map.containsKey(new_pos)){
                                        int[] savedSushi = new_map.get(new_pos);
                                        savedSushi[1] += sushi[1];
                                        new_map.put(new_pos, savedSushi);
                                    }else{
                                        new_map.put(new_pos, new int[]{t, sushi[1]});
                                    }
                                }
                            }

                        }
                        if(info[1] == 0) people.remove(name);
                        sushi_map.put(name, new_map);
                    }
                }else {
                    if (sushi_map.containsKey(name)) {
                        Map<Integer, int[]> map = sushi_map.get(name);
                        Map<Integer, int[]> new_map = new HashMap<>();
                        new_map.put(x, new int[]{t, 1});
                        for (int pos : map.keySet()) {
                            int[] sushi = map.get(pos);

                            int new_pos = (pos + (t - sushi[0])) % L;
                            if (new_map.containsKey(new_pos)) {
                                int[] savedSushi = new_map.get(new_pos);
                                savedSushi[1] += sushi[1];
                                new_map.put(new_pos, savedSushi);
                            } else {
                                new_map.put(new_pos, new int[]{t, sushi[1]});
                            }
                        }
                        sushi_map.put(name, new_map);
                    } else {
                        Map<Integer, int[]> map = new HashMap<>();
                        map.put(x, new int[]{t, 1});
                        sushi_map.put(name, map);
                    }
                }
            }else if(query == 200){
                // name인 사람이 t 시각에 x 위치에서 n개 초밥 제거 ( 자신의 name과 동일해야 함 )
                int t = Integer.parseInt(stk.nextToken());
                int x = Integer.parseInt(stk.nextToken());
                String name = stk.nextToken();
                int n = Integer.parseInt(stk.nextToken());

                if(sushi_map.containsKey(name)){
                    Map<Integer, int[]> map = sushi_map.get(name);
                    Map<Integer, int[]> new_map = new HashMap<>();
                    for(int pos : map.keySet()){
                        int[] sushi = map.get(pos);
                        int new_pos = (pos + (t - sushi[0])) % L;
                        if(new_pos != x){
                            if(new_map.containsKey(new_pos)){
                                int[] savedSushi = new_map.get(new_pos);
                                savedSushi[1] += sushi[1];
                                new_map.put(new_pos, savedSushi);
                            }else{
                                new_map.put(new_pos, new int[]{t, sushi[1]});
                            }
                        }else n -= sushi[1];
                    }

                    sushi_map.put(name, new_map);
                }

                if(n == 0) continue;
                people.put(name, new int[]{x, n});
            }else if(query == 300){
                // t 시각의 사람 수 와 초밥 수 출력
                int t = Integer.parseInt(stk.nextToken());

                Set<String> keySet = new HashSet<>(people.keySet());
                for(String name : keySet){
                    int[] info = people.get(name);

                    if(!sushi_map.containsKey(name) || sushi_map.get(name).size() == 0) continue;

                    Map<Integer, int[]> map = sushi_map.get(name);
                    Map<Integer, int[]> new_map = new HashMap<>();
                    for(int pos : map.keySet()){
                        int[] sushi = map.get(pos);
                        int time = t - sushi[0];
                        if(time >= L) info[1] -= sushi[1];
                        else{
                            int new_pos = (pos + time);
                            int p_pos = info[0] < pos ? info[0] + L : info[0];
                            if(pos <= p_pos && p_pos <= new_pos) info[1] -= sushi[1];
                            else{
                                new_pos %= L;
                                if(new_map.containsKey(new_pos)){
                                    int[] savedSushi = new_map.get(new_pos);
                                    savedSushi[1] += sushi[1];
                                    new_map.put(new_pos, savedSushi);
                                }else{
                                    new_map.put(new_pos, new int[]{t, sushi[1]});
                                }
                            }
                        }

                    }
                    if(info[1] == 0) people.remove(name);
                    sushi_map.put(name, new_map);
                }

                int sushi_cnt = 0;
                for(String name : sushi_map.keySet()){
                    for(int pos : sushi_map.get(name).keySet()){
                        sushi_cnt += sushi_map.get(name).get(pos)[1];
                    }
                }

                sb.append(people.size()).append(" ").append(sushi_cnt).append("\n");
            }
        }

        System.out.println(sb.toString());
    }
}