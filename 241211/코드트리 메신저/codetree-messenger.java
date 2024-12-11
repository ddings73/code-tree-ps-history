import java.util.*;
import java.io.*;

public class Main {
    private static int N, Q;

    private static Node[] tree;
    private static class Node{
        Node parent;
        int power;
        Set<Integer> childs;
        boolean alarm;

        Node(Node parent, int power){
            this.parent = parent;
            this.power = power;
            this.childs = new HashSet<>();
            this.alarm = true;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        Q = Integer.parseInt(stk.nextToken());

        tree = new Node[N + 1];
        for(int i = 0; i <= N; i++){
            tree[i] = new Node(null, 0);
        }

        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            stk = new StringTokenizer(br.readLine());

            String command = stk.nextToken();
            if("100".equals(command)){
                for(int i = 0; i < N; i++){
                    int p = Integer.parseInt(stk.nextToken());
                    tree[i + 1].parent = tree[p];
                    tree[p].childs.add(i + 1);
                }

                for(int i = 0; i < N; i++){
                    int a = Integer.parseInt(stk.nextToken());
                    tree[i + 1].power = a;
                }
            }else if("200".equals(command)){
                int c = Integer.parseInt(stk.nextToken());
                tree[c].alarm = !tree[c].alarm;
            }else if("300".equals(command)){
                int c = Integer.parseInt(stk.nextToken());
                int power = Integer.parseInt(stk.nextToken());
                tree[c].power = power;
            }else if("400".equals(command)){
                int c1 = Integer.parseInt(stk.nextToken());
                int c2 = Integer.parseInt(stk.nextToken());

                Node c1_p = tree[c1].parent;
                Node c2_p = tree[c2].parent;

                if(c1_p == c2_p) continue;

                c1_p.childs.remove(c1);
                c1_p.childs.add(c2);

                c2_p.childs.remove(c2);
                c2_p.childs.add(c1);

                tree[c1].parent = c2_p;
                tree[c2].parent = c1_p;
            }else if("500".equals(command)){
                int c = Integer.parseInt(stk.nextToken());
                int count = counting(tree[c], 0);
                sb.append(count).append("\n");
            }
        }

        System.out.println(sb.toString());
    }

    private static int counting(Node node, int depth){
        int count = node.power < depth || depth == 0 ? 0 : 1;
        for(Integer idx : node.childs){
            Node child = tree[idx];
            if(!child.alarm) continue;
            count += counting(child, depth + 1);
        }

        return count;
    }
}


/*
0 1 1 2 2 3 3 5 6
0
    1 : 1
        2 : 1
            4 : 2
            5 : 1
                8 : 1
        3 : 1
            6 : 2
                9 : 3
            7 : 2


8 10 10 0 4 5 5 9 0 9
0
    4 : 2
        10 : 2
            2 : 1
            6 : 2
    9 : 1
        8 : 2
            1 : 2
        5 : 1
            3 : 3
            7 : 3
*/