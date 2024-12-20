import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N, M;

    private static Node[] belt;
    private static int[][] info;

    private static class Node{
        ArrayDeque<Integer> dq;
        Node prev, nxt;
        int size;
        Node(){
            this.dq = new ArrayDeque<>();
            this.prev = null;
            this.nxt = null;
            this.size = 0;
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
                belt = new Node[N + 1];
                for(int i = 0; i <= N; i++){
                    belt[i] = new Node();
                }

                M = Integer.parseInt(stk.nextToken());
                info = new int[M + 1][2];
                for(int i = 1; i <= M; i++){
                    int b_num = Integer.parseInt(stk.nextToken());
                    
                    if(!belt[b_num].dq.isEmpty()){
                        int prev = belt[b_num].dq.peekLast();
                        info[prev][1] = i;
                        info[i][0] = prev;
                    }

                    belt[b_num].dq.addLast(i);
                    belt[b_num].size++;
                }
            }else if("200".equals(command)){
                int src = Integer.parseInt(stk.nextToken());
                int dst = Integer.parseInt(stk.nextToken());

                if(belt[src].size > 0){
                    Node front = belt[src];
                    while(front.nxt != null){
                        front = front.nxt;
                    }

                    if(!belt[dst].dq.isEmpty()){
                        front.nxt = belt[dst];
                        belt[dst].prev = front;
                        int prev = front.dq.peekLast();
                        int nxt = belt[dst].dq.peekFirst();
                        info[prev][1] = nxt;
                        info[nxt][0] = prev;
                    }

                    belt[dst] = belt[src];
                    belt[src] = new Node();

                    while(front != null){
                        front.size = front.nxt == null ? front.dq.size() : front.dq.size() + front.nxt.size;
                        front = front.prev;
                    }
                }
                sb.append(belt[dst].size).append("\n");
            }else if("300".equals(command)){
                int src = Integer.parseInt(stk.nextToken());
                int dst = Integer.parseInt(stk.nextToken());

                Integer p1 = belt[src].dq.isEmpty() ? -1 : belt[src].dq.pollFirst();
                Integer p2 = belt[dst].dq.isEmpty() ? -1 : belt[dst].dq.pollFirst();

                if(belt[src].dq.isEmpty() && belt[src].nxt != null) belt[src] = belt[src].nxt;
                if(belt[dst].dq.isEmpty() && belt[dst].nxt != null) belt[dst] = belt[dst].nxt;

                if(p1 != -1){ // src to dst
                    if(!belt[dst].dq.isEmpty()){
                        int dst_p = belt[dst].dq.peekFirst();
                        info[p1][1] = dst_p;
                        info[dst_p][0] = p1;
                    }else info[p1][1] = 0;

                    belt[dst].dq.addFirst(p1);
                }else if(p1 == -1){
                    if(belt[dst].dq.isEmpty() && belt[dst].nxt != null) {
                        belt[dst] = belt[dst].nxt;
                        belt[dst].prev = null;
                    }

                    if(!belt[dst].dq.isEmpty()) info[belt[dst].dq.peekFirst()][0] = 0;
                }

                if(p2 != -1){ // dst to src
                    if(!belt[src].dq.isEmpty()){
                        int src_p = belt[src].dq.peekFirst();
                        info[p2][1] = src_p;
                        info[src_p][0] = p2;
                    }else info[p2][1] = 0;

                    belt[src].dq.addFirst(p2);
                }else if(p2 == -1){
                    if(belt[src].dq.isEmpty() && belt[src].nxt != null){
                        belt[src] = belt[src].nxt;
                        belt[src].prev = null;
                    }

                    if(!belt[src].dq.isEmpty()) info[belt[src].dq.peekFirst()][0] = 0;
                }

                belt[src].size = belt[src].nxt != null 
                    ? belt[src].dq.size() + belt[src].nxt.size
                    : belt[src].dq.size();

                    
                belt[dst].size = belt[dst].nxt != null 
                    ? belt[dst].dq.size() + belt[dst].nxt.size
                    : belt[dst].dq.size();
                
                sb.append(belt[dst].size).append("\n");
            }else if("400".equals(command)){
                int src = Integer.parseInt(stk.nextToken());
                int dst = Integer.parseInt(stk.nextToken());

                int srcSize = belt[src].size;
                if(srcSize > 1){
                    int len = Math.floorDiv(srcSize, 2);

                    Node newNode = new Node();
                    Node node = belt[src];
                    while(newNode.size < len){
                        while(newNode.size < len && !node.dq.isEmpty()){
                            newNode.dq.addLast(node.dq.pollFirst());
                            newNode.size++;
                        }

                        if(newNode.size == len) break;
                        node = node.nxt;
                    }

                    if(belt[dst].dq.isEmpty()){
                        info[newNode.dq.peekLast()][1] = 0;
                    }else{
                        newNode.nxt = belt[dst];
                        belt[dst].prev = newNode;
                        newNode.size += belt[dst].size;
                        info[newNode.dq.peekLast()][1] = belt[dst].dq.peekFirst();
                        info[belt[dst].dq.peekFirst()][0] = newNode.dq.peekLast();
                    }
                    belt[dst] = newNode;

                    if(node.dq.isEmpty()) node = node.nxt;
                    node.size = node.nxt == null ? node.dq.size() : node.dq.size() + node.nxt.size;
                    node.prev = null;
                    info[node.dq.peekFirst()][0] = 0;
                    belt[src] = node;
                }

                sb.append(belt[dst].size).append("\n");
            }else if("500".equals(command)){
                int p_num = Integer.parseInt(stk.nextToken());
                // p_num의 앞 뒤 num 찾기

                int a = info[p_num][0] == 0 ? -1 : info[p_num][0];
                int b = info[p_num][1] == 0 ? -1 : info[p_num][1];

                int value = a + (2 * b);
                sb.append(value).append("\n");
            }else if("600".equals(command)){
                int b_num = Integer.parseInt(stk.nextToken());

                int p_front = belt[b_num].dq.isEmpty() ? -1 : belt[b_num].dq.peekFirst();
                Node node = belt[b_num];
                while(node.nxt != null) node = node.nxt;
                int p_last = node.dq.isEmpty() ? -1 : node.dq.peekLast();

                int p_size = belt[b_num].size;

                int value = p_front + (2 * p_last) + (3 * p_size);
                sb.append(value).append("\n");
            }    
        }

        System.out.println(sb.toString());
    }
}