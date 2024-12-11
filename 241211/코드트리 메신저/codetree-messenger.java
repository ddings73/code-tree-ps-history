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
        for(int i = 1; i <= N; i++){
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

                // 같은 부모를 가졌다면 무의미
                if(tree[c1].parent == tree[c2].parent) continue;

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
        int depth = 1;
        int power = node.power;

        while(node.alarm && depth <= power && node.parent != null){
            Node parent = node.parent;
            parent.count[power - depth] += v;
            parent.sum += v;
            node = parent;
            depth++;
        }
    }
    
    private static void update2(Node node, int v){
        Node origin = node;
        int depth = 1;

        int d = 1;
        while(node.parent != null){
            Node parent = node.parent;

            if(origin.power >= depth){
                parent.count[origin.power - depth] += v;
                parent.sum += v;
                depth++;
            }

            for(int i = d; i <= N; i++){
                parent.count[i - d] += (v * origin.count[i]);
                parent.sum += (v * origin.count[i]);
            }

            
            if(!parent.alarm) break;
            node = parent;
            d++;
        }
    }
}