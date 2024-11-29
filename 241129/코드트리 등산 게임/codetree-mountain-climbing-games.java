import java.util.*;
import java.io.*;

public class Main {
    private static int Q, N;
    private static ArrayDeque<Integer> dq = new ArrayDeque<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Q = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while(Q-- > 0){
            StringTokenizer stk = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(stk.nextToken());

            if(command == 100){ // 빅뱅
                N = Integer.parseInt(stk.nextToken());
                for(int i = 0; i < N; i++){
                    dq.addLast(Integer.parseInt(stk.nextToken()));
                }
                
            }else if(command == 200){ // 우공이산
                int height = Integer.parseInt(stk.nextToken());

                dq.addLast(height);
                N++;
            }else if(command == 300){ // 지진
                dq.pollLast();
                N--;
            }else if(command == 400){ // 등산 시뮬레이션
                /*
                    산 랜덤 선택
                    오른쪽 이동하면 100만점
                    케이블카 타면 100만점
                    더이상 이동 못하면 해당 산 높이만큼 점수 추가

                    첫 명령과 마지막 명령을 제외한 모든 명령이 산의 추가라면 약 550000의 산 개수가 됨
                    오른쪽 이동이 반드시 바로 옆 산으로 가는 것이 아님
                    오른쪽에 있는 산 중에서 내 위치보다 높기만 하면 되는듯
                */
                int m_index = Integer.parseInt(stk.nextToken()) - 1;

                List<int[]> list = new ArrayList<>();
                TreeMap<Integer, Integer> tMap = new TreeMap<>();
                List<Integer> mountain = new ArrayList<>(dq);

                for(int i = 0; i < N; i++){
                    if(i == 0) {
                        list.add(new int[]{mountain.get(i), 0});
                        tMap.put(mountain.get(i), 0);
                        continue;
                    }

                    int height = mountain.get(i);

                    // 내 앞에 온 높이 중에서 나보다 낮되, 가장 높은 점수 + 100만점 => 내 위치에서의 점수
                    List<Integer> cloneList = new ArrayList<>(tMap.keySet());
                    
                    int idx = binarySearch(cloneList, height);

                    if(idx == -1){
                        list.add(new int[]{height, 0});

                        if(!tMap.containsKey(height))
                            tMap.put(height, 0);
                    } else{
                        SortedMap<Integer, Integer> sortedMap = tMap.subMap(tMap.firstKey(), cloneList.get(idx) + 1);
                        TreeSet<Integer> tSet = new TreeSet(sortedMap.values());

                        int p = tSet.last() + 1000000;

                        list.add(new int[]{height, p});
                        if(tMap.containsKey(height) && tMap.get(height) >= p) continue; 
                        tMap.put(height, p);
                    }
                }

                int max = 0;
                for(int i = 0; i < N; i++){
                    max = Math.max(max, list.get(i)[0] + list.get(i)[1]);
                }

                int point = list.get(m_index)[1] + 1000000 + max;
                sb.append(point).append('\n');
            }
        }

        System.out.println(sb.toString());
    }

    private static int binarySearch(List<Integer> sortedList, int height){
        int idx = -1;
        int left = 0, right = sortedList.size() - 1;
        while(left <= right){
            int mid = (left + right)/2;
            if(sortedList.get(mid) < height){
                idx = mid;
                left = mid + 1;
            }else{
                right = mid - 1;
            }
        }

        return idx;
    }

}



// 57 47 79 70 22 92 7 3 11 94 2 47 8 86