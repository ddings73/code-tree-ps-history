import java.util.*;
import java.io.*;

public class Main {

    private static int Q;
    
    private static List<Node> roots = new ArrayList<>();
    private static class Node{
        int m_id, p_id, color, max_depth, value;
        List<Node> childs;
        Set<Integer> childIds;
        Set<Integer> colors;
        Node(int m_id, int p_id, int color, int max_depth){
            this.m_id = m_id;
            this.p_id = p_id;
            this.color = color;
            this.max_depth = max_depth;
            this.value = 1;
            this.childs = new ArrayList<>();
            this.childIds = new HashSet<>();
            this.colors = new HashSet<>();
            this.colors.add(this.color);
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

                Node node = new Node(m_id, p_id, color, max_depth);
                if(p_id == -1) roots.add(node);
                else{
                    for(Node root : roots){
                        if(root.m_id == p_id || root.childIds.contains(p_id)){
                            Map<Integer, Integer> map = new HashMap<>();
                            map.put(root.m_id, root.max_depth);
                            insertNode(root, node, map);
                            break;
                        }
                    }
                }
            }else if(q == 200){
                int m_id = Integer.parseInt(stk.nextToken());
                int color = Integer.parseInt(stk.nextToken());

                for(Node root : roots){
                    if(root.m_id == m_id || root.childIds.contains(m_id)){
                        changeColor(root, m_id, color, root.m_id == m_id);
                        break;
                    }
                }
            }else if(q == 300){
                int m_id = Integer.parseInt(stk.nextToken());

                for(Node root : roots){
                    if(root.m_id == m_id || root.childIds.contains(m_id)){
                        int color = selectNode(root, m_id);
                        sb.append(color).append("\n");
                        break;
                    }
                }
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

    // max_depth => 현재 노드에서 부터의 최대 깊이( 1부터 시작 )
    private static boolean insertNode(Node now, Node newOne, Map<Integer, Integer> depth_map){
        for(Integer m_id : depth_map.keySet()){
            Integer value = depth_map.get(m_id);
            if(value == 1) return false;
            depth_map.replace(m_id, value - 1);
        }

        if(now.m_id == newOne.p_id){
            now.insertChild(newOne);
            now.colors.add(newOne.color);

            now.value = (int)(Math.pow(now.colors.size(), 2));
            for(Node child : now.childs){
                now.value += child.value;
            }
            return true;
        }

        boolean result = false;

        now.value = 0;
        for(Node child : now.childs){
            if(child.m_id == newOne.p_id || child.childIds.contains(newOne.p_id)){
                depth_map.put(child.m_id, child.max_depth);
                result = insertNode(child, newOne, depth_map);
                if(result){
                    now.colors.add(newOne.color);
                    now.childIds.add(newOne.m_id);
                }
            }
            now.value += child.value;
        }

        now.value += (int)(Math.pow(now.colors.size(), 2));
        return result;
    }

    private static void changeColor(Node now, int m_id, int color, boolean flag){
        now.colors = new HashSet<>();
        if(flag){
            now.color = color;
            now.colors.add(color);
        }else{
            now.colors.add(now.color);
        }
        
        now.value = 0;
        for(Node child : now.childs){
            if(flag || child.m_id == m_id || child.childIds.contains(m_id)){
                changeColor(child, m_id, color, flag || child.m_id == m_id);
            }
            now.value += child.value;
            now.colors.addAll(child.colors);
        }
        
        now.value += (int)(Math.pow(now.colors.size(), 2));
    }

    private static int selectNode(Node now, int m_id){
        if(now.m_id == m_id) return now.color;

        int ret = 0;
        for(Node child : now.childs){
            if(child.m_id == m_id || child.childIds.contains(m_id)){
                ret = selectNode(child, m_id);
                break;
            }
        }

        return ret;
    }
}