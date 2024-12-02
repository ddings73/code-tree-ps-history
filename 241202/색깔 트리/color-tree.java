import java.util.*;
import java.io.*;

public class Main {

    private static int Q;
    
    private static List<Node> roots = new ArrayList<>();
    private static Node[] nodes = new Node[100_001];
    private static ArrayDeque<int[]> colorDQ = new ArrayDeque<>();
    private static Set<Integer> ids;

    private static class Node{
        int m_id, p_id, color, max_depth, value;
        List<Node> childs;
        Set<Integer> childIds;
        Map<Integer, Integer> colors;
        Node(int m_id, int p_id, int color, int max_depth){
            this.m_id = m_id;
            this.p_id = p_id;
            this.color = color;
            this.max_depth = max_depth;
            this.value = 1;
            
            this.childs = new ArrayList<>();
            this.childIds = new HashSet<>();

            this.colors = new HashMap<>();
            this.colors.put(this.color, 1);
        }

        void insertChild(Node child){
            this.childs.add(child);
            this.childIds.add(child.m_id);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            int q = Integer.parseInt(stk.nextToken());
            if(q == 100){
                int m_id = Integer.parseInt(stk.nextToken());
                int p_id = Integer.parseInt(stk.nextToken());
                int color = Integer.parseInt(stk.nextToken());
                int max_depth = Integer.parseInt(stk.nextToken());

                updateColorQuery();
                
                Node node = new Node(m_id, p_id, color, max_depth);
                nodes[m_id] = node;
                if(p_id == -1) roots.add(node);
                else{
                    for(Node root : roots){
                        if(root.m_id == p_id || root.childIds.contains(p_id)){
                            insertNode(root, node, root.max_depth);
                            break;
                        }
                    }
                }
            }else if(q == 200){
                int m_id = Integer.parseInt(stk.nextToken());
                int color = Integer.parseInt(stk.nextToken());

                colorDQ.addLast(new int[]{m_id, color});
            }else if(q == 300){
                updateColorQuery();
                
                int m_id = Integer.parseInt(stk.nextToken());
                sb.append(nodes[m_id].color).append("\n");
            }else if(q == 400){
                updateColorQuery();
                int value = 0;
                for(Node root : roots){
                    value += root.value;
                }

                sb.append(value).append("\n");
            }
        }
        System.out.println(sb.toString());
    }

    private static boolean insertNode(Node now, Node newOne, int max_depth){
        if(--max_depth == 0) return false;

        if(now.m_id == newOne.p_id){
            now.insertChild(newOne);

            now.value -= (int)Math.pow(now.colors.size(), 2);
            now.colors.merge(newOne.color, 1, Integer::sum);
            now.value += ((int)(Math.pow(now.colors.size(), 2)) + 1);
            
            return true;
        }

        boolean result = false;

        now.value = 0;
        for(Node child : now.childs){
            if(child.m_id == newOne.p_id || child.childIds.contains(newOne.p_id)){
                result = insertNode(child, newOne, Math.min(max_depth, child.max_depth));
                if(result){
                    now.colors.merge(newOne.color, 1, Integer::sum);
                    now.childIds.add(newOne.m_id);
                }
            }
            now.value += child.value;
        }

        now.value += (int)(Math.pow(now.colors.size(), 2));
        return result;
    }

    private static void updateColorQuery(){
        ids = new HashSet<>();
        while(!colorDQ.isEmpty()){
            int[] info = colorDQ.pollLast();
            int m_id = info[0];
            int color = info[1];

            if(ids.contains(m_id)) continue;

            int tmp_value = nodes[m_id].value;
            int p_id = nodes[m_id].p_id;

            Map<Integer, Integer> map = new HashMap<>(nodes[m_id].colors);
            
            changeSubTree(nodes[m_id], color);

            int id = m_id;
            while(p_id != -1){
                Node parent = nodes[p_id];
                Map<Integer, Integer> tMap = new HashMap<>(parent.colors);

                // -parent의 컬러 수 제곱 -tmp_value + 바뀐 parent의 컬러 수 제곱
                int minus_value = tmp_value + (int)Math.pow(parent.colors.size(), 2);

                for(int key : map.keySet()){
                    int value = map.get(key);
                    int v = parent.colors.get(key);

                    if(value == v) parent.colors.remove(key);
                    else parent.colors.put(key, v - value);
                } 
                

                for(int key : nodes[id].colors.keySet()){
                    parent.colors.merge(key, nodes[id].colors.get(key), Integer::sum);
                }
                
                int plus_value = nodes[id].value + (int)Math.pow(parent.colors.size(), 2);
                int new_value = parent.value + plus_value - minus_value;

                tmp_value = parent.value;
                parent.value = new_value;

                id = parent.m_id;
                p_id = parent.p_id;
                map = tMap;
            }

            ids.add(m_id);
            ids.addAll(nodes[m_id].childIds);
        }
    }

    private static void changeSubTree(Node now, int color){
        if(ids.contains(now.m_id))return;

        now.colors = new HashMap<>();
        now.color = color;
        now.colors.put(color, 1);
        
        now.value = 1;
        for(Node child : now.childs){
            changeSubTree(child, color);
            
            now.value += child.value;
            now.colors.merge(color, 1, Integer::sum);
        }
    }
}

