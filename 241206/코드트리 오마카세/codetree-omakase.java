import java.util.*;
import java.io.*;

/*
사람이 들어왔다면, 레일위의 대상 초밥들이 먹히는 시점이 정해짐

*/
public class Main {
    private static int L, Q;
    private static Queue<Sushi> sushi = new ArrayDeque<>();
    private static Map<String, int[]> people = new HashMap<>();

    private static class Sushi{
        int t, x;
        String name;
        Sushi(int t, int x, String name){
            this.t = t;
            this.x = x;
            this.name = name;
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
            int q = Integer.parseInt(stk.nextToken());
            if(q == 100){
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

                sushi.add(new Sushi(t, x, name));
            }else if(q == 200){
                // name인 사람이 t 시각에 x 위치에서 n개 초밥 제거 ( 자신의 name과 동일해야 함 )
                int t = Integer.parseInt(stk.nextToken());
                int x = Integer.parseInt(stk.nextToken());
                String name = stk.nextToken();
                int n = Integer.parseInt(stk.nextToken());

                int sushi_cnt = sushi.size();
                while(sushi_cnt-- > 0){
                    Sushi s = sushi.poll();

                    int time = t - s.t;
                    int pos = (s.x + time) % L;
                    if(!s.name.equals(name) || pos != x){
                        s.t = t;
                        s.x = pos;
                        sushi.add(s);
                    }else n--;
                }

                if(n == 0) continue;
                people.put(name, new int[]{x, n});
            }else if(q == 300){
                // t 시각의 사람 수 와 초밥 수 출력
                int t = Integer.parseInt(stk.nextToken());

                int sushi_cnt = sushi.size();
                while(sushi_cnt-- > 0){
                    Sushi s = sushi.poll();
                    if(!people.containsKey(s.name)){
                        s.x = (s.x + (t - s.t)) % L;
                        s.t = t;
                        sushi.add(s);
                        continue;
                    }


                    int[] info = people.get(s.name);

                    if(s.x == info[0]) info[1]--;
                    else {
                        int time = t - s.t;
                        if(time >= L) info[1]--;
                        else{
                            int pos = (s.x + time) % L;
                            if(info[0] <= pos) info[1]--;
                            else{
                                s.t = t;
                                s.x = pos;
                                sushi.add(s);
                            }
                        }
                    }


                    if(info[1] == 0) people.remove(s.name);
                }
                
                sb.append(people.size()).append(" ").append(sushi.size()).append("\n");
            }
        }

        System.out.println(sb.toString());
    }
}