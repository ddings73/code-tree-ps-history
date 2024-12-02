import java.util.*;
import java.io.*;

public class Main {

    private static int Q;
    
    private static List<Node> roots = new ArrayList<>();
    private static Node[] nodes = new Node[100_001];
    private static ArrayDeque<int[]> colorDQ = new ArrayDeque<>();

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

    // max_depth => 현재 노드에서 부터의 최대 깊이( 1부터 시작 )
    private static boolean insertNode(Node now, Node newOne, int max_depth){
        if(--max_depth == 0) return false;

        if(now.m_id == newOne.p_id){
            now.insertChild(newOne);

            now.value -= (int)Math.pow(now.colors.size(), 2);
            now.colors.add(newOne.color);
            now.value += ((int)(Math.pow(now.colors.size(), 2)) + 1);
            
            return true;
        }

        boolean result = false;

        now.value = 0;
        for(Node child : now.childs){
            if(child.m_id == newOne.p_id || child.childIds.contains(newOne.p_id)){
                result = insertNode(child, newOne, Math.min(max_depth, child.max_depth));
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

    private static void updateColorQuery(){
        Set<Integer> ids = new HashSet<>();
        while(!colorDQ.isEmpty()){
            int[] info = colorDQ.pollLast();
            int m_id = info[0];
            int color = info[1];

            if(ids.contains(m_id)) continue;

            ids.add(m_id);
            ids.addAll(nodes[m_id].childIds);
            for(Node root : roots){
                if(root.m_id == m_id || root.childIds.contains(m_id)){
                    changeColor(root, m_id, color, root.m_id == m_id);
                    break;
                }
            }
        }
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
}