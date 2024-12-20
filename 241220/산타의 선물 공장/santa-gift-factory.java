import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N, M;
    
    private static Belt[] belt_arr;
    private static Box[] box_arr;
    private static Map<Integer, Box> box_map;
    private static class Box{
        int id, w;
        Box nxt, prev;
        Box(int id, int w){
            this.id = id;
            this.w = w;
            this.nxt = null;
            this.prev = null;
        }
    }
    
    private static class Belt{
        Set<Integer> boxIDSet;
        Box head, tail;
        boolean broken;
        
        Belt(Box box){
            this.head = box;
            this.tail = box; 
            this.boxIDSet = new HashSet<>();
            this.boxIDSet.add(box.id);
            this.broken = false;
        }

        public void broke(){
            this.boxIDSet.clear();
            this.head = null;
            this.tail = null;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            String command = stk.nextToken();
            if("100".equals(command)){
                N = Integer.parseInt(stk.nextToken());
                M = Integer.parseInt(stk.nextToken());

                box_arr = new Box[N + 1];
                box_map = new HashMap<>();
                belt_arr = new Belt[N + 1];

                for(int i = 1; i <= N; i++){
                    int id = Integer.parseInt(stk.nextToken());
                    Box box = new Box(id, 0);
                    box_arr[i] = box;
                }

                for(int i = 1; i <= N; i++){
                    int w = Integer.parseInt(stk.nextToken());
                    box_arr[i].w = w;
                }

                for(int i = 1; i <= N; i++){
                    int idx = ((i-1) / (N/M)) + 1;
                    Box box = box_arr[i];
                    box_map.put(box.id, box);
                    if(belt_arr[idx] == null) belt_arr[idx] = new Belt(box);
                    else{
                        Belt belt = belt_arr[idx];
                        belt.boxIDSet.add(box.id);
                        belt.tail.nxt = box;
                        box.prev = belt.tail;
                        belt.tail = box;
                    }
                }
            }else if("200".equals(command)){
                int w_max = Integer.parseInt(stk.nextToken());
                int w_sum = 0;
                for(int i = 1; i <= M; i++){
                    Belt belt = belt_arr[i];
                    Box head = belt.head;
                    if(head == null) continue;
                    if(head.w <= w_max){
                        w_sum += head.w;
                        belt.head = head.nxt;
                        belt.head.prev = null;

                        belt.boxIDSet.remove(head.id);
                    }else if(head.nxt != null){
                        belt.head = head.nxt;
                        belt.head.prev = null;

                        belt.tail.nxt = head;
                        head.prev = belt.tail;

                        belt.tail = head;
                    }
                }
                sb.append(w_sum).append("\n");
            }else if("300".equals(command)){
                int r_id = Integer.parseInt(stk.nextToken());

                int result = -1;
                for(int i = 1; i <= M; i++){
                    Belt belt = belt_arr[i];
                    if(belt.boxIDSet.contains(r_id)){
                        Box box = box_map.get(r_id);

                        belt.boxIDSet.remove(r_id);
                        box.prev.nxt = box.nxt;
                        box.nxt.prev = box.prev;

                        box_map.remove(r_id);
                        result = r_id;
                    }
                }
                sb.append(result).append("\n");
            }else if("400".equals(command)){
                int f_id = Integer.parseInt(stk.nextToken());
                int result = -1;
                for(int i = 1; i <= M; i++){
                    Belt belt = belt_arr[i];
                    if(belt.boxIDSet.contains(f_id)){
                        result = i;
                        Box box = box_map.get(f_id); 

                        box.prev.nxt = null;
                        belt.tail.nxt = belt.head;
                        belt.head.prev = belt.tail;

                        box.prev = null;
                        belt.head = box;
                        box_map.remove(f_id);
                    }
                }
                sb.append(result).append("\n");
            }else if("500".equals(command)){
                int b_num = Integer.parseInt(stk.nextToken());
                int result = -1;
                if(!belt_arr[b_num].broken){
                    result = b_num;
                    belt_arr[b_num].broken = true;
                    for(int i = 1; i <= M; i++){
                        int idx = b_num + i;
                        if(idx > M) idx -= M;
                        if(belt_arr[idx].broken) continue;
                        Belt belt = belt_arr[idx];
                        Belt broken_belt = belt_arr[b_num];

                        if(belt.head == null){
                            belt_arr[idx] = belt_arr[b_num];
                        }else{
                            belt.boxIDSet.addAll(broken_belt.boxIDSet);
                            belt.tail.nxt = broken_belt.head;
                            broken_belt.head.prev = belt.tail;
                            belt.tail = broken_belt.tail;
                        }
                        belt_arr[b_num].broke();
                        break;
                    }
                }
                sb.append(result).append("\n");
            }
        }
        System.out.println(sb.toString());
    }
}