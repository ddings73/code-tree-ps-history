import java.util.*;
import java.io.*;

public class Main {
    private static int N, Q;

    private static Node[] tree;
    private static class Node{
        Node parent;
        int power, sum;
        boolean alarm;
        int[] count;

        Node(Node parent, int power){
            this.parent = parent;
            this.power = power;
            this.sum = 0;
            this.alarm = true;
            this.count = new int[N + 1];
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stk = new StringTokenizer(br.readLine());
        N = Integer.parseInt(stk.nextToken());
        Q = Integer.parseInt(stk.nextToken());

        tree = new Node[N + 1];
        Set<Integer> leaf = new HashSet<>();
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
                }

                for(int i = 0; i < N; i++){
                    int a = Integer.parseInt(stk.nextToken());
                    tree[i + 1].power = a;
                }

                for(int i = 1; i <= N; i++){
                    update(tree[i], 1);
                }
            }else if("200".equals(command)){
                int c = Integer.parseInt(stk.nextToken());
                tree[c].alarm = !tree[c].alarm;

                if(tree[c].alarm){
                    update2(tree[c], 1);
                }else{
                    update2(tree[c], -1);
                }
            }else if("300".equals(command)){
                int c = Integer.parseInt(stk.nextToken());
                int power = Integer.parseInt(stk.nextToken());

                update(tree[c], -1);
                tree[c].power = power;
                update(tree[c], 1);
            }else if("400".equals(command)){
                int c1 = Integer.parseInt(stk.nextToken());
                int c2 = Integer.parseInt(stk.nextToken());
                if(tree[c1].alarm) update2(tree[c1], -1);
                if(tree[c2].alarm) update2(tree[c2], -1);

                Node c1_parent = tree[c1].parent;
                tree[c1].parent = tree[c2].parent;
                tree[c2].parent = c1_parent;

                if(tree[c1].alarm) update2(tree[c1], 1);
                if(tree[c2].alarm) update2(tree[c2], 1);
                
            }else if("500".equals(command)){
                int c = Integer.parseInt(stk.nextToken());
                sb.append(tree[c].sum).append("\n");
            }
        }

        System.out.println(sb.toString());
    }

    private static void update(Node node, int v){
        int depth = node.power;
        int power = node.power;

        while(depth-- > 0 && node.parent != null){
            Node parent = node.parent;
            parent.count[power] += v;
            parent.sum += v;
            node = parent;
        }
    }
    
    private static void update2(Node node, int v){
        Node origin = node;
        int op = origin.power;

        int d = 2;
        while(node.parent != null){
            Node parent = node.parent;

            if(op > 0){
                parent.count[origin.power] += v;
                parent.sum += v;
                op--;
            }

            for(int i = d; i <= N; i++){
                parent.count[i] += (v * origin.count[i]);
                parent.sum += (v * origin.count[i]);
            }
            node = parent;
            d++;
        }
    }
}


/*
0 1 1 2 2 3 3 5 6
0
    1 : 1
        2 : 1
            4 : 2
            5 : 1
                9 : 3
        3 : 1
            6 : 2
                8 : 1
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