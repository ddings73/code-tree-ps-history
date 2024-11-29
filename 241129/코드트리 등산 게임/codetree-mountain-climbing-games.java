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
                List<Integer> mountain = new ArrayList<>(dq);

                List<int[]> list = new ArrayList<>();
                TreeSet<int[]> tSet = new TreeSet<>((o1, o2)->{
                    if(o1[0] == o2[0]) return Integer.compare(o2[1], o1[1]);
                    return Integer.compare(o1[0], o2[0]);
                });

                for(int i = 0; i < N; i++){
                    if(i == 0) {
                        int[] item = new int[]{mountain.get(i), 0};
                        list.add(item);
                        tSet.add(item);
                        continue;
                    }

                    int height = mountain.get(i);

                    // 내 앞에 온 높이 중에서 나보다 낮되, 가장 높은 점수 + 100만점 => 내 위치에서의 점수
                    List<int[]> sortedList = new ArrayList<>(tSet);
                    
                    int idx = -1;
                    int left = 0, right = sortedList.size() - 1;
                    while(left <= right){
                        int mid = (left + right)/2;
                        if(sortedList.get(mid)[0] < height){
                            idx = mid;
                            left = mid + 1;
                        }else{
                            right = mid - 1;
                        }
                    }

                    if(idx == -1) {
                        int[] item = new int[]{height, 0};
                        list.add(item);
                        tSet.add(item);
                    }
                    else{
                        List<int[]> subList = sortedList.subList(0, idx + 1);
                        Collections.sort(subList, (o1, o2)->o2[1] - o1[1]);

                        int[] item = new int[]{height, subList.get(0)[1] + 1000000};
                        list.add(item);
                        tSet.add(item);
                    }
                }

                int point = list.get(m_index)[1] + 1000000;

                int max = 0;
                for(int i = 0; i < N; i++){
                    max = Math.max(max, list.get(i)[0] + list.get(i)[1]);
                }

                point += max;
                sb.append(point).append('\n');
            }
        }

        System.out.println(sb.toString());
    }

}

