import java.util.*;
import java.io.*;

/*
사람이 들어왔다면, 레일위의 대상 초밥들이 먹히는 시점이 정해짐

*/
public class Main {
    private static int L, Q;
    private static Map<String, List<Sushi>> sushi_map = new HashMap<>();
    private static Map<String, int[]> people = new HashMap<>();

    private static class Sushi{
        int t, x;
        Sushi(int t, int x){
            this.t = t;
            this.x = x;
        }

        public int getT(){
            return this.t;
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
                    List<Sushi> list = sushi_map.get(name);
                    list.add(new Sushi(t, x));
                    Collections.sort(list, (o1, o2)->Integer.compare(o1.t, o2.t));
                }else{
                    List<Sushi> list = new ArrayList<>();
                    list.add(new Sushi(t, x));
                    sushi_map.put(name, list);
                }
            }else if(query == 200){
                // name인 사람이 t 시각에 x 위치에서 n개 초밥 제거 ( 자신의 name과 동일해야 함 )
                int t = Integer.parseInt(stk.nextToken());
                int x = Integer.parseInt(stk.nextToken());
                String name = stk.nextToken();
                int n = Integer.parseInt(stk.nextToken());
                
                if(sushi_map.containsKey(name)){
                    List<Sushi> list = sushi_map.get(name);
                    List<Sushi> new_list = new ArrayList<>();
                    for(Sushi s : list){
                        int time = t - s.t;
                        int pos = (s.x + time) % L;
                        if(pos != x){
                            s.t = t;
                            s.x = pos;
                            new_list.add(s);
                        }else n--;
                    }
                    
                    sushi_map.replace(name, new_list);
                }

                if(n == 0) continue;
                people.put(name, new int[]{x, n});
            }else if(query == 300){
                // t 시각의 사람 수 와 초밥 수 출력
                int t = Integer.parseInt(stk.nextToken());

                Set<String> keySet = new HashSet<>(people.keySet());
                for(String name : keySet){
                    int[] info = people.get(name);

                    if(!sushi_map.containsKey(name)) continue;
                    
                    List<Sushi> list = sushi_map.get(name);
                    int idx = Collections.binarySearch(list, new Sushi(t - L, -1), Comparator.comparingInt(Sushi::getT));
                    if(idx < 0) idx = -idx - 1;

                    info[1] -= idx;

                    list = list.subList(idx, list.size());
                    List<Sushi> new_list = new ArrayList<>();
                    for(Sushi s : list){
                        int time = t - s.t;
                        int p_pos = info[0];
                        if(p_pos < s.x) p_pos += L; 
                        if(s.x <= p_pos && p_pos <= (s.x + time)) info[1]--;
                        else{
                            s.t = t;
                            s.x = (s.x + time) % L;
                            new_list.add(s);
                        }

                    }
                    if(info[1] == 0) people.remove(name);
                    sushi_map.replace(name, new_list);
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