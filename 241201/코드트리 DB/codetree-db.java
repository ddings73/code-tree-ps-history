import java.util.*;
import java.io.*;

public class Main {

    private static int Q;

    private static Map<String, Integer> map = new HashMap<>();
    private static Map<Integer, String> map2 = new HashMap<>();
    private static TreeSet<Integer> set = new TreeSet<>();
    private static Node root;
    
    private static final int MAX = 1_000_000_000;
    
    private static class Node{
        String name;
        long value;
        Node left, right;
        Node(String name, int value){
            this.name = name;
            this.value = value;
            left = null;
            right = null;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        Node root = new Node(null, 0);
        
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            String query = stk.nextToken();

            if("init".equals(query)){ // 테이블 초기화
                map = new HashMap<>();
                map2 = new HashMap<>();
                set = new TreeSet<>();
                root = new Node(null, 0);
            }else if("insert".equals(query)){ // row(name, value) 추가
                String name = stk.nextToken();
                int value = Integer.parseInt(stk.nextToken());
                // 중복되는 name과 value는 존재하지 않음
                
                if(map.containsKey(name) || map2.containsKey(value)) sb.append("0\n");
                else{
                    long ret = update(root, 1, MAX, value, name, value);
                    
                    set.add(value);
                    map.put(name, value);
                    map2.put(value, name);

                    sb.append("1\n");
                }
            }else if("delete".equals(query)){ // name의 row 삭제
                String name = stk.nextToken();
                if(!map.containsKey(name)) sb.append("0\n");
                else{
                    int value = map.get(name);
                    update(root, 1, MAX, value, null, 0);
                    
                    map.remove(name);
                    map2.remove(value);
                    set.remove(value);

                    sb.append(value).append("\n");
                }
            }else if("rank".equals(query)){ // k번째로 작은 value의 row name 출력
                int k = Integer.parseInt(stk.nextToken());
                // k <= 100_000
                if(map.size() < k) sb.append("None\n");
                else{
                    List<Integer> list = new ArrayList<>(set);
                    int value = list.get(k - 1);
                    sb.append(map2.get(value)).append("\n");
                }
                
            }else if("sum".equals(query)){ // k이하 value를 가진 모든 row들의 value 합 출력
                int k = Integer.parseInt(stk.nextToken()); 
                // k <= 1_000_000_000

                long sum = query(root, 1, MAX, 1, k);
                sb.append(sum).append("\n");
            }
        }

        System.out.println(sb.toString());
    }

    private static long update(Node node, int from, int to, int target, String name, int value){
        if(target < from || to < target) return node.value;
        if(from == to){
            node.name = name;
            node.value = value;
            return node.value;
        }

        int mid = (from + to) / 2;

        if(node.left == null) node.left = new Node(null, 0);
        long left_value = update(node.left, from, mid, target, name, value);

        if(node.right == null) node.right = new Node(null, 0);
        long right_value = update(node.right, mid + 1, to, target, name, value);

        node.value = left_value + right_value;
        return node.value;
    }

    private static long query(Node node, int from, int to, int start, int end){
        if(end < from || to < start) return 0L;
        if(start <= from && to <= end) return node.value;

        long sum = 0L;
        int mid = (from + to) / 2;

        if(node.left != null) sum += query(node.left, from, mid, start, end);
        if(node.right != null) sum += query(node.right, mid + 1, to, start, end);

        return sum;
    }
}

// 9000 12000 18000 20000