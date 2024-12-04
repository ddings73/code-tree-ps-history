import java.util.*;
import java.io.*;


/*
    초기, 0번 도시를 출발지로 설정
    1) 도시와 간선 정보를 바탕으로 간선 연결
    2) id에 해당하는 상품으로 revenue만큼의 매출 / 도착지는 dest
    3) id에 해당하는 상품을 목록에서 제거
    4) revenue - cost 가 최대인 상품 중에서 id가 작은 상품 한개의 id 출력 및 상품 제거
    5) 출발지를 s로 변경
*/
public class Main {
    private static int Q, N, M;
    private static int start;

    private static int[] costs;
    private static List<List<int[]>> list = new ArrayList<>();

    private static Node root;

    private static class Node{
        int id, cost, revenue, dest;
        Node left, right;

        Node(int id, int cost, int revenue, int dest){
            this.id = id;
            this.cost = cost;
            this.revenue = revenue;
            this.dest = dest;
            this.left = null;
            this.right = null;
        }

        static Node emptyOne(){
            return new Node(-1, -1, -1, -1);
        }

        private void copyValue(Node node){
            this.id = node.cost == -1 ? -1 : node.id;
            this.cost = node.cost;
            this.revenue = node.revenue;
            this.dest = node.dest;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        
        root = Node.emptyOne();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            int q = Integer.parseInt(stk.nextToken());
            if(q == 100){
                landBuild(stk);
            }else if(q == 200){
                int id = Integer.parseInt(stk.nextToken());
                int revenue = Integer.parseInt(stk.nextToken());
                int dest = Integer.parseInt(stk.nextToken());
                int cost = costs[dest] == -1 || costs[dest] > revenue ? -1
                    : revenue - costs[dest];
                Node node = new Node(id, cost, revenue, dest);
                update(root, 1, 30000, id, id, node);
            }else if(q == 300){
                int id = Integer.parseInt(stk.nextToken());
                update(root, 1, 30000, id, id, Node.emptyOne());
            }else if(q == 400){
                int id = root.id;
                update(root, 1, 30000, id, id, Node.emptyOne());
                sb.append(id).append("\n");
            }else if(q == 500){
                int s = Integer.parseInt(stk.nextToken());
                start = s;
                
                calculateCost();
                rework(root, 1, 30000);
            }
        }
        System.out.println(sb.toString());
    }
    
    private static void landBuild(StringTokenizer stk){
        N = Integer.parseInt(stk.nextToken());
        for(int i = 0; i < N; i++){
            list.add(new ArrayList<>());
        }

        M= Integer.parseInt(stk.nextToken());
        for(int i = 0; i < M; i++){
            int v = Integer.parseInt(stk.nextToken());
            int u = Integer.parseInt(stk.nextToken());
            int w = Integer.parseInt(stk.nextToken());

            list.get(v).add(new int[]{u, w});
            list.get(u).add(new int[]{v, w});
        }

        calculateCost();
    }

    private static Node update(Node node, int from, int to, int left, int right, Node newOne){
        if(right < from || to < left) return node;
        if(from == to) return newOne;

        int mid = (from + to) / 2;
        if(node.left == null) node.left = Node.emptyOne();
        node.left = update(node.left, from, mid, left, right, newOne);

        if(node.right == null) node.right = Node.emptyOne();
        node.right = update(node.right, mid + 1, to, left, right, newOne);

        if(node.left.cost == node.right.cost || node.left.cost > node.right.cost){
            node.copyValue(node.left);
        }else{
            node.copyValue(node.right);
        }
        return node;
    }

    private static Node rework(Node node, int from, int to){
        if(from == to){
            if(node.dest != -1){
                node.cost = costs[node.dest] == -1 || costs[node.dest] > node.revenue ? -1
                    : node.revenue - costs[node.dest];
            } 
            return node;
        }

        int mid = (from + to) / 2;
        Node left = node.left == null ? Node.emptyOne() : rework(node.left, from, mid);
        Node right = node.right == null ? Node.emptyOne() : rework(node.right, mid + 1, to);

        if(left.cost == right.cost || left.cost > right.cost){
            node.copyValue(left);
        }else{
            node.copyValue(right);
        }

        return node;
    }

    private static void calculateCost(){
        costs = new int[N];
        Arrays.fill(costs, -1);

        boolean[] visit= new boolean[N];
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[1], o2[1]));
        pq.add(new int[]{start, 0});
        while(!pq.isEmpty()){
            int[] info = pq.poll();
            int pos = info[0];
            int cost = info[1];
            if(visit[pos]) continue;
            visit[pos] = true;
            costs[pos] = cost;

            for(int[] nxt : list.get(pos)){
                if(visit[nxt[0]]) continue;
                pq.add(new int[]{nxt[0], cost + nxt[1]});
            }
        }
    }
}