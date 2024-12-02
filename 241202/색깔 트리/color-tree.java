import java.util.*;
import java.io.*;

public class Main {

    private static int Q;
    
    private static List<Node> roots = new ArrayList<>();
    private static Node[] nodes = new Node[100_001];
    private static Map<Integer, Node> childMap = new HashMap<>();

    private static ArrayDeque<int[]> colorDQ = new ArrayDeque<>();

    private static class Node{
        int m_id, p_id, color, max_depth, value;
        Map<Integer, Node> childs;
        Set<Node> myChilds;
        Map<Integer, Integer> colors;
        Node(int m_id, int p_id, int color, int max_depth){
            this.m_id = m_id;
            this.p_id = p_id;
            this.color = color;
            this.max_depth = max_depth;
            this.value = 1;
            
            myChilds = new HashSet<>();
            this.childs = new HashMap<>();
            this.colors = new HashMap<>();
            this.colors.put(this.color, 1);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            int q = Integer.parseInt(stk.nextToken());

            if(q != 200) update();
            if(q == 100){
                int m_id = Integer.parseInt(stk.nextToken());
                int p_id = Integer.parseInt(stk.nextToken());
                int color = Integer.parseInt(stk.nextToken());
                int max_depth = Integer.parseInt(stk.nextToken());

                Node node = new Node(m_id, p_id, color, max_depth);
                nodes[m_id] = node;
                if(p_id == -1){
                    roots.add(node);
                    childMap.put(m_id, node);
                }else{
                    Node root = childMap.get(p_id);
                    if(insert(root, node, root.max_depth)){
                        childMap.put(m_id, root);
                    }
                }
            }else if(q == 200){
                int m_id = Integer.parseInt(stk.nextToken());
                int color = Integer.parseInt(stk.nextToken());

                colorDQ.addLast(new int[]{m_id, color});
            }else if(q == 300){
                int m_id = Integer.parseInt(stk.nextToken());
                sb.append(nodes[m_id].color).append("\n");
            }else if(q == 400){
                int value = 0;
                for(Node root : roots){
                    value += root.value;
                }

                sb.append(value).append("\n");
            }
        }
        System.out.println(sb.toString());
    }

    private static boolean insert(Node node, Node newOne, int max_depth){
        if(--max_depth == 0) return false;

        if(node.m_id == newOne.p_id){
            node.childs.put(newOne.m_id, newOne);
            node.myChilds.add(newOne);

            node.value -= multiply(node.colors.size());
            node.colors.merge(newOne.color, 1, Integer::sum);
            node.value += (multiply(node.colors.size()) + 1);
            return true;
        }

        Node child = node.childs.get(newOne.p_id);
        
        int cTemp = child.value;
        if(insert(child, newOne, Math.min(max_depth, child.max_depth))){
            node.value -= (multiply(node.colors.size()) + cTemp);
            node.colors.merge(newOne.color, 1, Integer::sum);
            node.value += (multiply(node.colors.size()) + child.value);

            node.childs.put(newOne.m_id, child);
            return true;
        }

        return false;
    }

    private static void update(){
        boolean[] visit = new boolean[100_001];
        while(!colorDQ.isEmpty()){
            int[] info = colorDQ.pollLast();
            int m_id = info[0];
            int color = info[1];
            if(visit[m_id]) continue;
            Node root = childMap.get(m_id);
            change(root, m_id, color, visit);
        }
    }

    private static void change(Node node, int m_id, int color, boolean[] visit){
        if(visit[m_id] || node.m_id == m_id) visit[node.m_id] = true;

        if(visit[m_id]){
            node.colors = new HashMap<>();
            node.color = color;
            node.colors.put(color, 1);

            node.value = 0;
            for(Node child : node.myChilds){
                if(!visit[child.m_id]) change(child, m_id, color, visit);
                Set<Integer> keys = child.colors.keySet();
                for(int key : keys){
                    node.colors.merge(key, child.colors.get(key), Integer::sum);
                }
                node.value += child.value;
            }
            node.value += multiply(node.colors.size());
        }else{
            Node child = node.childs.get(m_id);
            if(visit[child.m_id]) return;

            node.value -= multiply(node.colors.size()) + child.value;

            Set<Integer> childKeys = child.colors.keySet();
            for(int key : childKeys){
                int value = child.colors.get(key);
                int node_value = node.colors.get(key);
                if(node_value == value) node.colors.remove(key);
                else node.colors.put(key, node_value - value);
            }

            change(child, m_id, color, visit);

            childKeys = child.colors.keySet();
            for(int key : childKeys){
                node.colors.merge(key, child.colors.get(key), Integer::sum);
            }

            node.value += multiply(node.colors.size()) + child.value;
        }
    }

    

    private static int multiply(int value){
        return value * value;
    }
}

