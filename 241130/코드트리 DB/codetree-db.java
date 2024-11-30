import java.util.*;
import java.io.*;

public class Main {

    private static int Q;
    private static Map<String, Integer> map = new HashMap<>();
    private static Map<Integer, String> rMap = new HashMap<>();

    private static Map<Integer, Integer> idxMap = new HashMap<>();
    private static List<Integer> list = new LinkedList<>();
    private static List<Long> sumList = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();

        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            String query = stk.nextToken();

            if("init".equals(query)){ // 테이블 초기화
                map = new HashMap<>();
                rMap = new HashMap<>();

                idxMap = new HashMap<>();
                list = new LinkedList<>();
                sumList = new LinkedList<>();
            }else if("insert".equals(query)){ // row(name, value) 추가
                String name = stk.nextToken();
                int value = Integer.parseInt(stk.nextToken());
                // 중복되는 name과 value는 존재하지 않음
                Set<Integer> vSet = new HashSet<>(map.values());
                if(map.containsKey(name) || vSet.contains(value)) sb.append("0\n");
                else{
                    map.put(name, value);
                    rMap.put(value, name);

                    int idx = Collections.binarySearch(list, value);
                    if(idx < 0) idx = -idx - 1;

                    idxMap.put(value, idx);
                    list.add(idx, value);
                    
                    if(idx == 0) sumList.add(idx, Long.valueOf(value));
                    else sumList.add(idx, sumList.get(idx - 1) + value); 
                    
                    for(int i = idx + 1; i < sumList.size(); i++){
                        Long v = sumList.get(i);
                        sumList.set(i, v + value);
                    }

                    sb.append("1\n");
                }
            }else if("delete".equals(query)){ // name의 row 삭제
                String name = stk.nextToken();
                if(!map.containsKey(name)) sb.append("0\n");
                else{
                    Integer value = map.get(name);
                    map.remove(name);
                    rMap.remove(value);

                    int idx = idxMap.get(value);
                    idxMap.remove(value);

                    list.remove(idx);
                    sumList.remove(idx);
                    for(int i = idx; i < sumList.size(); i++){
                        Long v = sumList.get(i);
                        sumList.set(i, v - value);
                    }
                    sb.append(value).append("\n");
                }
            }else if("rank".equals(query)){ // k번째로 작은 value의 row name 출력
                int k = Integer.parseInt(stk.nextToken());
                // k <= 100_000
                if(map.size() < k) sb.append("None\n");
                else{
                    int value = list.get(k - 1);
                    String name = rMap.get(value);
                    sb.append(name).append("\n");
                }
            }else if("sum".equals(query)){ // k이하 value를 가진 row들의 value 합 출력
                int k = Integer.parseInt(stk.nextToken()); 
                // k <= 1_000_000_000

                int idx = Collections.binarySearch(list, k + 1);
                if(idx < 0) idx = -idx - 1;
                
                if(idx == 0) sb.append("0\n");
                else{
                    Long sum = sumList.get(idx - 1);
                    sb.append(sum).append("\n");
                }
            }
        }

        System.out.println(sb.toString());
    }
}

// 1000 9000 11000