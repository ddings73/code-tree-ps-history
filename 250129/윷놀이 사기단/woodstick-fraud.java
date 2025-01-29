import java.util.*;
import java.io.*;

public class Main {
    private static Queue<Integer> q1, q2, q3, q4;
    public static void main(String[] args) {
        int[] move_info = input();

        setQueue();

        List<Queue<Integer>> q_List = new ArrayList<>();
        int answer = playGame(q_List, 4, 0, 0, move_info);

        System.out.println(answer);
    }
    
    private static void setQueue(){
        q1 = new ArrayDeque<>();
        for(int i = 0; i <= 40; i += 2) q1.add(i);

        q2 = new ArrayDeque<>();
        q2.add(10); q2.add(13); q2.add(16); q2.add(19);

        q3 = new ArrayDeque<>();
        q3.add(20); q3.add(22); q3.add(24);

        q4 = new ArrayDeque<>();
        q4.add(30); q4.add(28); q4.add(27); q4.add(26);

        for(int i = 25; i <= 40; i += 5){
            q2.add(i);
            q3.add(i);
            q4.add(i);
        }
    }

    private static int playGame(List<Queue<Integer>> q_list, int remain, int idx, int point, int[] move_info){
        if(idx == move_info.length) return point;

        int result = 0;
        int move = move_info[idx];

        // 새로운 말이 남아 있는경우
        if(remain > 0){
            Queue<Integer> newOne = new ArrayDeque(q1);
            for(int i = 0; i < move; i++) newOne.poll();

            boolean dup = false;
            List<Queue<Integer>> clone = new ArrayList<>();
            for(Queue<Integer> q : q_list){
                clone.add(new ArrayDeque<>(q));
                dup = dup || newOne.peek() == q.peek();
            }

            if(!dup){
                if(newOne.peek() == 10) newOne = new ArrayDeque<>(q2);
                
                clone.add(newOne);
                result = Math.max(result, playGame(clone, remain - 1, idx + 1, point + newOne.peek(), move_info)); 
            }
        }

        int len = q_list.size();
        for(int i = 0; i < len; i++){
            Queue<Integer> q = new ArrayDeque<>(q_list.get(i));
            for(int j = 0; !q.isEmpty() && j < move; j++) q.poll();
            
            
            List<Queue<Integer>> clone = new ArrayList<>();
            boolean dup = false;
            for(int j = 0; j < len; j++){
                if(i == j) continue;
                clone.add(new ArrayDeque<>(q_list.get(j)));

                if(q.isEmpty()) continue;
                dup = dup || q.peek() == q_list.get(j).peek();
            }

            if(dup) continue;
            

            int num = q.isEmpty() ? 0 : q.peek();
            if(!q.isEmpty()){
                if(num == 10){
                    q = new ArrayDeque<>(q2);
                }else if(num == 20){
                    q = new ArrayDeque<>(q3);
                }else if(num == 30 && q.size() > 3){
                    q = new ArrayDeque<>(q4);
                }

                clone.add(q);
            }

            result = Math.max(result, playGame(clone, remain, idx + 1, point + num, move_info));
        }


        return result;
    }

    private static int[] input(){
        int[] input_data = new int[10];
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            for(int i = 0; i < 10; i++){
                input_data[i] = Integer.parseInt(stk.nextToken());
            }    
        }catch(IOException e){
            e.printStackTrace();
        }
        return input_data;
    }
}

/*
2 4 10 25 10 30 40 19 10 30
2 4 10 8 30 10 40 19 10 25 

2 6 12 20 10 22 25 40 25 10
2 4 10 25 12 30 40 18 26 36

2 6 16 41 53 83 123 141 167 203

10 8 19 30 35 18 26 32 36 40
10 18 37 67 102 120 146 178 214 254 
*/