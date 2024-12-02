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
        Map<Integer, Node> childs;
        Map<Integer, Integer> childIds;
        Map<Integer, Integer> colors;
        Node(int m_id, int p_id, int color, int max_depth){
            this.m_id = m_id;
            this.p_id = p_id;
            this.color = color;
            this.max_depth = max_depth;
            this.value = 1;
            
            this.childs = new HashMap<>();
            this.childIds = new HashMap<>();

            this.colors = new HashMap<>();
            this.colors.put(this.color, 1);
        }

        void insertChild(Node child){
            this.childs.put(child.m_id, child);
            this.childIds.put(child.m_id, child.m_id);
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
                        if(root.m_id == p_id || root.childIds.containsKey(p_id)){
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

    // max_depth => 현재 노드에서 부터의 최대 깊이( 1부터 시작 )
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

        Map<Integer, Integer> map = now.childIds;
        
        int child_id = map.get(newOne.p_id);
        Node child = now.childs.get(child_id);
        
        int tmp = now.value - child.value - (int)(Math.pow(now.colors.size(), 2));
        result = insertNode(child, newOne, Math.min(max_depth, child.max_depth));
        if(result){
            now.colors.merge(newOne.color, 1, Integer::sum);
            now.childIds.put(newOne.m_id, child_id);
            now.value = tmp + child.value;
            now.value += (int)(Math.pow(now.colors.size(), 2));
        }
        return result;
    }

    private static void updateColorQuery(){
        ids = new HashSet<>();
        while(!colorDQ.isEmpty()){
            int[] info = colorDQ.pollLast();
            int m_id = info[0];
            int color = info[1];

            if(ids.contains(m_id)) continue;
            
            for(Node root : roots){
                if(root.m_id == m_id || root.childIds.containsKey(m_id)){
                    changeColor(root, m_id, color, root.m_id == m_id);
                    break;
                }
            }
        }
    }

    private static Map<Integer, Integer> changeColor(Node now, int m_id, int color, boolean flag){
        Map<Integer, Integer> removeColors = new HashMap<>();
        
        if(ids.contains(now.m_id)) return removeColors;
        ids.add(now.m_id);
    
        if(flag){
            removeColors.put(now.color, 1);

            now.colors = new HashMap<>();
            now.color = color;
            now.colors.put(color, 1);

            now.value = 1;
            for(Node child : now.childs.values()){
                Map<Integer, Integer> ret = changeColor(child, m_id, color, true);

                for(int key : ret.keySet()){
                    int value = ret.get(key);
                    removeColors.merge(key, value, Integer::sum);
                }
                
                for(int key : child.colors.keySet()){
                    now.colors.merge(key, child.colors.get(key), Integer::sum); 
                }

                now.value += child.value;
            }
        }else{
            int child_id = now.childIds.get(m_id);
            Node child = now.childs.get(child_id);
            
            int tmp = now.value - child.value - (int)Math.pow(now.colors.size(), 2);
            Map<Integer, Integer> ret = changeColor(child, m_id, color, flag || child.m_id == m_id);

            removeColors = new HashMap<>(ret);
            for(int key : removeColors.keySet()){
                int value = removeColors.get(key);
                
                if(now.colors.get(key) == value) now.colors.remove(key);
                else now.colors.put(key, now.colors.get(key) - value);
            }

            for(int key : child.colors.keySet()){
                int value = child.colors.get(key);
                now.colors.merge(key, value, Integer::sum);
            }
            
            now.value = tmp + child.value;
            now.value += (int)(Math.pow(now.colors.size(), 2));

            removeColors.merge(now.color, 1, Integer::sum);
        }

        return removeColors;
    }
}

