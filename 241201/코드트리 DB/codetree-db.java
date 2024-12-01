import java.util.*;
import java.io.*;

public class Main {

    private static int Q;

    private static Map<String, Integer> map = new HashMap<>();
    private static Node root;
    
    private static final int MAX = 1_000_000_000;
    
    private static class Node{
        String name;
        long value;
        long count;
        Node left, right;
        Node(String name, int value){
            this.name = name;
            this.value = value;
            this.count = 0L;
            this.left = null;
            this.right = null;
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
                root = new Node(null, 0);
            }else if("insert".equals(query)){ // row(name, value) 추가
                String name = stk.nextToken();
                int value = Integer.parseInt(stk.nextToken());
                // 중복되는 name과 value는 존재하지 않음
                
                if(map.containsKey(name) || query(root, 1, MAX, value, value) != 0L) sb.append("0\n");
                else{
                    long ret = update(root, 1, MAX, value, name, value, 1)[0];
                    map.put(name, value);
                    sb.append("1\n");
                }
            }else if("delete".equals(query)){ // name의 row 삭제
                String name = stk.nextToken();
                if(!map.containsKey(name)) sb.append("0\n");
                else{
                    int value = map.get(name);
                    update(root, 1, MAX, value, null, 0, 0);

                    map.remove(name);

                    sb.append(value).append("\n");
                }
            }else if("rank".equals(query)){ // k번째로 작은 value의 row name 출력
                int k = Integer.parseInt(stk.nextToken());
                // k <= 100_000
                if(map.size() < k) sb.append("None\n");
                else{
                    Node node = root;
                    while(node.count > k){
                        long left_count = node.left != null ? node.left.count : 0L;

                        if(left_count < k){
                            k -= left_count;
                            node = node.right;
                        }else{
                            node = node.left;
                        }
                    }
                    sb.append(node.name).append("\n");
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

    private static long[] update(Node node, int from, int to, int target, String name, int value, int count){
        if(target < from || to < target) return new long[]{node.value, node.count};
        if(from == to){
            node.name = name;
            node.value = value;
            node.count = count;
            return new long[]{node.value, node.count};
        }

        int mid = (from + to) / 2;

        if(node.left == null) node.left = new Node(null, 0);
        long[] left_info = update(node.left, from, mid, target, name, value, count);

        if(node.right == null) node.right = new Node(null, 0);
        long[] right_info = update(node.right, mid + 1, to, target, name, value, count);

        node.value = left_info[0] + right_info[0];
        node.count = left_info[1] + right_info[1];
        node.name = node.right.name != null ? node.right.name : node.left.name;

        return new long[]{node.value, node.count};
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